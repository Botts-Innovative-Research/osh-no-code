/***************************** BEGIN LICENSE BLOCK ***************************
 The contents of this file are subject to the Mozilla Public License, v. 2.0.
 If a copy of the MPL was not distributed with this file, You can obtain one
 at http://mozilla.org/MPL/2.0/.

 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.

 Copyright (C) 2020-2025 Botts Innovative Research, Inc. All Rights Reserved.
 ******************************* END LICENSE BLOCK ***************************/
package com.botts.impl.sensor.datafeed;


import com.botts.api.parser.LineBasedStreamProcessor;
import com.botts.api.parser.IDataParser;
import com.botts.api.parser.IStreamProcessor;
import com.botts.impl.sensor.datafeed.config.MsgQueueCommConfig;
import com.botts.impl.sensor.datafeed.config.StreamConfig;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import org.sensorhub.api.comm.ICommProvider;
import org.sensorhub.api.comm.IMessageQueuePush;
import org.sensorhub.api.common.SensorHubException;
import org.sensorhub.impl.sensor.AbstractSensorModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.util.Asserts;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DataFeedDriver implementation for the sensor.
 * <p>
 * This class is responsible for providing sensor information, managing output registration,
 * and performing initialization and shutdown for the driver and its outputs.
 */
public class DataFeedDriver extends AbstractSensorModule<DataFeedConfig> {
    static final String UID_PREFIX = "urn:osh:driver:datafeed:";
    static final String XML_PREFIX = "DATAFEED_DRIVER_";

    private static final Logger logger = LoggerFactory.getLogger(DataFeedDriver.class);

    DataFeedOutput output;
    AtomicBoolean doProcessing = new AtomicBoolean(false);
    ICommProvider<?> streamProvider;
    IMessageQueuePush<?> messageQueueProvider;
    IDataParser dataParser;
    IStreamProcessor dataStreamProcessor;

    @Override
    public void doInit() throws SensorHubException {
        super.doInit();

        // Generate identifiers
        generateUniqueID(UID_PREFIX, config.serialNumber);
        generateXmlID(XML_PREFIX, config.serialNumber);

        // Create and initialize output
        output = new DataFeedOutput(this);
        addOutput(output, false);
        output.init();

        Asserts.checkNotNull(config.dataParserConfig, "dataParserConfig");
        Asserts.checkArgument(config.commType != null, "Must specify stream comm settings or message queue comm settings");
    }

    @Override
    public void doStart() throws SensorHubException {
        super.doStart();

        if(config.commType instanceof StreamConfig){
            if (streamProvider == null && ((StreamConfig) config.commType).streamCommSettings!= null)
                streamProvider = (ICommProvider<?>) getParentHub().getModuleRegistry().loadSubModule(((StreamConfig) config.commType).streamCommSettings, true);
            else if (((StreamConfig) config.commType).streamCommSettings == null)
                throw new SensorHubException("Stream communication selected but no stream comm settings specified");
            streamProvider.start();
            messageQueueProvider = null;
        }else if(config.commType instanceof MsgQueueCommConfig){
            if (messageQueueProvider == null && ((MsgQueueCommConfig) config.commType).messageQueueCommSettings != null)
                messageQueueProvider = (IMessageQueuePush<?>) getParentHub().getModuleRegistry().loadSubModule(this, ((MsgQueueCommConfig) config.commType).messageQueueCommSettings, true);
            else if (((MsgQueueCommConfig) config.commType).messageQueueCommSettings == null)
                throw new SensorHubException("Message queue communication selected but no message queue comm settings specified");
            messageQueueProvider.start();
            streamProvider = null;
        }


        if(dataParser == null && config.dataParserConfig != null){
            dataParser = getParentHub().getModuleRegistry().loadSubModule(this, config.dataParserConfig, true);
        }else if(config.dataParserConfig == null){
            throw new SensorHubException("Data Parser selected but no settings were specified");
        }

        startProcessing();
    }

    @Override
    public void doStop() throws SensorHubException {
        super.doStop();
        stopProcessing();
    }

    @Override
    public boolean isConnected() {
        return doProcessing.get();
    }

    /**
     * Starts the data processing thread.
     * <p>
     * This method simulates sensor data collection and processing by generating data samples at regular intervals.
     */
    public void startProcessing() {
        doProcessing.set(true);

        if(config.commType instanceof StreamConfig)
            handleStream();
        else if(config.commType instanceof MsgQueueCommConfig)
            handleMessageQueue();

    }

    private void handleStream() {
        if (streamProvider == null) {
            reportError("Stream provider not available", null);
            return;
        }

        dataStreamProcessor = dataParser instanceof IStreamProcessor processor ? processor : new LineBasedStreamProcessor(dataParser);

        try {
            dataStreamProcessor.processStream(streamProvider.getInputStream(), dataBlock -> output.setData(dataBlock));
        } catch (IOException e) {
            reportError("Unable to process stream using default LineBasedStreamProcessor", e);
        }
    }

    private void handleMessageQueue() {
        if (messageQueueProvider == null) {
            reportError("Message queue provider not available", null);
            return;
        }

        messageQueueProvider.registerListener((attrs, payload) -> {
            DataBlock dataBlock = dataParser.parse(payload);
            output.setData(dataBlock);
        });
    }

    /**
     * Signals the processing thread to stop.
     */
    public void stopProcessing() {
        doProcessing.set(false);
        try {
            if (streamProvider != null && streamProvider.isStarted())
                streamProvider.stop();
            if (messageQueueProvider != null)
                messageQueueProvider.stop();
//            if (dataStreamProcessor != null)
//                dataStreamProcessor.stop();
        } catch (SensorHubException e) {
            reportError("Failed to stop processing", e);
        }
    }
}