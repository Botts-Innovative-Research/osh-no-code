# UI Forms for the No Code Client


## Configuration

Add this codeblock in the config.json, replacing the `customForms` block inside the `AdminUI` config.
```json
"customForms": [
{
"objClass": "org.sensorhub.ui.CustomUIConfig",
"configClass": "com.georobotix.ui.nocode.NoCodeEditor",
"uiClass": "com.georobotix.ui.nocode.NoCodeEditorForm"
}
],
```
