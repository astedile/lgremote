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

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Panel with key listener that forwards key presses as remote control actions to TV.
 */
@Component
class VirtualRemoteControlPanel extends ObservablePanel {

    VirtualRemoteControlPanel(KeyMapping keyMapping) {
        add(new JLabel(keyMapping.getDocumentation()));
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                setChanged();
                notifyObservers(new GuiEvent(GuiEvent.Type.KEY_PRESSED, e.getKeyCode()));
            }
        });
    }
}
