package com.botts.impl.sensor.datafeed.ui;

import com.botts.impl.sensor.datafeed.config.MsgQueueCommConfig;
import com.botts.impl.sensor.datafeed.config.StreamConfig;
import org.sensorhub.ui.GenericConfigForm;
import org.sensorhub.ui.data.BaseProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataFeedConfigForm extends GenericConfigForm {

    private static final String PROP_PARSER_CONFIG = "dataParserConfig";
    private static final String PROP_FIELD_MAPPING = "fieldMapping";
    private static final String PROP_INPUT_FIELDS = "inputFields";
    private static final String PROP_OUTPUT_STRUCT = "outputStructure";
    private static final String PROP_FIELDS = "fields";
    private static final String PROP_COMM_TYPE = "commType";

    @Override
    public Map<String, Class<?>> getPossibleTypes(String propId, BaseProperty<?> prop) {
        Map<String, Class<?>> classList = new LinkedHashMap<>();

        if (propId.equals(PROP_COMM_TYPE)) {
            classList.put("Stream", StreamConfig.class);
            classList.put("Message Queue", MsgQueueCommConfig.class);
        }

        if (!classList.isEmpty())
            return classList;

        return super.getPossibleTypes(propId, prop);
    }

    private Map<String, Class<?>> getDataTypeList() {
        Map<String, Class<?>> typeList = new HashMap<>();
        typeList.put("Double", Double.class);
        typeList.put("Float", Float.class);
        typeList.put("Integer", Integer.class);
        typeList.put("Long", Long.class);
        typeList.put("Byte", Byte.class);
        typeList.put("String", String.class);
        typeList.put("Boolean", Boolean.class);
        return typeList;
    }

}
