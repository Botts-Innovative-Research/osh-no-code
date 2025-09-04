package com.botts.impl.parser.csv;


import com.botts.api.parser.DataParserConfig;
import com.botts.api.parser.data.DataFeedUtils;
import com.botts.api.parser.data.DataField;
import com.botts.api.parser.AbstractDataParser;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import org.sensorhub.api.common.SensorHubException;
import org.vast.util.Asserts;

public class CSVDataParser extends AbstractDataParser {

    // TODO Add to config
    private boolean hasSkippedHeader = false;
    private CSVDataParserConfig config;

//    public CSVDataParser(CSVDataParserConfig config, DataComponent outputStructure) {
//        super(config, outputStructure);
//        this.config = config;
//    }


    @Override
    public void init(DataParserConfig config) throws SensorHubException {
        super.init(config);

        Asserts.checkArgument(config instanceof CSVDataParserConfig);
        this.config = (CSVDataParserConfig) config;
    }

    @Override
    public DataBlock parse(byte[] data) {
        DataBlock dataBlock = getRecordStructure().createDataBlock();

        String line = new String(data);
        if (!config.skipHeader) {
            hasSkippedHeader = true;
            return dataBlock;
        }
        String[] values = line.split(config.delimiter);


        for (DataField field : getInputFields()) {
            String rawValue = values[field.index].trim();
            Object realValue = DataFeedUtils.parseValue(rawValue, field.dataType);
            DataFeedUtils.setFieldData(field.index, realValue, dataBlock);
        }

        return dataBlock;
    }

    @Override
    public void start() throws SensorHubException {

    }

    @Override
    public void stop() throws SensorHubException {

    }
}
