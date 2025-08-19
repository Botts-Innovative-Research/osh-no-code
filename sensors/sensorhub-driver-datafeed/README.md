# DataFeed Driver

**Driver Dependencies:**
- `sensorhub-ui-datafeed` This module holds the custom configuration forms for the datafeed-driver.You must include the module and update the config.json under the `AdminUI` to include the custom forms:

```json
"customForms": [
{
"objClass": "org.sensorhub.ui.CustomUIConfig",
"configClass": "com.botts.impl.sensor.datafeed.DataFeedConfig",
"uiClass": "com.botts.ui.DataFeedConfigForm"
},
{
"objClass": "org.sensorhub.ui.CustomUIConfig",
"configClass": "com.botts.api.parser.DataParserConfig",
"uiClass": "com.botts.ui.DataParserConfigForm"
},
{
"objClass": "org.sensorhub.ui.CustomUIConfig",
"configClass": "com.botts.api.parser.data.DataField",
"uiClass": "com.botts.ui.ProtobufParserConfigForm"
},
{
"objClass": "org.sensorhub.ui.CustomUIConfig",
"configClass": "com.botts.impl.parser.protobuf.ProtobufDataParserConfig",
"uiClass": "com.botts.ui.ProtobufParserConfigForm"
}
],
```

## Configuration

### Communication Configuration
There are two types of communication providers currently implemented for the data feed driver: Stream Based and Message Queue based communication.

**Stream Based**
-TCP/IP/UDP
**Message Queue:**
- MQTT/ KAFKA/ MessageQueue


### Data Parser Configuration
Select a data parser
**XML:**
**JSON:**
**CSV:**
**Protobuf:**
