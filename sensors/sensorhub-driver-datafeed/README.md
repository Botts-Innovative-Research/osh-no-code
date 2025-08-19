# DataFeed Driver

**Driver Dependencies:**
Ensure the `sensorhub-ui-datafeed` module is included. This module provides custom configuration forms for the datafeed-driver.

Update `AdminUI` in the `config.json` by adding the following under the `customForms` array:
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
There are two types of communication providers currently implemented for the data feed driver: Stream-Based and Message Queue based communication.

**Stream-Based Communication**
Used for continuous data streams
- Example: 
  - TCP
  - UDP

**Message Queue Communication:**
Used for event-driven messaging
- Example: 
  - MQTT
  - Kafka


### Data Parser Configuration
Choose a parser to interpret the incoming data format. The following parsers are currently supported:
- CSV
- JSON
- XML
- Protobuf
