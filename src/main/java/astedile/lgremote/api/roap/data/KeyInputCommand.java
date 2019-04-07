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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Specialized command for performing remote control key press actions.
 * With customized constructor and toString method.
 */
@XmlRootElement(name = "command")
public class KeyInputCommand extends Command {
    @XmlTransient
    private Key key;

    public KeyInputCommand() {
        setName("HandleKeyInput");
    }

    public static Command createKeyInputCommand(Key key) {
        KeyInputCommand c = new KeyInputCommand();
        c.setKey(key);
        return c;
    }

    private Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
        setValue(String.valueOf(key.getCode()));
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Command{");
        sb.append("name='").append(getName()).append('\'');
        sb.append(", value='").append(getValue()).append('\'');
        sb.append('}');
        sb.append(" - ").append(getKey());
        return sb.toString();
    }
}
