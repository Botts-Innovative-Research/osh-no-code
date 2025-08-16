# UI Forms for the No Code Driver


## Configuration

Add this codeblock in the config.json, replacing the `customForms` block inside the `AdminUI` config.
```java

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
"configClass": "com.botts.impl.parser.ProtobufDataParserConfig",
"uiClass": "com.botts.ui.ProtobufParserConfigForm"
}
],
```