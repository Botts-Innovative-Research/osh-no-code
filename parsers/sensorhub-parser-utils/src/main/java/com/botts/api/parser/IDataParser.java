package com.botts.api.parser;

import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;

public interface IDataParser {

    DataComponent getRecordStructure();

    void setRecordStructure(DataComponent recordStructure);

    DataBlock parse(byte[] data);
}
