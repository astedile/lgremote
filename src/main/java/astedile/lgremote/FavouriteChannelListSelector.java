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

import astedile.lgremote.api.roap.data.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Collections.nCopies;
import static java.util.Collections.singleton;

/**
 * Logic for selecting a favourite channel list on TV.
 */
@Component
class FavouriteChannelListSelector {
    private static final Logger LOG = LoggerFactory.getLogger(FavouriteChannelListSelector.class);

    private final Map<Key, Integer> pauseAfterMilliSeconds = new HashMap<Key, Integer>() {
        {
            put(Key.UP, 150);
            put(Key.LEFT, 150);
            put(Key.RIGHT, 150);
            put(Key.DOWN, 600);
            put(Key.BACK, 500);
            put(Key.OK, 600);
        }

        @Override
        public Integer get(Object key) {
            return containsKey(key) ? super.get(key) : 100;
        }
    };

    @Autowired
    private Configuration configuration;
    @Autowired
    private TvProxy tvProxy;

    boolean favouriteChannelListIsSelected() {
        int actualNumberOfChannels = tvProxy.getChannelList().size();
        int expectedNumberOfChannels = configuration.getFavouriteChannelListExpectedSize();
        return actualNumberOfChannels == expectedNumberOfChannels;
    }

/*
    void ensureFavouriteChannelListIsSelected() throws CallFailedException {
        char favouriteChannelList = configuration.getFavouriteChannelList();
        int favouriteChannelListExpectedSize = configuration.getFavouriteChannelListExpectedSize();

        if (favouriteChannelList == 0 || favouriteChannelListExpectedSize == 0) {
            LOG.info("Not checking for favourite channel because not all parameters are set. " +
                    "favouriteChannelList='{}', favouriteChannelListExpectedSize='{}'",
                    favouriteChannelList, favouriteChannelListExpectedSize);
            return;
        }
        if (favouriteChannelList < 'A' || favouriteChannelList > 'D') {
            LOG.warn("favouriteChannelList must be one of (A B C D).");
            return;
        }

        if (!favouriteChannelListIsSelected()) {
            setFavouriteChannelList(favouriteChannelList);
            if (favouriteChannelListIsSelected()) {
                LOG.info("Favourite channel list '{}' is selected.", favouriteChannelList);
            } else {
                LOG.warn("Failed to select favourite channel list '{}' or expected number of channels ({}) is incorrect.",
                        favouriteChannelList, favouriteChannelListExpectedSize);
            }
        }
    }
*/

    void setFavouriteChannelList(char favouriteChannelList) throws CallFailedException {
        LOG.info("Selecting favourite channel list '{}'.", favouriteChannelList);

        Stream.of(
                nCopies(2, Key.BACK),
                singleton(Key.CHANNEL_LIST),
                singleton(Key.BLUE),
                nCopies(5, Key.UP),
                nCopies(2, Key.LEFT),
                nCopies(1, Key.DOWN),
                nCopies(favouriteChannelList - 'A', Key.RIGHT),
                nCopies(2, Key.OK),
                nCopies(2, Key.BACK)
        )
                .flatMap(Collection::stream)
                .forEach(k -> {
                    tvProxy.sendKey(k);
                    try {
                        Thread.sleep(pauseAfterMilliSeconds.get(k));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        tvProxy.retrieveChannelList();
    }
}
