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
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Panel to configure channels that should be suppressed when browsing through channels.
 */
@org.springframework.stereotype.Component
class SkipChannelPanel extends ObservablePanel {
    private static final Logger LOG = LoggerFactory.getLogger(SkipChannelPanel.class);
    private final JTextField channelToAdd;
    private final SkipChannelListModel<Integer> dataModel;

    SkipChannelPanel() {
        super(new GridBagLayout());

        dataModel = new SkipChannelListModel<>();
        dataModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                update();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                update();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                update();
            }

            private void update() {
                setChanged();
                notifyObservers(new GuiEvent(GuiEvent.Type.SET_SKIPPABLE_CHANNELS, dataModel.getList()));
            }
        });
        JList<Integer> list = new JList<>(dataModel);
        list.setFixedCellWidth(80);
        list.setFixedCellHeight(15);
        JScrollPane scrollPanel = new JScrollPane(list,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        add(scrollPanel, c);
        channelToAdd = new JTextField(2);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        add(channelToAdd, c);

        JButton addButton = new JButton("add");
        addButton.addActionListener(a -> {
            try {
                Integer channelNumber = Integer.valueOf(channelToAdd.getText());
                dataModel.add(channelNumber);
                list.clearSelection();
                channelToAdd.setText("");
                channelToAdd.grabFocus();
            } catch (NumberFormatException e) {
                LOG.error("Invalid number {}", channelToAdd.getText());
            }
        });

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        add(addButton, c);

        JButton removeButton = new JButton("remove");
        removeButton.addActionListener(a -> {
            list.getSelectedValuesList().forEach(dataModel::remove);
            list.clearSelection();
        });

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(removeButton, c);

        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(a -> {
            dataModel.clear();
            list.clearSelection();
        });

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(clearButton, c);

        JButton invertButton = new JButton("invert");
        invertButton.addActionListener(a -> {
            setChanged();
            notifyObservers(new GuiEvent(GuiEvent.Type.INVERT_SKIPPABLE_CHANNELS));
        });

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.gridwidth = GridBagConstraints.REMAINDER;
        add(invertButton, c);
    }

    void setCurrentChannel(int currentChannel) {
        this.channelToAdd.setText(String.valueOf(currentChannel));
    }

    void addChannelToList(int channel) {
        dataModel.add(channel);
    }

    void fillChannelList(Collection<Integer> channels) {
        dataModel.resetTo(channels);
    }

    private static class SkipChannelListModel<T extends Comparable<T>> extends AbstractListModel<T> {

        // Cannot be SortedSet because access by index is required.
        private final List<T> list = new ArrayList<>();

        List<T> getList() {
            return list;
        }

        @Override
        public int getSize() {
            return list.size();
        }

        @Override
        public T getElementAt(int index) {
            return list.get(index);
        }

        void add(T element) {
            int newIndex = Collections.binarySearch(list, element);
            if (newIndex < 0) {
                newIndex = -newIndex - 1;
                list.add(newIndex, element);
                fireIntervalAdded(this, newIndex, newIndex);
            }
        }

        void resetTo(Collection<T> channels) {
            if (channels.isEmpty()) {
                clear();
            } else {
                list.clear();
                list.addAll(new TreeSet<>(channels));
                fireContentsChanged(this, 0, list.size() - 1);
            }
        }

        void clear() {
            if (!list.isEmpty()) {
                int oldLastIndex = list.size() - 1;
                list.clear();
                fireIntervalRemoved(this, 0, oldLastIndex);
            }
        }

        void remove(T element) {
            int indexToRemove = Collections.binarySearch(list, element);
            if (indexToRemove >= 0) {
                list.remove(indexToRemove);
                fireIntervalAdded(this, indexToRemove, indexToRemove);
            }
        }
    }
}
