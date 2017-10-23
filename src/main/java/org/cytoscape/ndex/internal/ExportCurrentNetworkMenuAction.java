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

package org.cytoscape.ndex.internal;

import java.awt.event.ActionEvent;
import java.io.IOException;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.ndex.internal.gui.ExportNetworkDialog;
import org.cytoscape.ndex.internal.gui.SignInDialog;
import org.cytoscape.ndex.internal.server.Server;
import org.cytoscape.ndex.internal.singletons.CyObjectManager;
import org.cytoscape.ndex.internal.singletons.ServerManager;
import org.ndexbio.model.exceptions.NdexException;

import javax.swing.*;

/**
 *
 * @author David Welker
 * Creates a new menu item in the Apps|NDex menu to upload an Cytoscape network to the current NDEx server.
 */
public class ExportCurrentNetworkMenuAction extends AbstractCyAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExportCurrentNetworkMenuAction(String menuTitle, CyApplicationManager applicationManager)
    {
        super(menuTitle, applicationManager, null, null);
        // We want this menu item to appear under the App|NDEx menu. The actual name of the menu item is set in
        // org.cytoscape.ndex.internal.CyActivator as "Upload Network"
        setPreferredMenu("Apps.NDEx");
    }

    @Override
    /**
     * This method displays the upload network dialog.
     * It is called when the menu item is selected.  
     */
    public void actionPerformed(ActionEvent e)
    {
        JFrame parent = CyObjectManager.INSTANCE.getApplicationFrame();

        Server currentServer = ServerManager.INSTANCE.getSelectedServer();
        if( !currentServer.isAuthenticated() )
        {
       /*     String serverName = currentServer.getName();
            String msg = "You are not authenticed on: " + serverName + "\n";
            msg += "You must be authenticated to export a network to an NDEx.";
            String dialogTitle = "Authentication Error";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return; */
        	SignInDialog signInD = new SignInDialog(parent);
        	signInD.setLocationRelativeTo(parent);
        	signInD.setVisible(true);
        }

        if (!currentServer.isAuthenticated()) {
        	return;
        }
        
        CyNetwork currentNetwork = CyObjectManager.INSTANCE.getCurrentNetwork();
        if( currentNetwork == null )
        {
            String msg = "There is no network to export.";
            String dialogTitle = "No Network Error";
            JOptionPane.showMessageDialog(parent, msg, dialogTitle, JOptionPane.ERROR_MESSAGE );
            return;
        }

        
        
        
        ExportNetworkDialog dialog;
		try {
			dialog = new ExportNetworkDialog(parent);
			 dialog.setLocationRelativeTo(parent);
		        dialog.setVisible(true);
		} catch (IOException | NdexException e1) {
            JOptionPane.showMessageDialog(parent, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );

		}
       
    }
}
