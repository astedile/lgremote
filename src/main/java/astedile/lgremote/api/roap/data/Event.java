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
 * Event that can be sent and received.
 */
@XmlRootElement(name = "event")
public class Event {

    private String name;
    private String result;
    private String chtype;
    private Integer major;
    private Integer minor;
    private Integer sourceIndex;
    private String physicalNum;
    private String chname;
    private String progName;
    private Integer audioCh;
    private String inputSourceName;
    private String inputSourceType;
    private String labelName;
    private Integer inputSourceIdx;

    public static Event createEvent(String event) {
        Event e = new Event();
        e.setName(event);
        return e;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("WeakerAccess")
    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getChtype() {
        return chtype;
    }

    public void setChtype(String chtype) {
        this.chtype = chtype;
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

    public String getPhysicalNum() {
        return physicalNum;
    }

    public void setPhysicalNum(String physicalNum) {
        this.physicalNum = physicalNum;
    }

    public String getChname() {
        return chname;
    }

    public void setChname(String chname) {
        this.chname = chname;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public Integer getAudioCh() {
        return audioCh;
    }

    public void setAudioCh(Integer audioCh) {
        this.audioCh = audioCh;
    }

    public String getInputSourceName() {
        return inputSourceName;
    }

    public void setInputSourceName(String inputSourceName) {
        this.inputSourceName = inputSourceName;
    }

    public String getInputSourceType() {
        return inputSourceType;
    }

    public void setInputSourceType(String inputSourceType) {
        this.inputSourceType = inputSourceType;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Integer getInputSourceIdx() {
        return inputSourceIdx;
    }

    public void setInputSourceIdx(Integer inputSourceIdx) {
        this.inputSourceIdx = inputSourceIdx;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Event{");
        sb.append("name='").append(name).append('\'');
        sb.append(", result='").append(result).append('\'');
        sb.append(", chtype='").append(chtype).append('\'');
        sb.append(", major=").append(major);
        sb.append(", minor=").append(minor);
        sb.append(", sourceIndex=").append(sourceIndex);
        sb.append(", physicalNum='").append(physicalNum).append('\'');
        sb.append(", chname='").append(chname).append('\'');
        sb.append(", progName='").append(progName).append('\'');
        sb.append(", audioCh=").append(audioCh);
        sb.append(", inputSourceName='").append(inputSourceName).append('\'');
        sb.append(", inputSourceType='").append(inputSourceType).append('\'');
        sb.append(", labelName='").append(labelName).append('\'');
        sb.append(", inputSourceIdx=").append(inputSourceIdx);
        sb.append('}');
        return sb.toString();
    }
}
