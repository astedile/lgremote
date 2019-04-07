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

package astedile.lgremote.api.roap.data;

/**
 * Key codes that can be sent to TV.
 */
public enum Key {
    POWER(1),

    NUMBER_0(2),
    NUMBER_1(3),
    NUMBER_2(4),
    NUMBER_3(5),
    NUMBER_4(6),
    NUMBER_5(7),
    NUMBER_6(8),
    NUMBER_7(9),
    NUMBER_8(10),
    NUMBER_9(11),

    UP(12),
    DOWN(13),
    LEFT(14),
    RIGHT(15),

    OK(20),
    MENU(21),

    BACK(23),

    VOLUME_PLUS(24),
    VOLUME_MINUS(25),
    MUTE(26),

    CHANNEL_UP(27),
    CHANNEL_DOWN(28),

    RED(31),
    GREEN(30),
    YELLOW(32),
    BLUE(29),

    PROGRAM_INFO(45),
    CHANNEL_LIST(50),

    PREVIOUS_CHANNEL(403);

    private int code;

    Key(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
