package com.botts.impl.sensor.datafeed.config;

import org.sensorhub.api.comm.MessageQueueConfig;
import org.sensorhub.api.config.DisplayInfo;

public class MsgQueueCommConfig extends CommTypeConfig {
    @DisplayInfo(desc = "Communication settings for using a Message Queue provider")
    public MessageQueueConfig messageQueueCommSettings;
}
