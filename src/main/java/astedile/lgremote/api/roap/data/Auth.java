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
 * Authentication request for connecting to TV.
 */
@XmlRootElement(name = "auth")
public class Auth {
    private String name;
    private String value;

    public static Auth createAuthKeyReq() {
        Auth auth = new Auth();
        auth.setName("AuthKeyReq");
        return auth;
    }

    public static Auth createAuthReq(String authKey) {
        Auth auth = new Auth();
        auth.setName("AuthReq");
        auth.setValue(authKey);
        return auth;
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
}
