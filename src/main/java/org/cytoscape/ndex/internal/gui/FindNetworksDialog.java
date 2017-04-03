/*
 * Copyright (c) 2014, the Cytoscape Consortium and the Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.cytoscape.ndex.internal.gui;

import java.awt.Component;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.cxio.aspects.datamodels.CartesianLayoutElement;
import org.cxio.aspects.datamodels.CyGroupsElement;
import org.cxio.aspects.datamodels.CyTableColumnElement;
import org.cxio.aspects.datamodels.CyViewsElement;
import org.cxio.aspects.datamodels.CyVisualPropertiesElement;
import org.cxio.aspects.datamodels.HiddenAttributesElement;
import org.cxio.aspects.datamodels.SubNetworkElement;
import org.cxio.core.interfaces.AspectElement;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.model.subnetwork.CySubNetwork;
import org.cytoscape.ndex.internal.cx_reader.CxToCy;
import org.cytoscape.ndex.internal.cx_reader.ViewMaker;
import org.cytoscape.ndex.internal.server.Server;
import org.cytoscape.ndex.internal.singletons.CXInfoHolder;
import org.cytoscape.ndex.internal.singletons.CyObjectManager;
import org.cytoscape.ndex.internal.singletons.NetworkManager;
import org.cytoscape.ndex.internal.singletons.ServerManager;
import org.cytoscape.ndex.internal.strings.ErrorMessage;
import org.cytoscape.ndex.io.cxio.CxImporter;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.ndexbio.model.cx.NiceCXNetwork;
import org.ndexbio.model.exceptions.NdexException;
import org.ndexbio.model.object.NdexPropertyValuePair;
import org.ndexbio.model.object.network.NetworkSummary;
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer;

/**
 *
 * @author David
 */
