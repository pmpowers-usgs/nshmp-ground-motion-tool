/*******************************************************************************
 * Copyright 2009 OpenSHA.org in partnership with
 * the Southern California Earthquake Center (SCEC, http://www.scec.org)
 * at the University of Southern California and the UnitedStates Geological
 * Survey (USGS; http://www.usgs.gov)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.opensha.nshmp.sha.calc.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.opensha.nshmp.sha.calc.HazardDataCalc;
import org.opensha.nshmp.sha.calc.api.HazardDataCalcAPI;
import org.opensha.nshmp.sha.calc.remote.api.RemoteHazardDataCalcFactoryAPI;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2002</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class RemoteHazardDataCalcFactoryImpl
    extends UnicastRemoteObject implements RemoteHazardDataCalcFactoryAPI {

  public RemoteHazardDataCalcFactoryImpl() throws RemoteException {
  }

  /**
   *
   * @return org.opensha.nshmp.sha.calc.api.HazardDataCalcAPI
   * @throws RemoteException
   * @todo Implement this
   *   org.opensha.nshmp.sha.calc.remote.api.RemoteHazardDataCalcFactoryAPI method
   */
  public HazardDataCalcAPI getRemoteHazardDataCalc() throws RemoteException {
    HazardDataCalcAPI hazardDataCalc = new HazardDataCalc();
    return hazardDataCalc;
  }
}
