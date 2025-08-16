# [NAME]

Sensor adapter for [NAME].

## Configuration

Replace this codeblock in the config.json
```java
{
"objClass": "org.sensorhub.ui.AdminUIConfig",
"widgetSet": "org.sensorhub.ui.SensorHubWidgetSet",
"bundleRepoUrls": [],
"customPanels": [],
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
"enableLandingPage": false,
"id": "5cb05c9c-9123-4fa1-8731-ffaa51489678",
"autoStart": true,
"moduleClass": "org.sensorhub.ui.AdminUIModule",
"name": "Admin UI"
}
```