/*
 * Copyright 2019 Alexander Stedile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package astedile.lgremote;

import astedile.lgremote.api.roap.RoapApiClient;
import astedile.lgremote.api.roap.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Proxy that handles communication from and to TV.
 */
@Component
public class TvProxy {

    private static final Logger LOG = LoggerFactory.getLogger(TvProxy.class);

    @Autowired
    private RoapApiClient roapApiClient;
    @Autowired
    private Configuration configuration;
    @Autowired
    private AsyncProcessor asyncProcessor;
    @Autowired
    private FavouriteChannelListSelector favouriteChannelListSelector;

    private NavigableMap<Integer, ChannelList.Data> channelList;
    private Integer currentChannelMajor;
    private Set<Integer> skippableChannels = Collections.emptySet();

    public void requestAuthKey() {
        try {
            roapApiClient.sendAuth(Auth.createAuthKeyReq());
        } catch (RestClientException e) {
            LOG.error("Call failed: ", e);
        }
    }

    private void authenticate() throws CallFailedException {
        try {
            String authKey = configuration.getTvAuthKey();
            roapApiClient.sendAuth(Auth.createAuthReq(authKey));
        } catch (RestClientException e) {
            LOG.error("Call failed: ", e);
            throw new CallFailedException();
        }
    }

    public void sendKey(Key key) {
        if (Key.CHANNEL_UP == key) {
            channelUpWithSkipping();
        } else if (Key.CHANNEL_DOWN == key) {
            channelDownWithSkipping();
        } else {
            Command keyInputCommand = KeyInputCommand.createKeyInputCommand(key);
            roapApiClient.sendCommand(keyInputCommand);
        }
    }

    public void connect() throws CallFailedException {
        authenticate();
        retrieveChannelList();
        favouriteChannelListIsSelected();
        sendKey(Key.OK);
    }

    public boolean favouriteChannelListIsSelected() {
        return favouriteChannelListSelector.favouriteChannelListIsSelected();
    }

    public void setFavouriteChannelList(char favouriteChannelList) throws CallFailedException {
        favouriteChannelListSelector.setFavouriteChannelList(favouriteChannelList);
    }

    public void disconnect() throws CallFailedException {
        try {
            roapApiClient.sendEvent(Event.createEvent("byebye"));
        } catch (RestClientException e) {
            LOG.error("Call failed: ", e);
            throw new CallFailedException();
        }
    }

    public ChannelList.Data retrieveCurrentChannel() throws CallFailedException {
        try {
            ChannelList.Data channel = roapApiClient.retrieveCurrentChannel().getData().get(0);
            setCurrentChannelMajor(channel.getMajor());
            return channel;
        } catch (RestClientException e) {
            LOG.error("Call failed: ", e);
            throw new CallFailedException();
        }
    }

    void retrieveChannelList() throws CallFailedException {
        try {
            channelList = roapApiClient.retrieveChannelList().getData().stream()
                    .collect(Collectors.toMap(ChannelList.Data::getMajor, Function.identity(), (a, b) -> b, TreeMap::new));
            LOG.info("Found {} channels.", channelList.size());
        } catch (RestClientException e) {
            LOG.error("Call failed: ", e);
            throw new CallFailedException();
        }
    }

    /**
     * Skip channels in up or down direction depending on previous channel.
     * Do nothing if the previous channel is not adjacent to the current one.
     */
    private void channelUpOrDownWithSkipping(int previousChannel) {
        // keySet is sorted.
        List<Integer> sortedChannels = new ArrayList<>(channelList.keySet());
        Integer currentChannelMajor = getCurrentChannelMajor();
        int currentIndex = Collections.binarySearch(sortedChannels, currentChannelMajor);
        int previousIndex = Collections.binarySearch(sortedChannels, previousChannel);
        int deltaIndex = currentIndex - previousIndex;
        int maxDeltaIndex = sortedChannels.size() - 1;
        if (deltaIndex == 1 || deltaIndex == -maxDeltaIndex) {
            channelUpWithSkipping();
        } else if (deltaIndex == -1 || deltaIndex == maxDeltaIndex) {
            channelDownWithSkipping();
        } else {
            LOG.info("Staying at skippable channel {} because it was chosen explicitly. Previous channel: {}, index delta: {}",
                    currentChannelMajor,
                    previousChannel,
                    deltaIndex);
        }
    }

    void channelUpWithSkipping() {
        goToFirstNonSkippableChannel(
                channelList.tailMap(getCurrentChannelMajor() + 1).keySet().stream(),
                channelList.keySet().stream());
    }

    void channelDownWithSkipping() {
        goToFirstNonSkippableChannel(
                reverseKeyStream((NavigableMap<Integer, ChannelList.Data>) channelList.headMap(getCurrentChannelMajor())),
                reverseKeyStream(channelList));
    }

    private <K, V> Stream<K> reverseKeyStream(NavigableMap<K, V> map) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(map.navigableKeySet().descendingIterator(), Spliterator.ORDERED),
                false);
    }

    @SafeVarargs
    private final void goToFirstNonSkippableChannel(Stream<Integer>... sortedChannelsStreams) {
        Optional<Integer> nextChannel = Arrays.stream(sortedChannelsStreams)
                .reduce(Stream.empty(), Stream::concat)
                .filter(c -> !getSkippableChannels().contains(c))
                .findFirst();
        if (nextChannel.isPresent()) {
            if (nextChannel.get().equals(getCurrentChannelMajor())) {
                LOG.info("Channel {} is already selected.", nextChannel.get());
            } else {
                asyncProcessor.startSetChannelWithRetries(nextChannel.get());
            }
        } else {
            LOG.info("Channel skipping is not applied because all channels are marked for skipping.");
        }
    }

    public void goToChannel(int channelNumber) {
        try {
            roapApiClient.sendCommand(Command.createHandleChannelChange(channelList.get(channelNumber)));
        } catch (RestClientException e) {
            LOG.error("Call failed: ", e);
        }
    }

    public Integer getCurrentChannelMajor() {
        return currentChannelMajor;
    }

    private void setCurrentChannelMajor(Integer currentChannelMajor) {
        if (Objects.equals(this.currentChannelMajor, currentChannelMajor)) {
            LOG.warn("Channel {} is set again.", currentChannelMajor);
        } else {
            Integer previousChannel = this.currentChannelMajor;
            this.currentChannelMajor = currentChannelMajor;
            LOG.info("Current channel is now {}", this.currentChannelMajor);
            if (previousChannel != null && getSkippableChannels().contains(currentChannelMajor)) {
                channelUpOrDownWithSkipping(previousChannel);
            }
        }
    }

    public Collection<Integer> getSkippableChannels() {
        return skippableChannels;
    }

    public void setSkippableChannels(Collection<Integer> skippableChannels) {
        this.skippableChannels = new TreeSet<>(skippableChannels);
        LOG.info("Skippable channels ({}) are now {}",
                skippableChannels.size(), skippableChannels);
    }

    public SortedMap<Integer, ChannelList.Data> getChannelList() {
        return channelList;
    }
}
