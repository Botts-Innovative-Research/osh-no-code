import org.eclipse.paho.client.mqttv3.*;
import org.junit.Test;

public class MqttTest {

    @Test
    public void testMqtt() throws MqttException {
        MqttClient client = new MqttClient("", "clientId:1020202");
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);
//        connectOptions.setPassword("admin".toCharArray());
//        connectOptions.setUserName("admin");

        client.connect(connectOptions);


        MqttCallback callback = new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                if(!client.isConnected()) {
                    try {
                        client.reconnect();
                    } catch (MqttException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                byte[] payload = message.getPayload();
//                sendMsgToTopic(topic, payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        };
    }
}
