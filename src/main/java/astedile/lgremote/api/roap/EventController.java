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

import astedile.lgremote.AsyncProcessor;
import astedile.lgremote.api.roap.data.Event;
import astedile.lgremote.gui.GuiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Service for receiving incoming events from TV.
 */
@RestController
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private GuiController guiController;
    @Autowired
    private AsyncProcessor asyncProcessor;

    @PostMapping("/roap/api/event")
    public void event(@RequestBody Event event) {
        log.info("Received {}", event);
        switch (event.getName()) {
            case "ChannelChangeResult":
                if ("SUCCESS".equals(event.getResult())
                        || "SAME CHANNEL".equals(event.getResult())) {
                    asyncProcessor.stopSetChannelWithRetries();
                }
                break;
            case "ChannelChanged":
                guiController.displayCurrentChannel();
                break;
            case "byebye":
                guiController.setDisconnected();
                break;
            default:
        }
    }
}

