package com.botts.impl.parser.csv;


import com.botts.api.parser.DataParserConfig;
import com.botts.api.parser.IDataParser;
import org.sensorhub.api.config.DisplayInfo;

public class CSVDataParserConfig extends DataParserConfig {

    @DisplayInfo(label="Delimiter", desc="")
    public String delimiter = ",";

    @DisplayInfo(label="Skip Header", desc="Skip the first row, which contains column headers")
    public boolean skipHeader = false;


    @Override
    public Class<? extends IDataParser> getDataParserClass() {
        return CSVDataParser.class;
    }
}
