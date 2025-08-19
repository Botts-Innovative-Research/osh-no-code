package com.botts.impl.sensor.datafeed;

import com.botts.api.parser.data.BaseDataType;
import com.botts.api.parser.data.DataField;
import com.botts.api.parser.data.FieldMapping;
import com.botts.impl.parser.protobuf.ProtobufDataParser;
import com.botts.impl.parser.protobuf.ProtobufDataParserConfig;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import org.junit.Before;
import org.junit.Test;
import org.vast.data.DataRecordImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
