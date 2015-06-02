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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.cytoscape.model.*;
import org.cytoscape.ndex.internal.server.Server;
import org.cytoscape.ndex.internal.singletons.CyObjectManager;
import org.cytoscape.ndex.internal.singletons.NetworkManager;
import org.cytoscape.ndex.internal.singletons.ServerManager;
import org.cytoscape.ndex.internal.strings.ErrorMessage;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.ndexbio.model.object.NdexPropertyValuePair;
import org.ndexbio.model.object.SimplePropertyValuePair;
import org.ndexbio.model.object.network.NetworkSummary;
import org.ndexbio.model.object.network.PropertyGraphEdge;
import org.ndexbio.model.object.network.PropertyGraphNetwork;
import org.ndexbio.model.object.network.PropertyGraphNode;
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ndexbio.model.object.ProvenanceEntity;

/**
 *
 * @author David
 */
public class ImportNetworksDialog extends javax.swing.JDialog {

    FindNetworksDialog findNetworksDialog;

    /**
     * Creates new form QueryNetwork
     */
    public ImportNetworksDialog(Frame parent) {
        super(parent, true);
        initComponents();
    }

    /**
     * This is the constructor that will normally be called.
     *
     * @param parent
     * @param modal
     */
    public ImportNetworksDialog(FindNetworksDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        prepComponents();
        this.findNetworksDialog = parent;
    }

    private String getName(PropertyGraphNode node) {
        for (NdexPropertyValuePair p : node.getProperties()) {
            if (p.getPredicateString().equals(PropertyGraphNode.name)) {
                return p.getValue();
            }
        }
        for (NdexPropertyValuePair p : node.getProperties()) {
            if (p.getPredicateString().equals(PropertyGraphNode.represents)) {
                return p.getValue();
            }
        }
        return "NONE";
    }

