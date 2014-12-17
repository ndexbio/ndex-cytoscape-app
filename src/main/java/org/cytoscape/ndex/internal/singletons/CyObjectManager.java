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

package org.cytoscape.ndex.internal.singletons;

import java.io.File;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.task.create.CreateNetworkViewTaskFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.TaskManager;
import org.cytoscape.work.swing.DialogTaskManager;

import javax.swing.*;

/**
 *
 * @author David Welker
 */
public enum CyObjectManager
{
    INSTANCE;
    // App Configuration Directory where configuraton may be stored.
    private File configDir;
    // The Cytoscape helper class that allows us to easily get all sorts of Cytoscape objects.
    private CySwingAppAdapter adapter;

    public File getConfigDir()
    {
        return configDir;
    }
    public void setConfigDir(File configDir)
    {
        this.configDir = configDir;
    }
    public void setCySwingAppAdapter(CySwingAppAdapter appAdapter)
    {
        this.adapter = appAdapter;
    }

    // Trivial Getters
    public CyNetworkFactory getNetworkFactory()
    {
        return adapter.getCyNetworkFactory();
    }
    public CyNetworkManager getNetworkManager()
    {
        return adapter.getCyNetworkManager();
    }
    public CyNetworkViewFactory getNetworkViewFactory()
    {
        return adapter.getCyNetworkViewFactory();
    }
    public CyNetworkViewManager getNetworkViewManager()
    {
        return adapter.getCyNetworkViewManager();
    }
    public VisualLexicon getDefaultVisualLexicon() { return adapter.getRenderingEngineManager().getDefaultVisualLexicon(); }
    public JFrame getApplicationFrame() { return adapter.getCySwingApplication().getJFrame(); }
    public TaskManager getTaskManager() { return adapter.getDialogTaskManager(); }
    public CyLayoutAlgorithmManager getLayoutAlgorithmManager() { return adapter.getCyLayoutAlgorithmManager(); }

    // Slightly More Sophisticated Getters
    public CyNetwork getCurrentNetwork()
    {
        CyApplicationManager applicationManager = adapter.getCyApplicationManager();
        return applicationManager == null ? null : applicationManager.getCurrentNetwork();
    }

    public CyNetworkView getCurrentNetworkView()
    {
        CyApplicationManager applicationManager = adapter.getCyApplicationManager();
        return applicationManager == null ? null : applicationManager.getCurrentNetworkView();
    }

}
