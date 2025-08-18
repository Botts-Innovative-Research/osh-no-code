package com.botts.impl.comm.mqtt;

import java.util.Map;

public class MqttMessageWrapper {
    final Map<String,String> attributes;
    final byte[] payload;

    MqttMessageWrapper(Map<String, String> attributes, byte[] payload){
        this.attributes = attributes;
        this.payload = payload;
    }
}
