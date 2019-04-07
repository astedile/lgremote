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

package astedile.lgremote.api.roap;

import astedile.lgremote.Configuration;
import astedile.lgremote.api.roap.data.Auth;
import astedile.lgremote.api.roap.data.ChannelList;
import astedile.lgremote.api.roap.data.Command;
import astedile.lgremote.api.roap.data.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client for sending messages to TV.
 */
@Component
public class RoapApiClient {

    private static final Logger LOG = LoggerFactory.getLogger(RoapApiClient.class);
    private static final String ROAP_API_URI_TEMPLATE = "http://{tvIpAddress}:8080/roap/api/{action}";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Configuration configuration;

    public void sendAuth(Auth auth) {
        LOG.info("Sending auth request: {}", auth.getName());
        restTemplate.postForEntity(
                ROAP_API_URI_TEMPLATE,
                new HttpEntity<>(auth, getHttpHeaders()),
                String.class,
                configuration.getTvIpAddress(), "auth");
    }

    public void sendCommand(Command command) {
        LOG.info("Sending {}", command.toString());
        restTemplate.postForEntity(
                ROAP_API_URI_TEMPLATE,
                new HttpEntity<>(command, getHttpHeaders()),
                String.class,
                configuration.getTvIpAddress(), "command");
    }

    public void sendEvent(Event event) {
        LOG.info("Sending event: {}", event.getName());
        restTemplate.postForEntity(
                ROAP_API_URI_TEMPLATE,
                new HttpEntity<>(event, getHttpHeaders()),
                String.class,
                configuration.getTvIpAddress(), "event");
    }

    public ChannelList retrieveCurrentChannel() {
        LOG.info("Sending data request: {}", "cur_channel");
        ResponseEntity<ChannelList> responseEntity = restTemplate.exchange(
                ROAP_API_URI_TEMPLATE + "?target={target}",
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                ChannelList.class,
                configuration.getTvIpAddress(), "data", "cur_channel");
        return responseEntity.getBody();
    }

    public ChannelList retrieveChannelList() {
        LOG.info("Sending data request: {}", "channel_list");
        ResponseEntity<ChannelList> responseEntity = restTemplate.exchange(
                ROAP_API_URI_TEMPLATE + "?target={target}",
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                ChannelList.class,
                configuration.getTvIpAddress(), "data", "channel_list");
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_ATOM_XML);
        return headers;
    }
}
