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

package astedile.lgremote;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Application configuration read from properties and overridable by command line parameters.
 */
@ConfigurationProperties
@Component
public class Configuration {
    private String tvIpAddress;
    private String tvAuthKey;
    private char favouriteChannelList;
    private int favouriteChannelListExpectedSize;

    public String getTvIpAddress() {
        return tvIpAddress;
    }

    public void setTvIpAddress(String tvIpAddress) {
        this.tvIpAddress = tvIpAddress;
    }

    public String getTvAuthKey() {
        return tvAuthKey;
    }

    public void setTvAuthKey(String tvAuthKey) {
        this.tvAuthKey = tvAuthKey;
    }

    public char getFavouriteChannelList() {
        return favouriteChannelList;
    }

    public void setFavouriteChannelList(char favouriteChannelList) {
        this.favouriteChannelList = favouriteChannelList;
    }

    int getFavouriteChannelListExpectedSize() {
        return favouriteChannelListExpectedSize;
    }

    public void setFavouriteChannelListExpectedSize(int favouriteChannelListExpectedSize) {
        this.favouriteChannelListExpectedSize = favouriteChannelListExpectedSize;
    }
}
