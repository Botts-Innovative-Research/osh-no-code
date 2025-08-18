# MQTT Communication Module and API
The MQTT Communication Module provides an interface for MQTT messaging capabilities. It implements the IMQ interface allowing it to be used as an API or directly integrated within drivers for communicating with sensors over the MQTT protocol. 

### Communication Module
The MQTT Communication Module implements the IMQ interface to provide a way to interact with MQTT Services. 

##### Configuring
- **Module Name:** A name for the instance of the driver
- **Serial Number:** The platform's serial number, or a unique identifier
- **Auto Start:** Check the box to start this module when OSH node is launched

- **Client ID:** (Required) Unique MQTT Client Identifier
- **Protocol:** (Required) Protocol 
- **Broker Address:** (Required)  Enter the broker address and port (e.g. mqtt.meshtastic.org:1883)
- **Username:** (Optional) Authentication username
- **Password:** (Optional) Authentication password

- **Quality of Service:** Message delivery guarantee level
  - 0: At most once delivery
  - 1: At least once delivery
  - 2: Exactly once delivery
- **Retain:** Check the box if messages should be retained by broker
- **Clean Session:** (Optional) Start with clean session state



#### Including MQTTMessageQueue in Drivers
- In your drivers `Config` class include the MQTTMessageQueueConfig

```java
    @DisplayInfo(label="MQTT Comm Provider", desc = "Communication settings for using MQTT")
    public MqttMessageQueueConfig mqttConfig = new MqttMessageQueueConfig();
```

- 