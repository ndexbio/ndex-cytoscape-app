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

import com.google.gson.Gson;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.*;
import org.cytoscape.ndex.internal.server.Server;
import org.cytoscape.ndex.internal.singletons.CyObjectManager;
import org.cytoscape.ndex.internal.singletons.ServerManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.ndexbio.model.object.NdexPropertyValuePair;
import org.ndexbio.model.object.SimplePropertyValuePair;
import org.ndexbio.model.object.network.PropertyGraphEdge;
import org.ndexbio.model.object.network.PropertyGraphNetwork;
import org.ndexbio.model.object.network.PropertyGraphNode;
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author David
 */
public class ExportNetworkDialog extends javax.swing.JDialog {

    /**
     * Creates new form UploadNetwork
     */
    public ExportNetworkDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        prepComponents();
    }
    
    private void prepComponents()
    {
        setModal(true);
        rootPane.setDefaultButton(upload);
        CyNetwork cyNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        String networkName = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class);
        nameField.setText(networkName);
        jLabel8.setText(String.valueOf(cyNetwork.getNodeCount()));
        jLabel9.setText(String.valueOf(cyNetwork.getEdgeCount()));
        
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        jLabel2.setText(selectedServer.getName());
        jLabel4.setText(selectedServer.getUsername());
        
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

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        upload = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Upload Network to NDEx");

        jLabel1.setText("Current NDEx Server:");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Server 3");

        jLabel3.setText("            Current User:");

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setText("Username");

        jLabel5.setText("Save Network to Server As:");

        nameField.setText("Default Network Name");

        jLabel6.setText("Nodes:");

        jLabel7.setText("Edges:");

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel8.setText("457");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel9.setText("1513");

        upload.setText("Upload Network To NDEx");
        upload.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                uploadActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameField, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8)
                                            .addComponent(jLabel9))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(upload)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(upload)
                    .addComponent(cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void uploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadActionPerformed

        CyNetwork cyNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        CyNetworkView cyNetworkView = CyObjectManager.INSTANCE.getCurrentNetworkView();
        VisualLexicon lexicon = CyObjectManager.INSTANCE.getRenderingEngineManager().getDefaultVisualLexicon();
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        final NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        
        final PropertyGraphNetwork network = new PropertyGraphNetwork();
        String networkName = nameField.getText().trim();
        
        
        // Upload Ordinary Network Properties
        List<NdexPropertyValuePair> networkProperties = network.getProperties();
        CyTable networkTable = cyNetwork.getDefaultNetworkTable();
        for( CyColumn cyColumn : networkTable.getColumns() )
        {
            String predicate = cyColumn.getName();
            if( predicate.equals("SUID") || predicate.equals("shared name") || predicate.equals("name") )
                continue;
            Class dataType = cyColumn.getType();
            
            if( dataType == List.class )
            {
                handeListType(cyColumn, cyNetwork, cyNetwork, predicate, networkProperties);          
            }
            else
            {
                handleSimpleType(cyNetwork, cyNetwork, predicate, dataType, networkProperties);
            }
        }
        //This needs to happen AFTER loading ordinary network properties.
        network.setName(networkName);
        
        
        //Set network presentation properties.
        List<SimplePropertyValuePair> networkPresentationProperties = network.getPresentationProperties();

        for(VisualProperty p : lexicon.getAllDescendants(BasicVisualLexicon.NETWORK))
        {
            if( cyNetworkView.isSet(p) && p.getTargetDataType() == CyNetwork.class )
            {
                SimplePropertyValuePair property = new SimplePropertyValuePair();
                property.setName(p.getIdString());
                Object value = cyNetworkView.getVisualProperty(p);
                property.setValue( p.toSerializableString( value ) );
                property.setType(value.getClass().getSimpleName());
                networkPresentationProperties.add(property);
            }
        }
        
        
        Map<Long, PropertyGraphNode> nodeMap = new HashMap<Long, PropertyGraphNode>();
        CyTable nodeTable = cyNetwork.getDefaultNodeTable();
        for( CyNode cyNode : cyNetwork.getNodeList() )
        {
            PropertyGraphNode node = new PropertyGraphNode();
            Long id = cyNode.getSUID();
            node.setId(id);
            
            node.setName( cyNetwork.getRow(cyNode).get(CyNetwork.NAME, String.class) );
            
            //Set Node properties
            List<NdexPropertyValuePair> properties = node.getProperties();
            for(CyColumn cyColumn : nodeTable.getColumns())
            {    
                String predicate = cyColumn.getName();
                if( predicate.equals("SUID") )
                    continue;
                Class<?> dataType = cyColumn.getType();
                if( dataType == List.class )
                {
                    handeListType(cyColumn, cyNetwork, cyNode, predicate, properties);          
                }
                else
                {
                    handleSimpleType(cyNetwork, cyNode, predicate, dataType, properties);
                }
            }
            
            //Set node presentation properties.
            List<SimplePropertyValuePair> presentationProperties = node.getPresentationProperties();
            View nodeView = cyNetworkView.getNodeView(cyNode);
            
            for(VisualProperty p : lexicon.getAllDescendants(BasicVisualLexicon.NODE))
            {
                if( nodeView.isSet(p) )
                {
                    SimplePropertyValuePair property = new SimplePropertyValuePair();
                    property.setName(p.getIdString());
                    Object value = nodeView.getVisualProperty(p);
                    property.setValue( p.toSerializableString( value ) );
                    property.setType(value.getClass().getSimpleName());
                    presentationProperties.add(property);
                }
            }
      
            nodeMap.put(id, node);
        }
        network.setNodes( nodeMap );
        
        Map<Long, PropertyGraphEdge> edges = new HashMap<Long, PropertyGraphEdge>();
        for( CyEdge cyEdge : cyNetwork.getEdgeList() )
        {
            PropertyGraphEdge edge = new PropertyGraphEdge();
            Long id = cyEdge.getSUID();
            edge.setId(id);
            edge.setSubjectId( cyEdge.getSource().getSUID() );
            edge.setPredicate( cyNetwork.getRow(cyEdge).get(CyEdge.INTERACTION, String.class));
            edge.setObjectId( cyEdge.getTarget().getSUID() );
                           
            //Set ordinary edge properties
            CyTable edgeTable = cyNetwork.getDefaultEdgeTable();
            List<NdexPropertyValuePair> properties = edge.getProperties();
            for(CyColumn cyColumn : edgeTable.getColumns())
            {    
                String predicate = cyColumn.getName();
                if( predicate.equals("SUID") )
                    continue;
                Class<?> dataType = cyColumn.getType();
                if( dataType == List.class )
                {
                    handeListType(cyColumn, cyNetwork, cyEdge, predicate, properties);          
                }
                else
                {
                    handleSimpleType(cyNetwork, cyEdge, predicate, dataType, properties);
                }
            }
            
            //Set edge presentation properties.
            List<SimplePropertyValuePair> presentationProperties = edge.getPresentationProperties();
            View edgeView = cyNetworkView.getEdgeView(cyEdge);

            for(VisualProperty p : lexicon.getAllDescendants(BasicVisualLexicon.EDGE))
            {
                if( edgeView.isSet(p) )
                {
                    SimplePropertyValuePair property = new SimplePropertyValuePair();
                    property.setName(p.getIdString());
                    Object value = edgeView.getVisualProperty(p);
                    property.setValue( p.toSerializableString( value ) );
                    property.setType(value.getClass().getSimpleName());
                    presentationProperties.add(property);
                }
            }   
            edges.put(id, edge);    
        }
        network.setEdges( edges );

        SwingWorker worker = new SwingWorker<Void,Void>()
        {

            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    mal.insertPropertyGraphNetwork(network);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
                return null;
            }
        };
        worker.execute();

        this.setVisible(false);
    }//GEN-LAST:event_uploadActionPerformed

    private void handleSimpleType(CyNetwork cyNetwork, CyIdentifiable rowId, String predicate, Class<?> dataType, List<NdexPropertyValuePair> properties)
    {
        Object value = cyNetwork.getRow(rowId).get(predicate, dataType);
        NdexPropertyValuePair property = new NdexPropertyValuePair();
        property.setPredicateString(predicate);
        String valueString = value != null ? value.toString() : null;
        property.setValue(valueString);
        property.setDataType(dataType.getSimpleName());
        properties.add(property);
    }

    private void handeListType(CyColumn cyColumn, CyNetwork cyNetwork, CyIdentifiable rowId, String predicate, List<NdexPropertyValuePair> networkProperties)
    {
        Class listElementType = cyColumn.getListElementType();
        String typeName = "List."+listElementType.getSimpleName();
        List list = cyNetwork.getRow(rowId).getList(predicate, listElementType);
        NdexPropertyValuePair property = new NdexPropertyValuePair();
        property.setPredicateString(predicate);
        Gson gson = new Gson();
        property.setValue( gson.toJson(list) );
        property.setDataType(typeName);
        networkProperties.add(property);
    }

    private void cancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelActionPerformed
    {//GEN-HEADEREND:event_cancelActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

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
            java.util.logging.Logger.getLogger(ExportNetworkDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExportNetworkDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExportNetworkDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExportNetworkDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ExportNetworkDialog dialog = new ExportNetworkDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField nameField;
    private javax.swing.JButton upload;
    // End of variables declaration//GEN-END:variables

   
}