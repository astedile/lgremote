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

import astedile.lgremote.api.roap.data.Key;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Mapping between computer keyboard keys and remote control keys that can be sent to TV.
 */
@Component
class KeyMapping extends HashMap<Integer, Key> {
    KeyMapping() {
        put(KeyEvent.VK_0, Key.NUMBER_0);
        put(KeyEvent.VK_1, Key.NUMBER_1);
        put(KeyEvent.VK_2, Key.NUMBER_2);
        put(KeyEvent.VK_3, Key.NUMBER_3);
        put(KeyEvent.VK_4, Key.NUMBER_4);
        put(KeyEvent.VK_5, Key.NUMBER_5);
        put(KeyEvent.VK_6, Key.NUMBER_6);
        put(KeyEvent.VK_7, Key.NUMBER_7);
        put(KeyEvent.VK_8, Key.NUMBER_8);
        put(KeyEvent.VK_9, Key.NUMBER_9);
        put(KeyEvent.VK_NUMPAD0, Key.NUMBER_0);
        put(KeyEvent.VK_NUMPAD1, Key.NUMBER_1);
        put(KeyEvent.VK_NUMPAD2, Key.NUMBER_2);
        put(KeyEvent.VK_NUMPAD3, Key.NUMBER_3);
        put(KeyEvent.VK_NUMPAD4, Key.NUMBER_4);
        put(KeyEvent.VK_NUMPAD5, Key.NUMBER_5);
        put(KeyEvent.VK_NUMPAD6, Key.NUMBER_6);
        put(KeyEvent.VK_NUMPAD7, Key.NUMBER_7);
        put(KeyEvent.VK_NUMPAD8, Key.NUMBER_8);
        put(KeyEvent.VK_NUMPAD9, Key.NUMBER_9);

        put(KeyEvent.VK_R, Key.RED);
        put(KeyEvent.VK_G, Key.GREEN);
        put(KeyEvent.VK_Y, Key.YELLOW);
        put(KeyEvent.VK_B, Key.BLUE);

        put(KeyEvent.VK_ENTER, Key.OK);

        put(KeyEvent.VK_PLUS, Key.VOLUME_PLUS);
        put(KeyEvent.VK_MINUS, Key.VOLUME_MINUS);
        put(KeyEvent.VK_ADD, Key.VOLUME_PLUS);
        put(KeyEvent.VK_SUBTRACT, Key.VOLUME_MINUS);

        put(KeyEvent.VK_MULTIPLY, Key.MUTE);

        put(KeyEvent.VK_ESCAPE, Key.BACK);
        put(KeyEvent.VK_BACK_SPACE, Key.BACK);

        put(KeyEvent.VK_PAGE_UP, Key.CHANNEL_UP);
        put(KeyEvent.VK_PAGE_DOWN, Key.CHANNEL_DOWN);

        put(KeyEvent.VK_LEFT, Key.LEFT);
        put(KeyEvent.VK_UP, Key.UP);
        put(KeyEvent.VK_RIGHT, Key.RIGHT);
        put(KeyEvent.VK_DOWN, Key.DOWN);
        put(KeyEvent.VK_KP_LEFT, Key.LEFT);
        put(KeyEvent.VK_KP_UP, Key.UP);
        put(KeyEvent.VK_KP_RIGHT, Key.RIGHT);
        put(KeyEvent.VK_KP_DOWN, Key.DOWN);

        put(KeyEvent.VK_HOME, Key.MENU);

        put(KeyEvent.VK_I, Key.PROGRAM_INFO);
        put(KeyEvent.VK_C, Key.CHANNEL_LIST);
        put(KeyEvent.VK_P, Key.PREVIOUS_CHANNEL);
    }

    String getDocumentation() {
        return "<html>\n" +
                "<style>" +
                "th {color:#191970;}" +
                "</style>" +
                "<table>" +
                "<tr>" +
                "<th colspan='2'>number keys</th>" +
                "<th>page up/down</th><td><em>channel up/down</em></td>" +
                "<th>+/-</th><td><em>volume up/down</em></td>" +
                "</tr>" +
                "<tr>" +
                "<th colspan='2'>cursor keys</th>" +
                "<th>home</th><td><em>menu</em></td>" +
                "<th>*</th><td><em>mute</em></td>" +
                "</tr>" +
                "<tr>" +
                "<th>backspace, ESC</th><td><em>back</em></td>" +
                "<th>enter</th><td><em>OK</em></td>" +
                "<th>i</th><td><em>program info</em></td>" +
                "</tr>" +
                "<tr>" +
                "<th>s</th><td><em>skip ch</em></td>"+
                "<th>r/g/y/b</th><td><em>red/green/yellow/blue</em></td>"+
                "<th>c</th><td><em>channel list</em></td>"+
                "</tr>" +
                "<tr>" +
                "<th>w</th><td><em>s<u>w</u>ap ch</em></td>"+
                "<th></th><td><em></em></td>"+
                "<th>p</th><td><em>previous ch</em></td>"+
                "</tr>" +
                "</table>" +
                "</html>";
    }
}
