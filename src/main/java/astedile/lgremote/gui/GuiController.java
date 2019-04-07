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

package astedile.lgremote.gui;

import astedile.lgremote.AsyncProcessor;
import astedile.lgremote.CallFailedException;
import astedile.lgremote.TvProxy;
import astedile.lgremote.api.roap.data.ChannelList;
import astedile.lgremote.api.roap.data.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Controller for GUI actions which are propagated via {@link Observer}'s
 * {@link #update(Observable, Object)} to prevent cyclic dependencies.
 */
@Component
public class GuiController implements Observer {
    private static final Logger LOG = LoggerFactory.getLogger(GuiController.class);

    @Autowired
    private List<GuiObservable> guiObservables;
    @Autowired
    private ConnectPanel connectPanel;
    @Autowired
    private Gui gui;
    @Autowired
    private TvProxy tvProxy;
    @Autowired
    private KeyMapping keyMapping;
    @Autowired
    private SkipChannelPanel skipChannelPanel;
    @Autowired
    private AsyncProcessor asyncProcessor;
    @Autowired
    private FeaturePanel featurePanel;

    @PostConstruct
    private void connectToObservables() {
        guiObservables.forEach(o -> o.addObserver(this));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GuiEvent) {
            GuiEvent guiEvent = (GuiEvent) arg;
            switch (guiEvent.getType()) {
                case SET_DISCONNECTED:
                    setDisconnected();
                    break;
                case REQUEST_AUTH_KEY:
                    tvProxy.requestAuthKey();
                    break;
                case CONNECT:
                    connect();
                    break;
                case DISCONNECT:
                    disconnect();
                    break;
                case KEY_PRESSED:
                    Integer keyCode = (Integer) guiEvent.getValue();
                    handleKeyPress(keyCode);
                    break;
                case POWER_OFF:
                    tvProxy.sendKey(Key.POWER);
                    break;
                case WINDOW_CLOSING:
                    if (connectPanel.isConnected()) {
                        disconnect();
                    }
                    break;
                case START_FADE_IN:
                    asyncProcessor.startFadeIn((Integer) guiEvent.getValue());
                    break;
                case START_FADE_OUT:
                    asyncProcessor.startFadeOut((Integer) guiEvent.getValue());
                    break;
                case START_CHANNELS_UP:
                    asyncProcessor.startChannelsUp((Integer) guiEvent.getValue());
                    break;
                case START_CHANNELS_DOWN:
                    asyncProcessor.startChannelsDown((Integer) guiEvent.getValue());
                    break;
                case STOP_FADE_IN:
                    asyncProcessor.stopFadeIn();
                    break;
                case STOP_FADE_OUT:
                    asyncProcessor.stopFadeOut();
                    break;
                case STOP_CHANNELS_UP:
                    asyncProcessor.stopChannelsUp();
                    break;
                case STOP_CHANNELS_DOWN:
                    asyncProcessor.stopChannelsDown();
                    break;
                case SET_SKIPPABLE_CHANNELS:
                    //noinspection unchecked
                    tvProxy.setSkippableChannels((List<Integer>) guiEvent.getValue());
                    break;
                case INVERT_SKIPPABLE_CHANNELS:
                    Set<Integer> allChannels = tvProxy.getChannelList().keySet();
                    Set<Integer> invertedList = new TreeSet<>(allChannels);
                    invertedList.removeAll(tvProxy.getSkippableChannels());
                    skipChannelPanel.fillChannelList(invertedList);
                    break;
                case SWAP_CURRENT_AND_MARKED_CHANNEL:
                    Integer newChannel = (Integer) guiEvent.getValue();
                    Integer currentChannel = tvProxy.getCurrentChannelMajor();
                    featurePanel.setSwapMarkedChannel(currentChannel);
                    tvProxy.goToChannel(newChannel);
                    break;
                case SELECT_FAV_CHANNEL_LIST:
                    Character favChannelList = (Character) guiEvent.getValue();
                    try {
                        tvProxy.setFavouriteChannelList(favChannelList);
                    } catch (CallFailedException e) {
                        LOG.warn("Filed switching to favourite channel list '{}'.", favChannelList);
                    }
                    break;
                default:
                    LOG.warn("Unhandled GuiEvent.Type {}", guiEvent.getType());
            }
        } else {
            LOG.warn("Unhandled observer object {}", arg);
        }
    }

    private void adjustFormElements(boolean connected) {
        gui.adjustFormElements(connected);
        displayCurrentChannel(connected);
    }

    public void displayCurrentChannel() {
        displayCurrentChannel(true);
    }

    private void displayCurrentChannel(boolean connected) {
        int newChannel = 0;
        String newChannelText = "not connected";
        if (connected) {
            try {
                ChannelList.Data currentChannel = tvProxy.retrieveCurrentChannel();
                newChannel = currentChannel.getMajor();
                newChannelText = currentChannel.getChname();
            } catch (CallFailedException e) {
                newChannelText = "failed to retrieve current channel";
            }
        }
        gui.setCurrentChannelAndText(newChannel, newChannelText);
    }

    private void setConnected() {
        adjustFormElements(true);
        gui.setFavouriteChannelSelected(tvProxy.favouriteChannelListIsSelected());
    }

    public void setDisconnected() {
        adjustFormElements(false);
    }

    private void connect() {
        try {
            tvProxy.connect();
            setConnected();
        } catch (CallFailedException e) {
            LOG.info("Connection process could not be completed.");
            setDisconnected();
        }
    }

    private void disconnect() {
        try {
            tvProxy.disconnect();
            setDisconnected();
        } catch (CallFailedException e) {
            LOG.info("Form fields are not disabled due to error.");
            setConnected();
        }
    }

    private void handleKeyPress(Integer keyCode) {
        if (KeyEvent.VK_S == keyCode) {
            Integer currentChannelMajor = tvProxy.getCurrentChannelMajor();
            skipChannelPanel.addChannelToList(currentChannelMajor);
        } else if (KeyEvent.VK_W == keyCode) {
            featurePanel.swapCurrentAndMarkedChannel();
        } else if (keyMapping.containsKey(keyCode)) {
            Key key = keyMapping.get(keyCode);
            tvProxy.sendKey(key);
        } else {
            LOG.info("Unhandled key press: keyCode = {}", keyCode);
        }
    }
}
