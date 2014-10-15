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
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;

/**
 *
 * @author David Welker
 */
public enum CyObjectManager
{
    INSTANCE;
    private CyApplicationManager applicationManager;
    private File configDir;
    private CyNetworkFactory networkFactory;
    private CyNetworkManager networkManager;
    private CyNetworkViewFactory networkViewFactory;
    private CyNetworkViewManager networkViewManager;
    private CyLayoutAlgorithmManager layoutAlgorithmManager;
    private DialogTaskManager dialogTaskManager;
    private VisualMappingManager visualMappingManager;
    private RenderingEngineManager renderingEngineManager;
    private CyEventHelper eventHelper;
    private CreateNetworkViewTaskFactory createNetworkViewTaskFactory;
    private CySwingApplication swingApplication;
    private CySwingAppAdapter adapter;

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

    public void setApplicationManager(CyApplicationManager applicationManager)
    {
        this.applicationManager = applicationManager;
    }
    
    public File getConfigDir()
    {
        return configDir;
    }

    public void setConfigDir(File configDir)
    {
        this.configDir = configDir;
    }
    
    public CyNetworkFactory getNetworkFactory()
    {
        return adapter.getCyNetworkFactory();
    }

    public void setNetworkFactory(CyNetworkFactory networkFactory)
    {
        this.networkFactory = networkFactory;
    }
    
    public CyNetworkManager getNetworkManager()
    {
        return adapter.getCyNetworkManager();
    }

    public void setNetworkManager(CyNetworkManager networkManager)
    {
        this.networkManager = networkManager;
    }
    
    public CyNetworkViewFactory getNetworkViewFactory()
    {
        return adapter.getCyNetworkViewFactory();
    }

    public void setNetworkViewFactory(CyNetworkViewFactory networkViewFactory)
    {
        this.networkViewFactory = networkViewFactory;
    }

    public CyNetworkViewManager getNetworkViewManager()
    {
        return adapter.getCyNetworkViewManager();
    }

    public void setNetworkViewManager(CyNetworkViewManager networkViewManager)
    {
        this.networkViewManager = networkViewManager;
    }
    
    public CyLayoutAlgorithmManager getLayoutAlgorithmManager()
    {
        return adapter.getCyLayoutAlgorithmManager();
    }

    public void setLayoutAlgorithmManager(CyLayoutAlgorithmManager layoutAlgorithmManager)
    {
        this.layoutAlgorithmManager = layoutAlgorithmManager;
    }

    public void setDialogTaskManager(DialogTaskManager dialogTaskManager)
    {
        this.dialogTaskManager = dialogTaskManager;
    }
    
    public DialogTaskManager getDialogTaskManager()
    {
        return adapter.getDialogTaskManager();
    }

    public VisualMappingManager getVisualMappingManager()
    {
        return adapter.getVisualMappingManager();
    }

    public void setVisualMappingManager(VisualMappingManager visualMappingManager)
    {
        this.visualMappingManager = visualMappingManager;
    }

    public RenderingEngineManager getRenderingEngineManager()
    {
        return adapter.getRenderingEngineManager();
    }

    public void setRenderingEngineManager(RenderingEngineManager renderingEngineManager)
    {
        this.renderingEngineManager = renderingEngineManager;
    }

    public CyEventHelper getEventHelper()
    {
        return adapter.getCyEventHelper();
    }

    public void setEventHelper(CyEventHelper eventHelper)
    {
        this.eventHelper = eventHelper;
    }

    public CreateNetworkViewTaskFactory getCreateNetworkViewTaskFactory()
    {
        return adapter.get_CreateNetworkViewTaskFactory();
    }

    public void setCreateNetworkViewTaskFactory(CreateNetworkViewTaskFactory createNetworkViewTaskFactory)
    {
        this.createNetworkViewTaskFactory = createNetworkViewTaskFactory;
    }

    public CySwingApplication getSwingApplication()
    {
        return adapter.getCySwingApplication();
    }
    public void setCySwingApplication(CySwingApplication swingApplication)
    {
        this.swingApplication = swingApplication;
    }

    public void setCySwingAppAdapter(CySwingAppAdapter appAdapter)
    {
        this.adapter = appAdapter;
    }


}
