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

package org.opensha.nshmp.sha.gui.infoTools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.data.Range;
import org.opensha.commons.data.function.ArbitrarilyDiscretizedFunc;
import org.opensha.commons.gui.plot.ButtonControlPanel;
import org.opensha.commons.gui.plot.GraphPanel;
import org.opensha.commons.gui.plot.GraphWidget;
import org.opensha.commons.gui.plot.PlotSpec;
import org.opensha.commons.util.FileUtils;
import org.opensha.nshmp.util.GlobalConstants;

/**
 * <p>Title: GraphWindow</p>
 *
 * <p>Description: Thi class allows user to visualise the computed data as graphs.</p>
 * @author Ned Field,Nitin Gupta,E.V.Leyendecker
 * @version 1.0
 */
public class NSHMPGraphWindow extends JFrame {

	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu();

	JMenuItem fileExitMenu = new JMenuItem();
	JMenuItem fileSaveMenu = new JMenuItem();
	JMenuItem filePrintMenu = new JCheckBoxMenuItem();
	JToolBar jToolBar = new JToolBar();

	JButton closeButton = new JButton();
	ImageIcon closeFileImage = new ImageIcon(FileUtils.loadImage("icons/closeFile.png"));

	JButton printButton = new JButton();
	ImageIcon printFileImage = new ImageIcon(FileUtils.loadImage("icons/printFile.jpg"));

	JButton saveButton = new JButton();
	ImageIcon saveFileImage = new ImageIcon(FileUtils.loadImage("icons/saveFile.jpg"));

	private final static int W = 650;
	private final static int H = 730;
	private JSplitPane chartSplitPane = new JSplitPane();
	private JPanel chartPane = new JPanel();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel buttonPanel = new JPanel();
	private FlowLayout flowLayout1 = new FlowLayout();

	private String plotTitle = "Hazard Curves";

	//instance of the GraphPanel class
	private GraphWidget graphWidget;

	private JComboBox graphListCombo = new JComboBox();

	/**
	 * Creating this Map to keep track of the selected item to plot
	 */
	private TreeMap<String, ArrayList<ArbitrarilyDiscretizedFunc>> map = new
			TreeMap<String, ArrayList<ArbitrarilyDiscretizedFunc>>();

