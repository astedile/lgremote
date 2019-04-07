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

/**
 * Class for event objects to notify the {@link GuiController} via the observer design pattern.
 */
class GuiEvent {
    private final Type type;
    private final Object value;

    GuiEvent(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    GuiEvent(Type type) {
        this(type, null);
    }

    Type getType() {
        return type;
    }

    Object getValue() {
        return value;
    }

    /**
     * List of supported and handled event types.
     */
    public enum Type {
        SET_DISCONNECTED,
        REQUEST_AUTH_KEY,
        CONNECT,
        KEY_PRESSED,
        START_FADE_OUT, STOP_FADE_OUT,
        START_FADE_IN, STOP_FADE_IN,
        START_CHANNELS_UP, STOP_CHANNELS_UP,
        START_CHANNELS_DOWN, STOP_CHANNELS_DOWN,
        SET_SKIPPABLE_CHANNELS,
        INVERT_SKIPPABLE_CHANNELS,
        SWAP_CURRENT_AND_MARKED_CHANNEL,
        SELECT_FAV_CHANNEL_LIST,
        DISCONNECT,
        POWER_OFF,
        WINDOW_CLOSING
    }
}
