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
import java.util.List;

/**
 * Data object for retrieving list of channels from TV.
 */
@XmlRootElement(name = "envelope")
public class ChannelList {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        private int major;
        private int minor;
        private int sourceIndex;
        private int physicalNum;
        private String chname;

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public int getMinor() {
            return minor;
        }

        public void setMinor(int minor) {
            this.minor = minor;
        }

        public int getSourceIndex() {
            return sourceIndex;
        }

        public void setSourceIndex(int sourceIndex) {
            this.sourceIndex = sourceIndex;
        }

        public int getPhysicalNum() {
            return physicalNum;
        }

        public void setPhysicalNum(int physicalNum) {
            this.physicalNum = physicalNum;
        }

        @SuppressWarnings("SpellCheckingInspection")
        public String getChname() {
            return chname;
        }

        @SuppressWarnings("SpellCheckingInspection")
        public void setChname(String chname) {
            this.chname = chname;
        }
    }
}
