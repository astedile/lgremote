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

import astedile.lgremote.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Main GUI component.
 */
@Component
public class Gui extends Observable implements GuiObservable {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private JLabel currentChannel;
    private JPanel favChannelLists;
    private JButton offButton;

    @Autowired
    private ConnectPanel connectPanel;
    @Autowired
    private VirtualRemoteControlPanel virtualRemoteControlPanel;
    @Autowired
    private FeaturePanel featurePanel;
    @Autowired
    private SkipChannelPanel skipChannelPanel;
    @Autowired
    private Configuration configuration;

    @PostConstruct
    public void createAndShowGui() {
        JFrame frame = new JFrame("LG Remote");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        favChannelLists = new JPanel(new FlowLayout());
        favChannelLists.add(new JLabel("Fav Ch List"), BorderLayout.CENTER);
        Stream.of('A', 'B', 'C', 'D')
                .map(fl -> new JToggleButton(fl.toString()))
                .peek(flButton -> flButton.addActionListener(getFavListActionListener()))
                .forEach(flButton -> favChannelLists.add(flButton, BorderLayout.CENTER));

        JPanel header = new JPanel(new BorderLayout());
        header.add(currentChannel = new JLabel("0 - not connected"), BorderLayout.WEST);
        header.add(favChannelLists, BorderLayout.CENTER);
        header.add(offButton = new JButton("off"), BorderLayout.EAST);
        offButton.addActionListener(a -> {
            setChanged();
            notifyObservers(new GuiEvent(GuiEvent.Type.POWER_OFF));
        });

        frame.add(header, BorderLayout.NORTH);

        tabbedPane.addTab("connect", connectPanel);
        tabbedPane.addTab("virt RC", virtualRemoteControlPanel);
        tabbedPane.addTab("feature", featurePanel);
        tabbedPane.addTab("skip CH", skipChannelPanel);
        frame.add(tabbedPane);

        setChanged();
        notifyObservers(new GuiEvent(GuiEvent.Type.SET_DISCONNECTED));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                setChanged();
                notifyObservers(new GuiEvent(GuiEvent.Type.WINDOW_CLOSING));
            }
        });

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        adjustFormElements(false);
        frame.toFront();
    }

    private ActionListener getFavListActionListener() {
        return a -> {
            JToggleButton source = (JToggleButton) a.getSource();
            if (source.isSelected()) {
                source.setForeground(null);
                // Deselect all the other fav list buttons.
                Arrays.stream(source.getParent().getComponents())
                        .filter(c -> c != source)
                        .filter(c -> c instanceof JToggleButton)
                        .map(c -> (JToggleButton) c)
                        .forEach(tb -> tb.setSelected(false));
                // Switch TV to fav list.
                setChanged();
                notifyObservers(new GuiEvent(
                        GuiEvent.Type.SELECT_FAV_CHANNEL_LIST,
                        source.getText().charAt(0)));
            } else {
                source.setSelected(true);
            }
        };
    }

    void adjustFormElements(boolean connected) {
        if (!connected && tabbedPane.getSelectedIndex() > 0) {
            tabbedPane.setSelectedIndex(0);
        } else if (connected && tabbedPane.getSelectedIndex() == 0) {
            tabbedPane.setSelectedIndex(1);
        }
        offButton.setEnabled(connected);
        Arrays.stream(favChannelLists.getComponents())
                .filter(c -> c instanceof JToggleButton)
                .forEach(c -> c.setEnabled(connected));
        IntStream.range(1, tabbedPane.getTabCount())
                .forEach(i -> tabbedPane.setEnabledAt(i, connected));
        connectPanel.adjustFormElements(connected);
    }

    void setCurrentChannelAndText(Integer currentChannelMajor, String currentChannelText) {
        SwingUtilities.invokeLater(() -> {
            currentChannel.setText(String.format("%d - %s", currentChannelMajor, currentChannelText));
            skipChannelPanel.setCurrentChannel(currentChannelMajor);
            featurePanel.setCurrentChannel(currentChannelMajor);
        });
    }

    void setFavouriteChannelSelected(boolean favouriteChannelListIsSelected) {
        Arrays.stream(favChannelLists.getComponents())
                .filter(c -> c instanceof JToggleButton)
                .map(c -> (JToggleButton) c)
                .filter(button -> button.getText().charAt(0) == configuration.getFavouriteChannelList())
                .forEach(button -> {
                    button.setForeground(favouriteChannelListIsSelected ? null : Color.RED);
                    button.setSelected(favouriteChannelListIsSelected);
                });

    }
}
