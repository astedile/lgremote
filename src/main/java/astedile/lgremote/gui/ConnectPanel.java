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

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel for connecting to and disconnecting from TV.
 */
@org.springframework.stereotype.Component
class ConnectPanel extends ObservablePanel {

    @Autowired
    private Configuration configuration;

    private JTextField tvIpAddressField;
    private JButton authKeyRequestButton;
    private JTextField tvAuthKey;
    private JToggleButton connectButton;

    ConnectPanel() {
        super(new GridLayout(0, 3));
    }

    @PostConstruct
    void createForm() {
        add(new JLabel("TV IP Address"));
        add(tvIpAddressField = new JTextField(configuration.getTvIpAddress()));
        add(authKeyRequestButton = new JButton("Request auth key"));
        authKeyRequestButton.addActionListener(this::requestAuthClicked);

        add(new JLabel("TV Auth Key"));
        add(tvAuthKey = new JTextField(configuration.getTvAuthKey()));
        add(connectButton = new JToggleButton("Connect"));
        connectButton.addActionListener(this::connectOrDisconnectAction);
    }

    private void connectOrDisconnectAction(ActionEvent a) {
        JToggleButton button = (JToggleButton) a.getSource();
        button.setEnabled(false);
        if (button.isSelected()) {
            connectAction();
        } else {
            disconnectAction();
        }
    }

    private void connectAction() {
        configuration.setTvIpAddress(tvIpAddressField.getText());
        configuration.setTvAuthKey(tvAuthKey.getText());
        setChanged();
        notifyObservers(new GuiEvent(GuiEvent.Type.CONNECT));
    }

    private void disconnectAction() {
        setChanged();
        notifyObservers(new GuiEvent(GuiEvent.Type.DISCONNECT));
    }

    private void requestAuthClicked(ActionEvent a) {
        configuration.setTvIpAddress(tvIpAddressField.getText());
        setChanged();
        notifyObservers(new GuiEvent(GuiEvent.Type.REQUEST_AUTH_KEY));
    }

    void adjustFormElements(boolean connected) {
        tvIpAddressField.setEnabled(!connected);
        authKeyRequestButton.setEnabled(!connected);
        tvAuthKey.setEnabled(!connected);

        connectButton.setSelected(connected);
        connectButton.setText(connected ? "Disconnect" : "Connect");
        connectButton.setEnabled(true);
        if (!connected) {
            connectButton.grabFocus();
        }
    }

    boolean isConnected() {
        return connectButton.isSelected();
    }
}
