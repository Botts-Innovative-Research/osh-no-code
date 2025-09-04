package com.botts.api.parser;


import com.botts.api.parser.data.DataField;
import net.opengis.swe.v20.DataComponent;
import org.sensorhub.api.common.SensorHubException;
import org.sensorhub.impl.module.AbstractSubModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.util.Asserts;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractDataParser extends AbstractSubModule<DataParserConfig> implements IDataParser {

    private DataComponent outputStructure;
    private List<DataField> inputFields;
    private Map<String, String> fieldMap;

    public List<DataField> getInputFields() {
        return inputFields;
    }

    @Override
    public void init(DataParserConfig config) throws SensorHubException {
        super.init(config);

        // Ensure we are at least sorting by index
        this.inputFields = Asserts.checkNotNull(config.inputFields, "inputFields").stream()
                .sorted(Comparator.comparingInt(d -> d.index))
                .collect(Collectors.toList());

        this.fieldMap = config.fieldMapping.stream()
                .collect(Collectors.toMap(
                        entry -> entry.inputFieldName,
                        entry -> entry.outputFieldName
                ));
    }

//    public AbstractDataParser() {
//        Asserts.checkNotNull(config, "config");
//        Asserts.checkNotNull(config.outputStructure, "config.outputStructure");
//        this.outputStructure = Asserts.checkNotNull(outputStructure, "outputStructure");
//    }

    @Override
    public DataComponent getRecordStructure() {
        return outputStructure;
    }

    @Override
    public void setRecordStructure(DataComponent recordStructure) {
        this.outputStructure = recordStructure;
    }
}
