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

import astedile.lgremote.api.roap.data.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

/**
 * Starts and stops asynchronously scheduled tasks.
 */
@Component
public class AsyncProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AsyncProcessor.class);

    @Autowired
    private TvProxy tvProxy;
    @Autowired
    private TaskScheduler taskScheduler;

    private ScheduledFuture<?> setChannelJob;
    private ScheduledFuture<?> fadeOutJob;
    private ScheduledFuture<?> fadeInJob;
    private ScheduledFuture<?> channelUpJob;
    private ScheduledFuture<?> channelDownJob;
    private int fadeInLimit;

    void startSetChannelWithRetries(int channelNumber) {
        stopSetChannelWithRetries();
        LOG.info("Scheduling setting channel with retries");
        setChannelJob = taskScheduler.scheduleAtFixedRate(
                () -> tvProxy.goToChannel(channelNumber),
                Duration.ofSeconds(1));
    }

    public void stopSetChannelWithRetries() {
        if (setChannelJob != null) {
            LOG.info("Stopping setting channel with retries");
            setChannelJob.cancel(false);
            setChannelJob = null;
        }
    }

    public void startFadeOut(int intervalSeconds) {
        stopFadeOut();
        LOG.info("Scheduling fade-out");
        fadeOutJob = taskScheduler.scheduleAtFixedRate(
                () -> tvProxy.sendKey(Key.VOLUME_MINUS),
                Duration.ofSeconds(intervalSeconds));
    }

    public void stopFadeOut() {
        if (fadeOutJob != null) {
            LOG.info("Stopping fade-out");
            fadeOutJob.cancel(false);
            fadeOutJob = null;
        }
    }

    public void startFadeIn(int intervalSeconds) {
        stopFadeIn();
        LOG.info("Scheduling fade-in");
        fadeInLimit = 20;
        fadeInJob = taskScheduler.scheduleAtFixedRate(
                () -> {
                    if (fadeInLimit-- > 0) {
                        tvProxy.sendKey(Key.VOLUME_PLUS);
                    } else {
                        stopFadeIn();
                    }
                },
                Duration.ofSeconds(intervalSeconds));
    }

    public void stopFadeIn() {
        if (fadeInJob != null) {
            LOG.info("Stopping fade-in");
            fadeInJob.cancel(false);
            fadeInJob = null;
        }
    }

    public void startChannelsUp(int intervalSeconds) {
        stopChannelsUp();
        LOG.info("Scheduling channels up");
        channelUpJob = taskScheduler.scheduleAtFixedRate(
                tvProxy::channelUpWithSkipping,
                Duration.ofSeconds(intervalSeconds));
    }

    public void stopChannelsUp() {
        if (channelUpJob != null) {
            LOG.info("Stopping channels up");
            channelUpJob.cancel(false);
            channelUpJob = null;
        }
    }

    public void startChannelsDown(int intervalSeconds) {
        stopChannelsDown();
        LOG.info("Scheduling channels down");
        channelDownJob = taskScheduler.scheduleAtFixedRate(
                tvProxy::channelDownWithSkipping,
                Duration.ofSeconds(intervalSeconds));
    }

    public void stopChannelsDown() {
        if (channelDownJob != null) {
            LOG.info("Stopping channels down");
            channelDownJob.cancel(false);
            channelDownJob = null;
        }
    }
}