public class FindNetworksDialog extends javax.swing.JDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<NetworkSummary> networkSummaries;
    
    /**
     * Creates new form SimpleSearch
     */
    public FindNetworksDialog(Frame parent) {
        super(parent, true);
        initComponents();
        prepComponents();
    }
    
    public void setFocusOnDone()
    {
        this.getRootPane().setDefaultButton(done);
        done.requestFocus();
    }
    
    private void prepComponents()
    {
        this.setModal(true);
        this.getRootPane().setDefaultButton(search);
        
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        serverName.setText( selectedServer.display() );
        
        if( selectedServer.isAuthenticated() )
        {
            username.setText( selectedServer.getUsername() );
            administeredByMe.setVisible(true);
        }
        else
        {
            username.setText("Not Authenticated");
            administeredByMe.setVisible(false);
        }
        
        
        NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        try {
			if( selectedServer.check(mal) )
			{
			    try
			    {
			        networkSummaries = mal.findNetworks("*", null, null, true, 0, 10000).getNetworks();
			    }
			    catch (IOException | NdexException ex)
			    {         
			        ex.printStackTrace();
			        JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);
			        this.setVisible(false);
			        return;
			    }
			    showSearchResults( ); 
			}
			else
			{
			    JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);
			    this.setVisible(false);
			}
		} catch (HeadlessException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);

		}
    }

    private void createCyNetworkFromCX(InputStream cxStream, NetworkSummary networkSummary/*, boolean stopLayout*/) throws IOException
    {

        //Create the CyNetwork to copy to.
        CyNetworkFactory networkFactory = CyObjectManager.INSTANCE.getNetworkFactory();
        CxToCy cxToCy = new CxToCy();
        CxImporter cxImporter = new CxImporter();

        NiceCXNetwork niceCX = cxImporter.getCXNetworkFromStream(cxStream);
        
        boolean doLayout = niceCX.getNodeAssociatedAspect(CartesianLayoutElement.ASPECT_NAME) == null;        
           
        List<CyNetwork> networks = cxToCy.createNetwork(niceCX, null, networkFactory, null, true);
        
        //populate the CXInfoHolder object.
        CXInfoHolder cxInfoHolder = new CXInfoHolder();
        
        for (Map.Entry<Long, CyNode> entry: cxToCy.get_cxid_to_cynode_map().entrySet()) {
        	cxInfoHolder.addNodeMapping(entry.getValue().getSUID(), entry.getKey());
        }
        
        for ( Map.Entry<Long,CyEdge> entry: cxToCy.get_cxid_to_cyedge_map().entrySet()) {
        	cxInfoHolder.addEdgeMapping(entry.getValue().getSUID(), entry.getKey());
        }
        
        cxInfoHolder.setOpaqueAspectsTable(
        		niceCX.getOpaqueAspectTable().entrySet().stream()
        		  .filter(map -> (!map.getKey().equals(SubNetworkElement.ASPECT_NAME) &&
        				          !map.getKey().equals(CyGroupsElement.ASPECT_NAME))&&
        				          !map.getKey().equals(CyViewsElement.ASPECT_NAME) &&
        				          !map.getKey().equals(CyVisualPropertiesElement.ASPECT_NAME) &&
        				          !map.getKey().equals(CartesianLayoutElement.ASPECT_NAME) && 
        				          !map.getKey().equals(CyTableColumnElement.ASPECT_NAME) &&
        				          !map.getKey().equals(HiddenAttributesElement.ASPECT_NAME)
        				          )
        		  .collect(Collectors.toMap( p -> p.getKey(), p -> p.getValue())));
        
        cxInfoHolder.setProvenance(niceCX.getProvenance());
        cxInfoHolder.setMetadata(niceCX.getMetadata());
        cxInfoHolder.setNetworkId(networkSummary.getExternalId());
        Collection<AspectElement> subNets = niceCX.getOpaqueAspectTable().get(SubNetworkElement.ASPECT_NAME);

        cxInfoHolder.setSubNetCount( subNets== null? 0 : subNets.size());
        
        for ( CyNetwork subNetwork : networks) {
        	NetworkManager.INSTANCE.setCXInfoHolder(subNetwork.getSUID(), cxInfoHolder);
        }
        
        CyRootNetwork rootNetwork = ((CySubNetwork)networks.get(0)).getRootNetwork();
        String collectionName = networkSummary.getName();
        rootNetwork.getRow(rootNetwork).set(CyNetwork.NAME, collectionName);

        //last ndex property is ndex:provenance
        CyTable networkTable = rootNetwork.getDefaultNetworkTable();
      /*  if (networkTable.getColumn("ndex:provenance") == null)
        {
            networkTable.createColumn("ndex:provenance", String.class, false);
        } */
        CyRow cyRow = rootNetwork.getRow(rootNetwork);
    //    ObjectMapper objectMapper = new ObjectMapper();
    //    JsonNode provenanceJson = objectMapper.valueToTree(provenance);
    //    cyRow.set("ndex:provenance", provenanceJson.toString());

        if (networkTable.getColumn("ndex:uuid") == null)
        {
            networkTable.createColumn("ndex:uuid", String.class, false);
        }
        cyRow.set("ndex:uuid", networkSummary.getExternalId().toString());

        if (networkTable.getColumn("ndex:modificationTime") == null)
        {
            networkTable.createColumn("ndex:modificationTime", String.class, false);
        }
        cyRow.set("ndex:modificationTime", networkSummary.getModificationTime().toString());

        if( networks.size() == 1 )
        {
            CyNetwork cyNetwork = networks.get(0);
            cyNetwork.getRow(cyNetwork).set(CyNetwork.NAME, collectionName);
        }

        for( CyNetwork cyNetwork : networks )
        {
            CyObjectManager.INSTANCE.getNetworkManager().addNetwork(cyNetwork);

            CyNetworkViewFactory nvf = CyObjectManager.INSTANCE.getNetworkViewFactory();
            RenderingEngineManager rem = CyObjectManager.INSTANCE.getRenderingEngineManager();
            VisualMappingManager vmm = CyObjectManager.INSTANCE.getVisualMappingManager();
            VisualStyleFactory vsf = CyObjectManager.INSTANCE.getVisualStyleFactory();
            VisualMappingFunctionFactory vmffc = CyObjectManager.INSTANCE.getVisualMappingFunctionContinuousFactory();
            VisualMappingFunctionFactory vmffd = CyObjectManager.INSTANCE.getVisualMappingFunctionDiscreteFactory();
            VisualMappingFunctionFactory vmffp = CyObjectManager.INSTANCE.getVisualMappingFunctionPassthroughFactory();

            Map<CyNetworkView, Boolean> cyNetworkViewMap = ViewMaker.makeView(cyNetwork, cxToCy, collectionName, nvf, rem, vmm, vsf, vmffc, vmffd, vmffp);
            CyNetworkView cyNetworkView = null;
            for ( CyNetworkView v : cyNetworkViewMap.keySet()) {
            	cyNetworkView = v;
            	break;
            }
            if( doLayout ) // && !stopLayout)
            {
                CyLayoutAlgorithmManager lam = CyObjectManager.INSTANCE.getLayoutAlgorithmManager();
                CyLayoutAlgorithm algorithm = lam.getLayout("force-directed");
                TaskIterator ti = algorithm.createTaskIterator(cyNetworkView, algorithm.getDefaultLayoutContext(), CyLayoutAlgorithm.ALL_NODE_VIEWS, "");
                TaskManager tm = CyObjectManager.INSTANCE.getTaskManager();
                tm.execute(ti);
                cyNetworkView.updateView();
            }
            vmm.getCurrentVisualStyle().apply(cyNetworkView);
            if( cyNetworkView != null )
                cyNetworkView.updateView();


            CyObjectManager.INSTANCE.getNetworkViewManager().addNetworkView(cyNetworkView);
        }
    }

    private void load(final NetworkSummary networkSummary )
    {
        // Note: In this code, references named network, node, and edge generally refer to the NDEx object model
        // while references named cyNetwork, cyNode, and cyEdge generally refer to the Cytoscape object model.

        boolean largeNetwork = false;
        
        largeNetwork = networkSummary.getEdgeCount() > 10000;

        if (largeNetwork)
        {
            JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();
            String  msg = "You have chosen to download a network that has more than 10,000 edges.\n";
            msg += "The download will occur in the background and you can continue working,\n";
            msg += "but it may take a while to appear in Cytoscape. Also, no layout will be\n";
            msg += "applied. Would you like to proceed?";
            String dialogTitle = "Proceed?";
            int choice = JOptionPane.showConfirmDialog(parent, msg, dialogTitle, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION)
                return;
        }
//        final boolean finalLargeNetwork = largeNetwork;

        final Component me = this;
       // final boolean isLargeNetwork = largeNetwork;
        SwingWorker<Integer,Integer> worker = new SwingWorker<Integer, Integer>()
        {

            @Override
            protected Integer doInBackground() throws Exception
            {

                {
                    // For entire network, we will query again, hence will check credential
                    final Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
                    final NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
                    boolean success = selectedServer.check(mal);
                    if (success)
                    {
                        //The network to copy from.
//                        NetworkSummary networkSummary = NetworkManager.INSTANCE.getSelectedNetworkSummary();
                        UUID id = networkSummary.getExternalId();
                        try
                        {
                     //       ProvenanceEntity provenance = mal.getNetworkProvenance(id.toString());
                            InputStream cxStream = mal.getNetworkAsCXStream(id.toString());
                            createCyNetworkFromCX(cxStream, networkSummary); //, finalLargeNetwork);
                       //     me.setVisible(false);
                        }
                        catch (IOException ex)
                        {
                            JOptionPane.showMessageDialog(me, ErrorMessage.failedToParseJson, "Error", JOptionPane.ERROR_MESSAGE);
                            return -1;
                        }
                    } else
                    {
                        JOptionPane.showMessageDialog(me, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return 1;
            }

        };
        worker.execute();
//        findNetworksDialog.setFocusOnDone();
//        this.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        resultsTable = new javax.swing.JTable();
        selectNetwork = new javax.swing.JButton();
        done = new javax.swing.JButton();
        search = new javax.swing.JButton();
        searchField = new javax.swing.JTextField();
        administeredByMe = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        serverName = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        hiddenLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Find Networks");
        resultsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null, null, null}
            },
            new String []
            {
                "Network Title", "Format", "Number of Nodes", "Number of Edges", "Owned By", "Last Modified"
            }
        )
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			boolean[] canEdit = new boolean []
            {
                false, false, false, false, false, true
            };

            @Override
			public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return canEdit [columnIndex];
            }
        });
        resultsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(resultsTable);

        selectNetwork.setText("Load Network");
        
        selectNetwork.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                int selectedIndex = resultsTable.getSelectedRow();
                if( selectedIndex == -1 )
                {
                    JOptionPane.showMessageDialog((Component)evt.getSource(), ErrorMessage.noNetworkSelected, "Error", JOptionPane.ERROR_MESSAGE);
                }
                NetworkSummary ns = displayedNetworkSummaries.get(selectedIndex);
            //    NetworkManager.INSTANCE.setSelectedNetworkSummary(ns);

                load(ns);
            }
        });

        done.setText("Done Loading Networks");
        done.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                setVisible(false);
            }
        });

        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                searchActionPerformed(evt);
            }
        });

        administeredByMe.setText("My Networks");
        administeredByMe.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
            	JCheckBox cb = (JCheckBox)evt.getSource();
            	searchField.setEnabled(!cb.isSelected());
            	if ( cb.isSelected()) {
            	   getMyNetworks();	
                   //administeredByMeActionPerformed();
            	} else {
            		search();
            	}
            }
        });

        jLabel1.setText("Results");

        jLabel2.setText("Current Source: ");

        serverName.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        serverName.setText("Server1");

        jLabel3.setText("Authenticated As: ");

        username.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        username.setText("Not Authenticated");

        hiddenLabel.setText(" ");

        jLabel4.setText("WARNING: In some cases, not all network information stored in NDEx will be available within Cytoscape after loading.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1066, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(done, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(administeredByMe)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hiddenLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(search))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverName))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(username)))
                        .addGap(0, 821, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectNetwork, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(serverName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(username))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(search, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(administeredByMe)
                    .addComponent(hiddenLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectNetwork)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(done)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void selectNetworkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectNetworkActionPerformed
    {//GEN-HEADEREND:event_selectNetworkActionPerformed
        int selectedIndex = resultsTable.getSelectedRow();
        if( selectedIndex == -1 )
        {
            JOptionPane.showMessageDialog(this, ErrorMessage.noNetworkSelected, "Error", JOptionPane.ERROR_MESSAGE);
        }
        NetworkSummary ns = displayedNetworkSummaries.get(selectedIndex);
    //    NetworkManager.INSTANCE.setSelectedNetworkSummary(ns);

        load(ns);
    }//GEN-LAST:event_selectNetworkActionPerformed

    
    private void getMyNetworks() 
    {
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
       
        NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        try {
			if( selectedServer.check(mal) )
			{
			    try
			    {
				        networkSummaries = mal.getMyNetworks(selectedServer.getUserId());
			    }
			    catch (IOException ex)
			    {         
			        ex.printStackTrace();
			        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			        return;
			    }
			    showSearchResults( ); 
			}
			else
			{
			    JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "ErrorY", JOptionPane.ERROR_MESSAGE);
			    this.setVisible(false);
			}
		} catch (HeadlessException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "ErrorY", JOptionPane.ERROR_MESSAGE);

		}
    }    
    
    private void search() 
    {
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();

    /*    if( administeredByMe.isSelected() )
            permissions = Permissions.READ; */
        
        String searchText = searchField.getText();
        if( searchText.isEmpty() )
            searchText = "";
        
        NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        try {
			if( selectedServer.check(mal) )
			{
			    try
			    {
			    	if ( administeredByMe.isSelected() )
				        networkSummaries = mal.getMyNetworks(selectedServer.getUserId());
			    	else 
			            networkSummaries = mal.findNetworks(searchText, null, null, true, 0, 10000).getNetworks();
			    }
			    catch (IOException |NdexException ex)
			    {         
			        ex.printStackTrace();
			        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			        return;
			    }
			    showSearchResults( ); 
			}
			else
			{
			    JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "ErrorY", JOptionPane.ERROR_MESSAGE);
			    this.setVisible(false);
			}
		} catch (HeadlessException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "ErrorY", JOptionPane.ERROR_MESSAGE);

		}
    }
    
    private void searchActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_searchActionPerformed
    {//GEN-HEADEREND:event_searchActionPerformed
        search();
        
    }
