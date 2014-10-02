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

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.ndex.internal.gui.ChangeNdexServerDialog;


/**
 * @author David Welker
 * Creates a new menu item in the Apps|NDex menu to select an NDEx server.
 */
public class ChangeNdexServerMenuAction extends AbstractCyAction
{
    public ChangeNdexServerMenuAction(final String menuTitle, CyApplicationManager cyApplicationManager)
    {
        super(menuTitle, cyApplicationManager, null, null);
        // We want this menu item to appear under the App|NDEx menu. The actual name of the menu item is set in
        // org.cytoscape.ndex.internal.CyActivator as "Select Server"
        setPreferredMenu("Apps.NDEx");
    }

    /**
     * This method displays the select server dialog.
     * It is called when the menu item is selected.  
     */
    public void actionPerformed(ActionEvent e)
    {
        ChangeNdexServerDialog dialog = new ChangeNdexServerDialog(null, true);
        dialog.setVisible(true);
    }
}
