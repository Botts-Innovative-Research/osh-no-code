/***************************** BEGIN LICENSE BLOCK ***************************
 The contents of this file are subject to the Mozilla Public License, v. 2.0.
 If a copy of the MPL was not distributed with this file, You can obtain one
 at http://mozilla.org/MPL/2.0/.

 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.

 Copyright (C) 2026 GeoRobotix Innovative Research, Inc. All Rights Reserved.
 ******************************* END LICENSE BLOCK ***************************/
package com.georobotix.ui.nocode;

import org.sensorhub.api.config.DisplayInfo;

import java.util.LinkedHashMap;
import java.util.Map;

public class MappingEntry {

    @DisplayInfo(label = "Datastream Name", desc = "The source datastream to map from")
    public String datastreamName = "";

    @DisplayInfo(label = "Serializer", desc = "The output serializer (JSON, CSV, XML, Protobuf, CoT)")
    public String serializer = "JSON";

    @DisplayInfo(label = "Field Mappings", desc = "Map output field names to source datastream fields")
    public Map<String, String> fieldMappings = new LinkedHashMap<>();
}
