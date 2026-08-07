/***************************** BEGIN LICENSE BLOCK ***************************
 The contents of this file are subject to the Mozilla Public License, v. 2.0.
 If a copy of the MPL was not distributed with this file, You can obtain one
 at http://mozilla.org/MPL/2.0/.

 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.

 Copyright (C) 2026 GeoRobotix Innovative Research, Inc. All Rights Reserved.
 ******************************* END LICENSE BLOCK ***************************/
package com.georobotix.impl.client.nocode;


import org.sensorhub.api.client.IClientModule;
import org.sensorhub.impl.module.AbstractModule;

public class NoCodeClientModule extends AbstractModule<NoCodeClientConfig> implements IClientModule<NoCodeClientConfig> {

    @Override
    public boolean isConnected() {
        return false;
    }
}