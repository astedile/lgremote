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

/**
 * Data object for sending commands to TV.
 */
@XmlRootElement(name = "command")
public class Command {
    private String name;
    private String value;
    private Integer major;
    private Integer minor;
    private Integer sourceIndex;
    private Integer physicalNum;

    public static Command createHandleChannelChange(ChannelList.Data ch) {
        Command c = new Command();
        c.setName("HandleChannelChange");
        c.setMajor(ch.getMajor());
        c.setMinor(ch.getMinor());
        c.setSourceIndex(ch.getSourceIndex());
        c.setPhysicalNum(ch.getPhysicalNum());
        return c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMajor() {
        return major;
    }

    public void setMajor(Integer major) {
        this.major = major;
    }

    public Integer getMinor() {
        return minor;
    }

    public void setMinor(Integer minor) {
        this.minor = minor;
    }

    public Integer getSourceIndex() {
        return sourceIndex;
    }

    public void setSourceIndex(Integer sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    public Integer getPhysicalNum() {
        return physicalNum;
    }

    public void setPhysicalNum(Integer physicalNum) {
        this.physicalNum = physicalNum;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Command{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", major=").append(major);
        sb.append(", minor=").append(minor);
        sb.append(", sourceIndex=").append(sourceIndex);
        sb.append(", physicalNum=").append(physicalNum);
        sb.append('}');
        return sb.toString();
    }
}
