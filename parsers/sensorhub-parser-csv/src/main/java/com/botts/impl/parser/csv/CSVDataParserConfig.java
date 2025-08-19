package com.botts.impl.parser.csv;


import com.botts.api.parser.DataParserConfig;
import com.botts.api.parser.IDataParser;
import org.sensorhub.api.config.DisplayInfo;

public class CSVDataParserConfig extends DataParserConfig {

    @DisplayInfo(label="Delimiter", desc="")
    public String delimiter = ",";

    @DisplayInfo(label="Skipped Header", desc="Skip the first row, which contains column headers")
    public boolean skippedHeader = false;


    @Override
    public Class<? extends IDataParser> getDataParserClass() {
        return CSVDataParser.class;
    }
}
