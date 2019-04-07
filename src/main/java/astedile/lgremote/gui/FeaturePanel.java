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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Panel that offers features which start and stop scheduled events.
 */
@org.springframework.stereotype.Component
class FeaturePanel extends ObservablePanel {
    private static final Logger LOG = LoggerFactory.getLogger(FeaturePanel.class);

    private static final int FADE_IN_OUT_DEFAULT_DELAY_SECONDS = 2;
    private static final int CHANNELS_UP_DOWN_DEFAULT_DELAY_SECONDS = 6;

    private final JTextField fadeInOutDelaySecondsField;
    private final JToggleButton fadeOutButton;
    private final JToggleButton fadeInButton;
    private final JTextField channelsUpDownIntervalTextField;
    private final JToggleButton channelsDownButton;
    private final JToggleButton channelsUpButton;
    private final JTextField swapCurrentChannel;
    private final JTextField swapMarkedChannel;
    private final JButton swapCurrentAndMarkedChannel;

    FeaturePanel() {
        super(new GridLayout(0, 4));

        add(new JLabel("Delay [s]"));
        add(fadeInOutDelaySecondsField = new JTextField(String.valueOf(FADE_IN_OUT_DEFAULT_DELAY_SECONDS)));
        add(fadeOutButton = new JToggleButton("Fade out"));
        add(fadeInButton = new JToggleButton("Fade in"));

        fadeOutButton.addActionListener(a ->
                updateFormElementsEnabled((JToggleButton) a.getSource()));
        fadeInButton.addActionListener(a ->
                updateFormElementsEnabled((JToggleButton) a.getSource()));

        fadeOutButton.addActionListener(a -> {
            setChanged();
            if (fadeOutButton.isSelected()) {
                notifyObservers(new GuiEvent(GuiEvent.Type.START_FADE_OUT,
                        getIntervalSeconds(fadeInOutDelaySecondsField, FADE_IN_OUT_DEFAULT_DELAY_SECONDS)));
            } else {
                notifyObservers(new GuiEvent(GuiEvent.Type.STOP_FADE_OUT));
            }
        });
        fadeInButton.addActionListener(a -> {
            setChanged();
            if (fadeInButton.isSelected()) {
                notifyObservers(new GuiEvent(GuiEvent.Type.START_FADE_IN,
                        getIntervalSeconds(fadeInOutDelaySecondsField, FADE_IN_OUT_DEFAULT_DELAY_SECONDS)));
            } else {
                notifyObservers(new GuiEvent(GuiEvent.Type.STOP_FADE_IN));
            }
        });

        add(new JLabel("Delay [s]"));
        add(channelsUpDownIntervalTextField = new JTextField(String.valueOf(CHANNELS_UP_DOWN_DEFAULT_DELAY_SECONDS)));
        add(channelsDownButton = new JToggleButton("Channels down"));
        add(channelsUpButton = new JToggleButton("Channels up"));

        channelsUpButton.addActionListener(a ->
                updateFormElementsEnabled((JToggleButton) a.getSource()));
        channelsDownButton.addActionListener(a ->
                updateFormElementsEnabled((JToggleButton) a.getSource()));

        channelsUpButton.addActionListener(a -> {
            setChanged();
            if (channelsUpButton.isSelected()) {
                notifyObservers(new GuiEvent(GuiEvent.Type.START_CHANNELS_UP,
                        getIntervalSeconds(channelsUpDownIntervalTextField, CHANNELS_UP_DOWN_DEFAULT_DELAY_SECONDS)));
            } else {
                notifyObservers(new GuiEvent(GuiEvent.Type.STOP_CHANNELS_UP));
            }
        });
        channelsDownButton.addActionListener(a -> {
            setChanged();
            if (channelsDownButton.isSelected()) {
                notifyObservers(new GuiEvent(GuiEvent.Type.START_CHANNELS_DOWN,
                        getIntervalSeconds(channelsUpDownIntervalTextField, CHANNELS_UP_DOWN_DEFAULT_DELAY_SECONDS)));
            } else {
                notifyObservers(new GuiEvent(GuiEvent.Type.STOP_CHANNELS_DOWN));
            }
        });

        add(new JLabel("Current/marked ch"));
        add(swapCurrentChannel = new JTextField());
        add(swapMarkedChannel = new JTextField());
        add(swapCurrentAndMarkedChannel = new JButton("swap"));
        swapCurrentChannel.setEnabled(false);
        swapCurrentAndMarkedChannel.addActionListener(a -> swapCurrentAndMarkedChannel());
    }

    private int getIntervalSeconds(JTextField intervalTextField, int defaultIntervalSeconds) {
        int intervalSeconds;
        String intervalSecondsText = intervalTextField.getText();
        try {
            intervalSeconds = Integer.parseInt(intervalSecondsText);
        } catch (NumberFormatException e) {
            intervalSeconds = -1;
        }
        if (intervalSeconds < 1) {
            LOG.error("Invalid value fade in/out interval seconds '{}'. using default {}",
                    intervalSecondsText, defaultIntervalSeconds);
            return defaultIntervalSeconds;
        }
        return intervalSeconds;
    }

    private void updateFormElementsEnabled(JToggleButton button) {
        Arrays.stream(getComponents())
                .filter(c -> c instanceof JToggleButton || c instanceof JTextField)
                .filter(c -> c != button)
                .filter(c -> c != swapCurrentChannel)
                .forEach(c -> c.setEnabled(!button.isSelected()));
    }

    void setSwapMarkedChannel(Integer markedChannel) {
        swapMarkedChannel.setText(markedChannel.toString());
    }

    void setCurrentChannel(Integer currentChannel) {
        swapCurrentChannel.setText(currentChannel.toString());
        if (swapMarkedChannel.getText() == null || swapMarkedChannel.getText().isEmpty()) {
            setSwapMarkedChannel(currentChannel);
        }
    }

    void swapCurrentAndMarkedChannel() {
        setChanged();
        Integer swapMarkedChannelNumber;
        String markedChannel = swapMarkedChannel.getText();
        String currentChannel = swapCurrentChannel.getText();
        try {
            swapMarkedChannelNumber = Integer.valueOf(markedChannel);
        } catch (NumberFormatException e) {
            LOG.warn("Marked channel '{}' is not a number. Swapping with current Channel {}.",
                    markedChannel, currentChannel);
            swapMarkedChannelNumber = Integer.valueOf(swapCurrentChannel.getText());
        }
        swapCurrentChannel.setText(swapMarkedChannelNumber.toString());
        swapMarkedChannel.setText(currentChannel);
        notifyObservers(new GuiEvent(GuiEvent.Type.SWAP_CURRENT_AND_MARKED_CHANNEL, swapMarkedChannelNumber));
    }
}
