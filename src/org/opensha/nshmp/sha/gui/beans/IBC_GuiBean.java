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

package org.opensha.nshmp.sha.gui.beans;

import java.util.ArrayList;

import org.opensha.commons.geo.Region;
import org.opensha.commons.param.event.ParameterChangeEvent;
import org.opensha.nshmp.exceptions.AnalysisOptionNotSupportedException;
import org.opensha.nshmp.sha.gui.api.ProbabilisticHazardApplicationAPI;
import org.opensha.nshmp.util.GlobalConstants;
import org.opensha.nshmp.util.RegionUtil;



/**
 * <p>Title: IBC_GuiBean</p>
 *
 * <p>Description: </p>
 *
 * @author Ned Field, Nitin Gupta and E.V.Leyendecker
 *
 * @version 1.0
 */
public class IBC_GuiBean
    extends NEHRP_GuiBean {

	private static final long serialVersionUID = 0x63C3AE7;
	
  public IBC_GuiBean(ProbabilisticHazardApplicationAPI api) {
    super(api);
  }

  /**
   * Creates the Parameter that allows user to select  the Editions based on the
   * selected Analysis and choosen geographic region.
   */
  protected void createEditionSelectionParameter() {

    ArrayList<String> supportedEditionList = new ArrayList<String>();

    supportedEditionList.add(GlobalConstants.IBC_2006);
    supportedEditionList.add(GlobalConstants.IBC_2004);
    supportedEditionList.add(GlobalConstants.IBC_2003);
    supportedEditionList.add(GlobalConstants.IBC_2000);
    datasetGui.createEditionSelectionParameter(supportedEditionList);
    datasetGui.getEditionSelectionParameter().addParameterChangeListener(this);
    selectedEdition = datasetGui.getSelectedDataSetEdition();
  }

  /**
   * If GuiBean parameter is changed.
   * @param event ParameterChangeEvent
   */
  public void parameterChange(ParameterChangeEvent event) {

    String paramName = event.getParameterName();

    if (paramName.equals(DataSetSelectionGuiBean.GEOGRAPHIC_REGION_SELECTION_PARAM_NAME)) {
      selectedRegion = datasetGui.getSelectedGeographicRegion();
      createEditionSelectionParameter();
    }
    super.parameterChange(event);

  }

  /**
   *
   * @return RectangularGeographicRegion
   */
  protected Region getRegionConstraint() {

    if (selectedRegion.equals(GlobalConstants.CONTER_48_STATES) ||
        selectedRegion.equals(GlobalConstants.ALASKA) ||
        selectedRegion.equals(GlobalConstants.HAWAII) ||
		  selectedEdition.equals(GlobalConstants.IBC_2006)) {

      return RegionUtil.getRegionConstraint(selectedRegion);
    }

    return null;
  }

  /**
   *
   * Creating the parameter that allows user to choose the geographic region list
   * if selected Analysis option is NEHRP.
   *
   */
  protected void createGeographicRegionSelectionParameter() throws
      AnalysisOptionNotSupportedException {

    ArrayList supportedRegionList = RegionUtil.
        getSupportedGeographicalRegions(GlobalConstants.INTL_BUILDING_CODE);
    datasetGui.createGeographicRegionSelectionParameter(supportedRegionList);
    datasetGui.getGeographicRegionSelectionParameter().
        addParameterChangeListener(this);
    selectedRegion = datasetGui.getSelectedGeographicRegion();
  }

}