//GEN-LAST:event_searchActionPerformed

    private void administeredByMeActionPerformed()//GEN-FIRST:event_administeredByMeActionPerformed
    {//GEN-HEADEREND:event_administeredByMeActionPerformed
        getMyNetworks();
    }//GEN-LAST:event_administeredByMeActionPerformed

    private List<NetworkSummary> displayedNetworkSummaries = new ArrayList<>();
    private void showSearchResults()
    {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers( new String[]
        {
            "Network Title", "Format", "Number of Nodes", "Number of Edges", "Owned By", "Last Modified"
        });
        displayedNetworkSummaries.clear();
        for( NetworkSummary networkSummary : networkSummaries )
        {
            Vector row = new Vector();
            
            //Network Title
            row.add(networkSummary.getName());
            //Format
            row.add(getSourceFormat(networkSummary));
            //Number of Nodes
            row.add(networkSummary.getNodeCount());
            //Number of Edges
            row.add(networkSummary.getEdgeCount());
            //Owned By
            row.add(networkSummary.getOwner());
            //Last Modified
            row.add(networkSummary.getModificationTime());
               
            model.addRow(row);
            displayedNetworkSummaries.add(networkSummary);
        }
        resultsTable.setModel(model);
        resultsTable.getSelectionModel().setSelectionInterval(0, 0);
    }

    private String getSourceFormat(NetworkSummary ns)
    {
        for(NdexPropertyValuePair vp : ns.getProperties() )
        {
            if( vp.getPredicateString().equals("sourceFormat") )
                return vp.getValue();
        }
        return "Unknown";
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FindNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FindNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FindNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FindNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
			public void run() {
                FindNetworksDialog dialog = new FindNetworksDialog(new javax.swing.JFrame());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox administeredByMe;
    private javax.swing.JButton done;
    private javax.swing.JLabel hiddenLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable resultsTable;
    private javax.swing.JButton search;
    private javax.swing.JTextField searchField;
    private javax.swing.JButton selectNetwork;
    private javax.swing.JLabel serverName;
    private javax.swing.JLabel username;
    // End of variables declaration//GEN-END:variables

    
}
