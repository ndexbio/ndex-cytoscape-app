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

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.cytoscape.ndex.internal.server.Server;
import org.cytoscape.ndex.internal.server.ServerList;
import org.cytoscape.ndex.internal.singletons.ServerManager;
import org.cytoscape.ndex.internal.strings.ErrorMessage;
import org.ndexbio.model.object.NdexStatus;
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer;

/**
 * @author David
 */
public class ChangeNdexServerDialog extends javax.swing.JDialog
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame parent;

    /**
     * Creates new form SelectServerDialog. This is for prototyping.
     */
    public ChangeNdexServerDialog(JFrame parent)
    {
        super(parent, true);
        this.parent = parent;
        initComponents();
        prepComponents();
    }

    /**
     * This method should be run after initComponents() in actual production.
     * The purpose is to prepare the GUI for actual data, instead of mock data.
     */
    private void prepComponents()
    {
        this.setModal(true);
        prepServerList();
        this.getRootPane().setDefaultButton(connect);
    }

    /**
     * To prepare the server list. (1) Clear any prototype servers from the
     * list. (2) Add any default servers to the list. (3) Add any previously
     * added servers to the list.
     */
    private void prepServerList()
    {
        ServerManager manager = ServerManager.INSTANCE;
        final ServerList availableServers = manager.getAvailableServers();
        guiServerList.setModel(availableServers);


        guiServerList.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int i = guiServerList.getSelectedIndex();
                assert( i != -1 );
                Server selectedServer = availableServers.get(i);
                ServerManager.INSTANCE.setSelectedServer(selectedServer);
                String header = selectedServer.getHeader();
                serverInformation.setText(header);
                delete.setEnabled(!selectedServer.isDefault());
            }
        });
        availableServers.addListDataListener( new ListDataListener() 
        {
            @Override
            public void contentsChanged(ListDataEvent e)
            {
                //When editing an existing server, reflect the changes in the
                //serverInformation text area immediately.
                String header = ServerManager.INSTANCE.getSelectedServer().getHeader();
                serverInformation.setText(header);
            }
            
            @Override
            public void intervalAdded(ListDataEvent e)
            {
                //Do nothing.
            }

            @Override
            public void intervalRemoved(ListDataEvent e)
            {
                //Do nothing.
            }

            
        });

        for( int i = 0; i < availableServers.getSize(); i++ )
        {
            if( availableServers.get(i).equals( ServerManager.INSTANCE.getSelectedServer()) )
            {
                guiServerList.setSelectedIndex(i);
                break;
            }
        }
    }
    
    public void setSelectedServer(Server server)
    {
        guiServerList.setSelectedValue(server, true);
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

        add = new javax.swing.JButton();
        copy = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        connect = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        serverInformation = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        guiServerList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        edit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select NDEx Source");

        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addActionPerformed();
            }
        });

        copy.setText("Copy");
        copy.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                copyActionPerformed();
            }
        });

        delete.setText("Delete");
        delete.setEnabled(false);
        delete.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                deleteActionPerformed();
            }
        });

        connect.setText("Connect");
        connect.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                try {
					connectActionPerformed(evt);
				} catch (HeadlessException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
	                JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

				}
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cancelActionPerformed();
            }
        });

        serverInformation.setEditable(false);
        serverInformation.setColumns(20);
        serverInformation.setRows(5);
        serverInformation.setText("Source Information\n============\nNDEx Source URL: http://www.foobar.org\nUsername: treyideker\n\n");
        jScrollPane2.setViewportView(serverInformation);

        guiServerList.setModel(new javax.swing.AbstractListModel()
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			String[] strings = { "Source 1", "Source 2", "Source 3", "Source 4", "Source 5", "Source 6", "Source 7", "Source 8", "Source 9", "Source 10" };
            @Override
			public int getSize() { return strings.length; }
            @Override
			public Object getElementAt(int i) { return strings[i]; }
        });
        guiServerList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(guiServerList);

        jLabel1.setText("NDEx Source List");

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editActionPerformed();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connect))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(copy, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane3))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(add)
                            .addComponent(copy))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(edit)
                            .addComponent(delete)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connect)
                    .addComponent(cancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void copyActionPerformed() {//GEN-FIRST:event_copyActionPerformed
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        Server copied = new Server(selectedServer);
        ServerList servers = ServerManager.INSTANCE.getAvailableServers();
        String copyName = servers.findNextAvailableName( selectedServer.getName() );
        copied.setName( copyName );
        copied.setType(Server.Type.ADDED);
        try
        {
            servers.add(copied);
            servers.save();
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Copying Source",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_copyActionPerformed

    private void connectActionPerformed(java.awt.event.ActionEvent evt) throws HeadlessException, IOException {//GEN-FIRST:event_connectActionPerformed
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        NdexRestClientModelAccessLayer mal = selectedServer.getModelAccessLayer();
        if( selectedServer.check(mal) )
        {
            if( !selectedServer.isRunningNdexServer(mal) )
            {
                JOptionPane.showMessageDialog(this, ErrorMessage.notValidNdexServer, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
                {
                    NdexStatus status = mal.getServerStatus();
                    String description = "Number of Networks: " + status.getNetworkCount();
                    selectedServer.setDescription(description);
                    ServerList availableServers = ServerManager.INSTANCE.getAvailableServers();
                    availableServers.serverDescriptionChanged(selectedServer);
                    availableServers.save();
                    // TODO Fix this description later.
                }
                catch( IOException ex )
                {
                    Logger.getLogger(ChangeNdexServerDialog.class.getName()).log(Level.SEVERE, null, ex);
                }

                String name = selectedServer.getName();
                JOptionPane.showMessageDialog(this, "Successfully connect to: " + name, "Connected", JOptionPane.INFORMATION_MESSAGE);
                this.setVisible(false);

        }
        else
        {
            // TODO Need error from server
            JOptionPane.showMessageDialog(this, ErrorMessage.failedToConnect, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_connectActionPerformed

    private void addActionPerformed() {//GEN-FIRST:event_addActionPerformed
        AddEditServerDialog dialog = new AddEditServerDialog(this);
        dialog.setMode(AddEditServerDialog.Mode.ADD);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_addActionPerformed

    private void editActionPerformed()//GEN-FIRST:event_editActionPerformed
    {//GEN-HEADEREND:event_editActionPerformed
        AddEditServerDialog dialog = new AddEditServerDialog(this);
        dialog.setMode(AddEditServerDialog.Mode.EDIT);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }//GEN-LAST:event_editActionPerformed

    private void cancelActionPerformed()//GEN-FIRST:event_cancelActionPerformed
    {//GEN-HEADEREND:event_cancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelActionPerformed

    private void deleteActionPerformed()//GEN-FIRST:event_deleteActionPerformed
    {//GEN-HEADEREND:event_deleteActionPerformed
        Server selectedServer = ServerManager.INSTANCE.getSelectedServer();
        ServerList servers = ServerManager.INSTANCE.getAvailableServers();
        int index = guiServerList.getSelectedIndex();
        if( index != 0 )
            guiServerList.setSelectedIndex(guiServerList.getSelectedIndex()-1);
        servers.delete(selectedServer);
        servers.save();
    }//GEN-LAST:event_deleteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(ChangeNdexServerDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(ChangeNdexServerDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(ChangeNdexServerDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(ChangeNdexServerDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                ChangeNdexServerDialog dialog = new ChangeNdexServerDialog(new javax.swing.JFrame());
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton cancel;
    private javax.swing.JButton connect;
    private javax.swing.JButton copy;
    private javax.swing.JButton delete;
    private javax.swing.JButton edit;
    private javax.swing.JList<Server> guiServerList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea serverInformation;
    // End of variables declaration//GEN-END:variables
}