    private void prepComponents() {
        this.setModal(true);
        this.getRootPane().setDefaultButton(load);

        NetworkSummary networkSummary = NetworkManager.INSTANCE.getSelectedNetworkSummary();
        networkNameLabel.setText(networkSummary.getName());
        networkNameField.setText(networkSummary.getName());
        networkDetails.setText(networkSummary.getDescription());
        
        //edgePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Edges [ 25 out of " + edgeCount + " ]"));

        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        boolean success = selectedServer.check(mal);
        if (success) {
            UUID id = networkSummary.getExternalId();
            PropertyGraphNetwork network = null;
            try {
                network = mal.getPropertyGraphNetwork(id.toString(), 0, 25);
                NetworkManager.INSTANCE.setSelectedNetwork(network);
                int edgeCount = networkSummary.getEdgeCount();
                int edgesReturned = network.getEdges().size();
                edgePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Edges [ " + edgesReturned + " out of " + edgeCount + " ]"));
                updateEdgeTable(network);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ErrorMessage.failedToParseJson, "Error", JOptionPane.ERROR_MESSAGE);
                this.setVisible(false);
                return;
            }
            
        } else {
            JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);
            this.setVisible(false);
        }
    }
    
    private void updateEdgeTable(PropertyGraphNetwork network){
                    DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                "Source Node (Subject)", "Edge Type (Predicate)", "Target Node (Object)"
            });
            Map<Long, PropertyGraphNode> nodeMap = network.getNodes();
            for (Map.Entry<Long, PropertyGraphEdge> entry : network.getEdges().entrySet()) {
                PropertyGraphEdge edge = entry.getValue();
                Vector row = new Vector();

                //Source Node
                PropertyGraphNode sourceNode = nodeMap.get(edge.getSubjectId());
                row.add(getName(sourceNode));
                //Edge Type
                row.add(edge.getPredicate());
                //Target Node
                PropertyGraphNode objectNode = nodeMap.get(edge.getObjectId());
                row.add(getName(objectNode));

                model.addRow(row);
            }
            edgeJTable.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        loadNetworkRadioGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        networkDetails = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        query = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        edgePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        edgeJTable = new javax.swing.JTable();
        back = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        selectedSubnetworkRadio = new javax.swing.JRadioButton();
        entireNetworkRadio = new javax.swing.JRadioButton();
        load = new javax.swing.JButton();
        networkNameField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        networkNameLabel = new javax.swing.JLabel();
        queryComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Download Network");

        jLabel1.setText("Selected Network:");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Network Details"));

        networkDetails.setEditable(false);
        networkDetails.setColumns(20);
        networkDetails.setRows(5);
        jScrollPane2.setViewportView(networkDetails);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2))
        );

        jLabel3.setText("Query");

        query.setText("Run Query");
        query.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                queryActionPerformed(evt);
            }
        });

        jLabel4.setText("Selected Subnetwork:");

        edgePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Edges [ 25 out of 77 ]"));

        edgeJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {"FANCD2/ubiquitin", "CO_CONTROL", "CHK2"},
                {"Hus1/Rad1/Rad9/Rad17", "STATE_CHANGE", "ATM"},
                {"Hus1/Rad1/Rad9/Rad17", "STATE_CHANGE", "ATR"},
                {"Hus1/Rad1/Rad9/Rad17", "STATE_CHANGE", "TREX1"},
                {"FA Complex", "CO_CONTROL", "ATR/ATRIP/ATM"},
                {"NBS1/Rad50/Mre11", "CO_CONTROL", "FANCD2/ubiquitin"},
                {"ATR/ATRIP/ATM", "STATE_CHANGE", "CHK1"},
                {"ATR/ATRIP/ATM", "STATE_CHANGE", "RAD1"},
                {"ATR/ATRIP/ATM", "STATE_CHANGE", "HUS1"},
                {"ATR/ATRIP/ATM", "STATE_CHANGE", "NBN"}
            },
            new String []
            {
                "Source Node (Subject)", "Edge Type (Predicate)", "Target Node (Object)"
            }
        )
        {
            Class[] types = new Class []
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex)
            {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(edgeJTable);

        javax.swing.GroupLayout edgePanelLayout = new javax.swing.GroupLayout(edgePanel);
        edgePanel.setLayout(edgePanelLayout);
        edgePanelLayout.setHorizontalGroup(
            edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        edgePanelLayout.setVerticalGroup(
            edgePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, edgePanelLayout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        back.setText("Back to Search Results");
        back.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                backActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Load Network to Session"));

        loadNetworkRadioGroup.add(selectedSubnetworkRadio);
        selectedSubnetworkRadio.setText("Selected Subnetwork");
        selectedSubnetworkRadio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                selectedSubnetworkRadioActionPerformed(evt);
            }
        });

        loadNetworkRadioGroup.add(entireNetworkRadio);
        entireNetworkRadio.setSelected(true);
        entireNetworkRadio.setText("Entire Network");
        entireNetworkRadio.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                entireNetworkRadioActionPerformed(evt);
            }
        });

        load.setText("Load");
        load.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                loadActionPerformed(evt);
            }
        });

        jLabel6.setText("Name:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(load))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectedSubnetworkRadio)
                            .addComponent(entireNetworkRadio))
                        .addGap(0, 141, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(networkNameField)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(networkNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(entireNetworkRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectedSubnetworkRadio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(load))
        );

        networkNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        networkNameLabel.setText("Network 7");

        queryComboBox.setEditable(true);
        queryComboBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                queryComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edgePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(networkNameLabel))
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(query))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(back)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(networkNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(query)
                    .addComponent(queryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edgePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(back)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void queryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_queryActionPerformed
        query();
    }//GEN-LAST:event_queryActionPerformed

    private void query()
    {
        PropertyGraphNetwork network = null;
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        // First re-check credentials
        boolean success = selectedServer.check(mal);
        if (success) {
            //The network to query.
            String queryString = queryComboBox.getSelectedItem().toString();
            int depth = 1; // TODO: need to add control for depth
            NetworkSummary networkSummary = NetworkManager.INSTANCE.getSelectedNetworkSummary();
            UUID id = networkSummary.getExternalId();

            try {
                network = mal.getNeighborhoodAsPropertyGraph(id.toString(), queryString, depth);
                if( network == null || network.getNodes() == null || network.getNodes().size() == 0 )
                {
                    JOptionPane.showMessageDialog(this, ErrorMessage.noResultsFromQuery, "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                NetworkManager.INSTANCE.setSelectedNetwork(network);
                updateEdgeTable(network);
                int edgeCount = networkSummary.getEdgeCount();
                int edgesReturned = network.getEdges().size();
                edgePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Edges [ " + edgesReturned + " out of " + edgeCount + " ]"));
                if( !networkNameField.getText().endsWith(" query") )
                    networkNameField.setText(networkNameField.getText() + " query");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ErrorMessage.failedToParseJson, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);
        }
        selectedSubnetworkRadio.setSelected(true);
    }
    
    
    private boolean disableLoad = false;
    private void load()
    {
        if( disableLoad )
        {
            disableLoad = false;
            return;
        }
        // Note: In this code, references named network, node, and edge generally refer to the NDEx object model 
        // while references named cyNetwork, cyNode, and cyEdge generally refer to the Cytoscape object model. 
        final Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        final NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        
        boolean largeNetwork = false;
        if( entireNetworkRadio.isSelected() )
        {
            NetworkSummary networkSummary = NetworkManager.INSTANCE.getSelectedNetworkSummary();
            largeNetwork = networkSummary.getEdgeCount() > 10000;
        }
        else
        {
            PropertyGraphNetwork network = NetworkManager.INSTANCE.getSelectedNetwork();
            largeNetwork = network.getEdges().size() > 10000;
        }
        
        if( largeNetwork )
        {
            JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();
            String  msg = "You have chosen to download a network that has more than 10,000 edges.\n";
                   msg += "The download will occur in the background and you can continue working,\n";
                   msg += "but it may take a while to appear in Cytoscape. Would you like to proceed?";
            String dialogTitle = "Proceed?";
            int choice = JOptionPane.showConfirmDialog(parent, msg, dialogTitle, JOptionPane.YES_NO_OPTION );
            if( choice == JOptionPane.NO_OPTION )
                return;
        }
        
        final Component me = this;
        final boolean isLargeNetwork = largeNetwork;
        SwingWorker worker = new SwingWorker<Integer,Integer>()
        {

            @Override
            protected Integer doInBackground() throws Exception
            {
                PropertyGraphNetwork network = null;
                if (selectedSubnetworkRadio.isSelected()) {
                    // We already have the selected subnetwork
                    network = NetworkManager.INSTANCE.getSelectedNetwork();
                    NetworkSummary networkSummary = NetworkManager.INSTANCE.getSelectedNetworkSummary();
                    UUID id = networkSummary.getExternalId();
                    ProvenanceEntity provenance = mal.getNetworkProvenance(id.toString());
                    loadNetworkToCyNetwork(network, provenance);
                } else if (entireNetworkRadio.isSelected()) {
                    // For entire network, we will query again, hence will check credential
                    boolean success = selectedServer.check(mal);
                    if (success) {
                        //The network to copy from.
                        NetworkSummary networkSummary = NetworkManager.INSTANCE.getSelectedNetworkSummary();
                        UUID id = networkSummary.getExternalId();
                      


                        try {
                            network = mal.getPropertyGraphNetwork(id.toString());
                            ProvenanceEntity provenance = mal.getNetworkProvenance(id.toString());
                            loadNetworkToCyNetwork(network, provenance);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(me, ErrorMessage.failedToParseJson, "Error", JOptionPane.ERROR_MESSAGE);
                            return -1;
                        }
                    } else {
                        JOptionPane.showMessageDialog(me, ErrorMessage.failedServerCommunication, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                return 1;
            }
            
        };
        worker.execute();
        findNetworksDialog.setFocusOnDone();
        this.setVisible(false);
    }
    
    
    private Object convertTo(String value, String type) {
        if (value == null) {
            return null;
        }
        if (type.equalsIgnoreCase("Boolean")) {
            return value.trim().equals("true") || value.equals("1");
        } else if (type.equalsIgnoreCase("Integer")) {
            return Integer.parseInt(value);
        } else if (type.equalsIgnoreCase("Long")) {
            return Long.parseLong(value);
        } else if (type.equalsIgnoreCase("Double")) {
            return Double.parseDouble(value);
        } else if (type.equalsIgnoreCase("String")) {
            return value;
        }
        return value;
    }

    private void setData(NdexPropertyValuePair property, String cyPropertyName, CyRow cyRow) {
        String dataType = property.getDataType();
        if( dataType == null )
            dataType = "String";
        if (dataType.startsWith("List")) {
            String elementDataType = dataType.substring(dataType.indexOf(".") + 1);
            Gson gson = new Gson();
            if (elementDataType.equals("Boolean")) {
                java.lang.reflect.Type collectionType = new TypeToken<Collection<Boolean>>() {
                }.getType();
                Collection<Boolean> collection = gson.fromJson(property.getValue(), collectionType);
                List<Boolean> values = collection == null ? null : new ArrayList<Boolean>(collection);
                cyRow.set(cyPropertyName, values);
            }
            if (elementDataType.equals("Integer")) {
                java.lang.reflect.Type collectionType = new TypeToken<Collection<Integer>>() {
                }.getType();
                Collection<Integer> collection = gson.fromJson(property.getValue(), collectionType);
                List<Integer> values = collection == null ? null : new ArrayList<Integer>(collection);
                cyRow.set(cyPropertyName, values);
            }
            if (elementDataType.equals("Long")) {
                java.lang.reflect.Type collectionType = new TypeToken<Collection<Long>>() {
                }.getType();
                Collection<Long> collection = gson.fromJson(property.getValue(), collectionType);
                List<Long> values = collection == null ? null : new ArrayList<Long>(collection);
                cyRow.set(cyPropertyName, values);
            }
            if (elementDataType.equals("Double")) {
                java.lang.reflect.Type collectionType = new TypeToken<Collection<Double>>() {
                }.getType();
                Collection<Double> collection = gson.fromJson(property.getValue(), collectionType);
                List<Double> values = collection == null ? null : new ArrayList<Double>(collection);
                cyRow.set(cyPropertyName, values);
            }
            if (elementDataType.equals("String")) {
                java.lang.reflect.Type collectionType = new TypeToken<Collection<String>>() {
                }.getType();
                Collection<String> collection = gson.fromJson(property.getValue(), collectionType);
                List<String> values = collection == null ? null : new ArrayList<String>(collection);
                cyRow.set(cyPropertyName, values);
            }
        } else {
            Object converted = convertTo(property.getValue(), dataType);
            if (converted instanceof Boolean) {
                cyRow.set(cyPropertyName, (Boolean) converted);
            } else if (converted instanceof Integer) {
                cyRow.set(cyPropertyName, (Integer) converted);
            } else if (converted instanceof Long) {
                cyRow.set(cyPropertyName, (Long) converted);
            } else if (converted instanceof Double) {
                cyRow.set(cyPropertyName, (Double) converted);
            } else if (converted instanceof String) {
                cyRow.set(cyPropertyName, (String) converted);
            }
        }
    }

    
    

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
         load();
    }

    private void loadNetworkToCyNetwork(PropertyGraphNetwork network, ProvenanceEntity provenance) {

        //Create the CyNetwork to copy to.
        CyNetworkFactory networkFactory = CyObjectManager.INSTANCE.getNetworkFactory();
        CyNetwork cyNetwork = networkFactory.createNetwork();
        String networkName = networkNameField.getText();
        cyNetwork.getRow(cyNetwork).set(CyNetwork.NAME, networkName );

        //Copy network properties
        CyTable networkTable = cyNetwork.getDefaultNetworkTable();
        List<NdexPropertyValuePair> networkProperties = network.getProperties();
        for (NdexPropertyValuePair property : networkProperties) {
            String cyPropertyName = property.getPredicateString();
            if( cyPropertyName.equalsIgnoreCase("dc:title") )
                continue;
            if (networkTable.getColumn(cyPropertyName) == null) {
                Class type = String.class;
                Class listElementType = null;
                try {
                    String ndexDataType = property.getDataType();
                    if (ndexDataType.startsWith("List")) {
                        type = List.class;
                        String elementTypeString = ndexDataType.substring(ndexDataType.indexOf(".") + 1);
                        listElementType = Class.forName("java.lang." + elementTypeString);
                    } else {
                        type = Class.forName("java.lang." + ndexDataType);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ImportNetworksDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (type == List.class) {
                    networkTable.createListColumn(cyPropertyName, listElementType, false);
                } else {
                    networkTable.createColumn(cyPropertyName, type, false);
                }
            }
            CyRow cyRow = cyNetwork.getRow(cyNetwork);
            setData(property, cyPropertyName, cyRow);
        }
        //last ndex property is ndex:provenance
        networkTable.createColumn("NDEX:provenance", String.class, false);
        CyRow cyRow = cyNetwork.getRow(cyNetwork);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode provenanceJson = objectMapper.valueToTree(provenance);
        cyRow.set("NDEX:provenance", provenanceJson.toString());

        //Copy nodes   
        //Create a map to keep track of the new CyNodes we create.
        Map<Long, CyNode> nodeMap = new HashMap<Long, CyNode>();
        for (PropertyGraphNode node : network.getNodes().values()) {
            //Create a new node and save a reference in the nodeMap for a little later.
            CyNode cyNode = cyNetwork.addNode();
            nodeMap.put(node.getId(), cyNode);
            List<NdexPropertyValuePair> nodeProperties = node.getProperties();
            CyTable nodeTable = cyNetwork.getDefaultNodeTable();
            readNdexProperties(nodeProperties, nodeTable, cyNetwork, cyNode);
        }

        //Copy edges           
        //Create a map to keep track of the new CyEdges we create.
        Map<Long, CyEdge> edgeMap = new HashMap<Long, CyEdge>();
        for (Map.Entry<Long, PropertyGraphEdge> entry : network.getEdges().entrySet()) {
            PropertyGraphEdge e = entry.getValue();
            PropertyGraphNode s = network.getNodes().get(e.getSubjectId());
            CyNode sourceNode = nodeMap.get(s.getId());

            PropertyGraphNode t = network.getNodes().get(e.getObjectId());
            CyNode targetNode = nodeMap.get(t.getId());

            CyEdge cyEdge = cyNetwork.addEdge(sourceNode, targetNode, true);
            edgeMap.put(e.getId(), cyEdge);
            cyNetwork.getRow(cyEdge).set(CyEdge.INTERACTION, e.getPredicate());

            String edgeName = getName(s) + " (" + e.getPredicate() + ") " + getName(t);
            cyNetwork.getRow(cyEdge).set(CyNetwork.NAME, edgeName);

            List<NdexPropertyValuePair> edgeProperties = e.getProperties();
            CyTable edgeTable = cyNetwork.getDefaultEdgeTable();
            readNdexProperties(edgeProperties, edgeTable, cyNetwork, cyEdge);
        }

        //Create a view for the network
        CyNetworkView cyNetworkView = CyObjectManager.INSTANCE.getNetworkViewFactory().createNetworkView(cyNetwork);

//        VisualLexicon lexicon = CyObjectManager.INSTANCE.getDefaultVisualLexicon();
//
//        // Copy presentation properties for the network.
//        List<SimplePropertyValuePair> networkPresentationProperties = network.getPresentationProperties();
//        copyPresentationProperties(CyNetwork.class, networkPresentationProperties, lexicon, cyNetworkView);
//
//        for (PropertyGraphNode node : network.getNodes().values()) {
//            List<SimplePropertyValuePair> properties = node.getPresentationProperties();
//
//            CyNode cyNode = nodeMap.get(node.getId());
//            View cyNodeView = cyNetworkView.getNodeView(cyNode);
//
//            copyPresentationProperties(CyNode.class, properties, lexicon, cyNodeView);
//        }
//
//        for (Map.Entry<Long, PropertyGraphEdge> entry : network.getEdges().entrySet()) {
//            PropertyGraphEdge edge = entry.getValue();
//            List<SimplePropertyValuePair> properties = edge.getPresentationProperties();
//
//            CyEdge cyEdge = edgeMap.get(edge.getId());
//            View cyEdgeView = cyNetworkView.getEdgeView(cyEdge);
//
//            copyPresentationProperties(CyEdge.class, properties, lexicon, cyEdgeView);
//        }

        CyLayoutAlgorithmManager lam = CyObjectManager.INSTANCE.getLayoutAlgorithmManager();
        CyLayoutAlgorithm algorithm = lam.getLayout("force-directed");
        TaskIterator ti = algorithm.createTaskIterator(cyNetworkView, algorithm.getDefaultLayoutContext(), CyLayoutAlgorithm.ALL_NODE_VIEWS, "");
        TaskManager tm = CyObjectManager.INSTANCE.getTaskManager();
        tm.execute(ti);

        cyNetworkView.updateView();
        //Register the new network and view with the appropriate managers.
        CyObjectManager.INSTANCE.getNetworkManager().addNetwork(cyNetwork);
        CyObjectManager.INSTANCE.getNetworkViewManager().addNetworkView(cyNetworkView);

    }//GEN-LAST:event_loadActionPerformed

    private void readNdexProperties(List<NdexPropertyValuePair> ndexProperties, CyTable cyTable, CyNetwork cyNetwork, CyIdentifiable rowId) {
        for (NdexPropertyValuePair property : ndexProperties) {
            String cyPropertyName = property.getPredicateString();
            if (cyPropertyName.equalsIgnoreCase("dc:title")) {
                CyRow cyRow = cyNetwork.getRow(rowId);
                cyRow.set("name", property.getValue());
                continue;
            }
            if (cyTable.getColumn(property.getPredicateString()) == null) {
                Class type = String.class;
                Class listElementType = null;
                try {
                    String ndexDataType = property.getDataType();
                    if( ndexDataType == null )
                        type = String.class;
                    else if (ndexDataType.startsWith("List")) {
                        type = List.class;
                        String elementTypeString = ndexDataType.substring(ndexDataType.indexOf(".") + 1);
                        listElementType = Class.forName("java.lang." + elementTypeString);
                    } else {
                        ndexDataType = ndexDataType.toLowerCase();
                        ndexDataType = ndexDataType.substring(0,1).toUpperCase() + ndexDataType.substring(1);
                        type = Class.forName("java.lang." + ndexDataType);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ImportNetworksDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (type == List.class) {
                    cyTable.createListColumn(property.getPredicateString(), listElementType, false);
                } else {
                    cyTable.createColumn(property.getPredicateString(), type, false);
                }
            }
            CyRow cyRow = cyNetwork.getRow(rowId);
            // This is a temporary hack, need to check the mapping of node names / labels...

            setData(property, cyPropertyName, cyRow);
        }
    }

    private void copyPresentationProperties(Class type, List<SimplePropertyValuePair> properties, VisualLexicon lexicon, View view)
    {
        for (SimplePropertyValuePair property : properties)
        {
             String name = property.getName();
            VisualProperty vp = lexicon.lookup(type, name);
            if( vp == null || property.getValue() == null ) 
                continue;
            Object value = null;
            try
            {
                value = vp.parseSerializableString(property.getValue());
            }
            catch( Throwable t)
            {
                t.printStackTrace();
            }
            if( value == null )
                continue;
            //The exceptions must be set as visual properties rather than bypasses, otherwise zooming isn't possible and
            //nodes end up in fixed locations. Are there other properties that should be treated like this?
            Set<String> dontLock = new HashSet<>( 
                    Arrays.asList("NODE_X_LOCATION","NODE_Y_LOCATION",
                            "NETWORK_SCALE_FACTOR","NETWORK_CENTER_X_LOCATION",
                            "NETWORK_CENTER_Y_LOCATION") );
                       
            if( dontLock.contains(name) )
                view.setVisualProperty(vp, value);
            else
                view.setLockedValue(vp, value);
        }
    }




    private void selectedSubnetworkRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedSubnetworkRadioActionPerformed
        if( !networkNameField.getText().endsWith(" query") )
            networkNameField.setText(networkNameField.getText() + " query");
    }//GEN-LAST:event_selectedSubnetworkRadioActionPerformed

    private void entireNetworkRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entireNetworkRadioActionPerformed
        String name = networkNameField.getText();
        if( name.endsWith(" query") )
            networkNameField.setText( name.substring(0, name.length() - " query".length()) );
    }//GEN-LAST:event_entireNetworkRadioActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backActionPerformed
    {//GEN-HEADEREND:event_backActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_backActionPerformed

    String lastQuery = "";
    
    private boolean alreadyQueried()
    {
        String thisQuery = queryComboBox.getSelectedItem().toString();
        boolean result = thisQuery.trim().equals(lastQuery.trim());
        lastQuery = thisQuery;
        return result;
    }
    
    private void queryComboBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_queryComboBoxActionPerformed
    {//GEN-HEADEREND:event_queryComboBoxActionPerformed
        if( alreadyQueried() )
            return;
        query();
        disableLoad = true;
    }//GEN-LAST:event_queryComboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(ImportNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ImportNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ImportNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ImportNetworksDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImportNetworksDialog dialog = new ImportNetworksDialog(new javax.swing.JFrame());
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
    private javax.swing.JButton back;
    private javax.swing.JTable edgeJTable;
    private javax.swing.JPanel edgePanel;
    private javax.swing.JRadioButton entireNetworkRadio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton load;
    private javax.swing.ButtonGroup loadNetworkRadioGroup;
    private javax.swing.JTextArea networkDetails;
    private javax.swing.JTextField networkNameField;
    private javax.swing.JLabel networkNameLabel;
    private javax.swing.JButton query;
    private javax.swing.JComboBox queryComboBox;
    private javax.swing.JRadioButton selectedSubnetworkRadio;
    // End of variables declaration//GEN-END:variables
}
