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

package org.opensha.nshmp.sha.gui.api;

/**
 * <p>Title: ProbabilisticHazardApplicationAPI</p>
 *
 * <p>Description: This interface is used by all the Gui bean to communicate with
 * main application. This interface is like a Listener class that notifys the
 * application whenever data needs to be updated due to some action that has taken
 * place in the Gui bean.</p>
 * @author : Ned Field,Nitin Gupta and E.V. Lyeyendecker
 * @version 1.0
 */
public interface ProbabilisticHazardApplicationAPI {

  /**
   * Sets the information from the Gui beans in Data window
   * @param dataInfo String
   */
  public void setDataInWindow(String dataInfo);

}
