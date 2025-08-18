package com.botts.impl.comm.mqtt;


import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.paho.client.mqttv3.*;
import org.sensorhub.api.comm.IMessageQueuePush;
import org.sensorhub.api.common.SensorHubException;
import org.sensorhub.impl.module.AbstractSubModule;
import javax.net.ssl.SSLSocketFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class MqttMessageQueue extends AbstractSubModule<MqttMessageQueueConfig> implements IMessageQueuePush<MqttMessageQueueConfig> {
    private final Set<MessageListener> listeners = new CopyOnWriteArraySet<>();

    MqttClient mqttClient;

    public final static String QUALITY_OF_SERVICE = "qos";
    public final static String TOPIC_NAME = "topic";
    public final static String RETAINED = "retained";

    /**
     * @param config
     * @throws SensorHubException
     */
    @Override
    public void init(MqttMessageQueueConfig config) throws SensorHubException {
        super.init(config);
    }

    /**
     * @param config
     * @param protocol
     * @return
     */
    private static MqttConnectOptions getConnectOptions(MqttMessageQueueConfig config, String protocol) {

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setCleanSession(config.cleanSession);
        connectOptions.setKeepAliveInterval(60);
        connectOptions.setConnectionTimeout(10);
        connectOptions.setAutomaticReconnect(true);

        // auth
        if (config.username != null && config.username.trim().length() != 0)
            connectOptions.setUserName(config.username);

        if (config.password != null && config.password.trim().length() != 0)
            connectOptions.setPassword(config.password.toCharArray());

        if (protocol.equals("wss") || protocol.equals("ssl"))
            connectOptions.setSocketFactory(SSLSocketFactory.getDefault());

        //connectOptions.setSSLProperties(--);

        return connectOptions;
    }

    /**
     *
     */
    @Override
    public void start() throws SensorHubException{

        String protocol = config.protocol.getName();
        String brokerAddress = config.brokerAddress;
        String clientId = config.clientId;
        try {
            mqttClient = new MqttClient(protocol + "://" + brokerAddress, clientId);

            MqttConnectOptions connectOptions = getConnectOptions(config, protocol);
            mqttClient.connect(connectOptions);

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
//                if (!mqttClient.isConnected()) {
//                    try {
//                        mqttClient.reconnect();
//                    } catch (MqttException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Map<String, String> attributes = new HashMap<>();
                attributes.put(TOPIC_NAME, topic);
                attributes.put(QUALITY_OF_SERVICE, String.valueOf(mqttMessage.getQos()));
                attributes.put(RETAINED, String.valueOf(mqttMessage.isRetained()));

                for (MessageListener listener : listeners) {
                    listener.receive(attributes, mqttMessage.getPayload());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                getLogger().debug("managed to deliver: ", iMqttDeliveryToken.isComplete());
            }
        });


        if (config.enableSubscribe) {
            try {
                getLogger().info("Subscribed to topic: {}", config.topicName);
                mqttClient.subscribe(config.topicName, config.qos.getValue());
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     *
     * @throws SensorHubException
     */
    @Override
    public void stop() throws SensorHubException{

        if (mqttClient == null)
            return;

        try {
            mqttClient.disconnect();
            mqttClient.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param payload
     */
    @Override
    public void publish(byte[] payload) {
        publish(null, payload);
    }

    /**
     *
     * @param attrs
     * @param payload
     */
    @Override
    public void publish(Map<String, String> attrs, byte[] payload) {

        if (!config.enablePublish)
            return;

        MqttMessage mqttMessage = new MqttMessage(payload);

        if (attrs != null) {
            var qosAttr = attrs.get(QUALITY_OF_SERVICE);

            if (qosAttr != null) {
                int qos = Integer.parseInt(qosAttr);
                if (qos >= 0 && qos < MqttMessageQueueConfig.QoS.values().length) {
                    mqttMessage.setQos(qos);
                } else {
                    mqttMessage.setQos(config.qos.getValue());
                }
            }

            var retainedAttr = attrs.get(RETAINED);
            mqttMessage.setRetained(retainedAttr != null  ? Boolean.parseBoolean(qosAttr) : config.retain);
        }

        try {
            mqttClient.publish(config.topicName, mqttMessage);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param listener
     */
    @Override
    public void registerListener(MessageListener listener) {
        listeners.add(listener);
    }

    /**
     *
     * @param listener
     */
    @Override
    public void unregisterListener(MessageListener listener) {
        listeners.remove(listener);
    }

}