	/**
	 * Class constructor that shows the list of graphs that user can plot.
	 * @param dataList ArrayList List of DiscretizedFunctionList
	 */
	public NSHMPGraphWindow(ArrayList dataList) {

		//adding list of graphs to the shown to the user.
		int size = dataList.size();

		//creating the ArrayList for the plots
		for (int i = 0; i < size; ++i) {
			//getting the functions to plot and creating individual ArrayList for those
			//adding these individual arraylist to the hashmap.
			ArbitrarilyDiscretizedFunc function = (ArbitrarilyDiscretizedFunc)
			dataList.get(i);
			ArrayList plottingFunction = new ArrayList();
			plottingFunction.add(function);
			map.put(function.getName(), plottingFunction);
		}

		//adding the functions having same X and Y axis name to HashMap
		for (int i = 0; i < size; ++i) {
			ArbitrarilyDiscretizedFunc function = (ArbitrarilyDiscretizedFunc)
			dataList.get(i);
			ArrayList plottingFunctions = new ArrayList();
			String functionXName = function.getXAxisName();
			String functionYName = function.getYAxisName();
			boolean containsSameName = false;
			String name = null;
			for (int j = i + 1; j < size; ++j) {
				ArbitrarilyDiscretizedFunc function1 = (ArbitrarilyDiscretizedFunc)
				dataList.get(j);
				String function1XName = function1.getXAxisName();
				String function1YName = function1.getYAxisName();
				if (functionXName.equals(function1XName) &&
						functionYName.equals(function1YName)) {
					name = functionYName + " Vs " + functionXName;
					if (!map.containsKey(name)) {
						plottingFunctions.add(function1);
						containsSameName = true;
					}
				}
			}
			if (containsSameName) {
				plottingFunctions.add(function);
				map.put(name, plottingFunctions);
				containsSameName = false;
			}
		}

		//Adding the names of the plot to the Combo selection
		Set plotNames = map.keySet();
		Iterator it = plotNames.iterator();
		while (it.hasNext()) {
			graphListCombo.addItem(it.next());
		}

		graphListCombo.setSelectedIndex(0);

		try {
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			jbInit();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}

		graphWidget = new GraphWidget();
		chartPane.add(graphWidget, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
				, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		chartPane.validate();
		chartPane.repaint();
		drawGraph();
	}

	/**
	 * Component initialization.
	 *
	 * @throws java.lang.Exception
	 */
	private void jbInit() throws Exception {
		getContentPane().setLayout(borderLayout1);
		setSize(new Dimension(W, H));
		setTitle("Data Plot Window");
		fileMenu.setText("File");
		fileExitMenu.setText("Exit");
		fileSaveMenu.setText("Save");
		filePrintMenu.setText("Print");

		fileExitMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileExitMenu_actionPerformed(e);
			}
		});

		fileSaveMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSaveMenu_actionPerformed(e);
			}
		});

		filePrintMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filePrintMenu_actionPerformed(e);
			}
		});
		graphListCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				graphListCombo_itemStateChanged(itemEvent);
			}
		});
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				closeButton_actionPerformed(actionEvent);
			}
		});
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				printButton_actionPerformed(actionEvent);
			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				saveButton_actionPerformed(actionEvent);
			}
		});

		menuBar.add(fileMenu);
		fileMenu.add(fileSaveMenu);
		fileMenu.add(filePrintMenu);
		fileMenu.add(fileExitMenu);

		setJMenuBar(menuBar);
		closeButton.setIcon(closeFileImage);
		closeButton.setToolTipText("Close Window");
		Dimension d = closeButton.getSize();
		jToolBar.add(closeButton);
		printButton.setIcon(printFileImage);
		printButton.setToolTipText("Print Graph");
		printButton.setSize(d);
		jToolBar.add(printButton);
		saveButton.setIcon(saveFileImage);
		saveButton.setToolTipText("Save Graph as image");
		saveButton.setSize(d);
		jToolBar.add(saveButton);
		jToolBar.setFloatable(false);

		this.getContentPane().add(jToolBar, BorderLayout.NORTH);
		this.setSize(W, H);
		chartSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		chartPane.setLayout(gridBagLayout1);

		buttonPanel.setLayout(flowLayout1);
		this.getContentPane().add(chartSplitPane, BorderLayout.CENTER);
		chartSplitPane.add(chartPane, JSplitPane.TOP);
		chartSplitPane.add(buttonPanel, JSplitPane.BOTTOM);
		chartSplitPane.setDividerLocation(600);
		//object for the ButtonControl Panel
		chartPane.add(graphListCombo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(4, 4, 4, 4), 0, 0));
		Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( (dm.width - this.getSize().width) / 2,
				(dm.height - this.getSize().height) / 2);
		this.setIconImage(GlobalConstants.USGS_LOGO_ICON.getImage());
	}

	/**
	 * File | Exit action performed.
	 *
	 * @param actionEvent ActionEvent
	 */
	private void fileExitMenu_actionPerformed(ActionEvent actionEvent) {
		this.dispose();
	}

	/**
	 * File | Exit action performed.
	 *
	 * @param actionEvent ActionEvent
	 */
	private void fileSaveMenu_actionPerformed(ActionEvent actionEvent) {
		try {
			save();
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Save File Error",
					JOptionPane.OK_OPTION);
			return;
		}
	}

	/**
	 * File | Exit action performed.
	 *
	 * @param actionEvent ActionEvent
	 */
	private void filePrintMenu_actionPerformed(ActionEvent actionEvent) {
		print();
	}

	/**
	 * to draw the graph
	 */
	private void drawGraph() {

		//getting the list of the curves that we need to plot
		String selectedDataToPlot = (String) graphListCombo.getSelectedItem();

		//show correct graph titles
		plotTitle = selectedDataToPlot;

		ArrayList<ArbitrarilyDiscretizedFunc> functionsToPlot = map.get(selectedDataToPlot);
		ArbitrarilyDiscretizedFunc func = functionsToPlot.get(0);
		PlotSpec spec = graphWidget.getPlotSpec();
		spec.setXAxisLabel(func.getXAxisName());
		spec.setXAxisLabel(func.getYAxisName());
		spec.setPlotElems(functionsToPlot);
		spec.setTitle(plotTitle);
		graphWidget.drawGraph();
		graphWidget.updateUI();
	}

	/**
	 * plots the curves with defined color,line width and shape.
	 * @param plotFeatures
	 */
	public void plotGraphUsingPlotPreferences() {
		drawGraph();
	}

	/**
	 * Opens a file chooser and gives the user an opportunity to save the chart
	 * in PNG format.
	 *
	 * @throws IOException if there is an I/O error.
	 */
	public void save() throws IOException {
		graphWidget.save();
	}

	/**
	 * Creates a print job for the chart.
	 */
	public void print() {
		graphWidget.print();
	}

	/**
	 *
	 * sets plot Title
	 */
	public void setPlotLabel(String plotTitle) {
		this.plotTitle = plotTitle;
	}

	public void graphListCombo_itemStateChanged(ItemEvent itemEvent) {
		graphWidget.removeChartAndMetadata();
		drawGraph();
	}

	public void closeButton_actionPerformed(ActionEvent actionEvent) {
		this.dispose();
	}

	public void printButton_actionPerformed(ActionEvent actionEvent) {
		print();
	}

	public void saveButton_actionPerformed(ActionEvent actionEvent) {
		try {
			save();
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Save File Error",
					JOptionPane.OK_OPTION);
			return;
		}
	}
}
