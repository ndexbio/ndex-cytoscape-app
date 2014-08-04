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
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
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

    public CyApplicationManager getApplicationManager()
    {
        return applicationManager;
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
        return networkFactory;
    }

    public void setNetworkFactory(CyNetworkFactory networkFactory)
    {
        this.networkFactory = networkFactory;
    }
    
    public CyNetworkManager getNetworkManager()
    {
        return networkManager;
    }

    public void setNetworkManager(CyNetworkManager networkManager)
    {
        this.networkManager = networkManager;
    }
    
    public CyNetworkViewFactory getNetworkViewFactory()
    {
        return networkViewFactory;
    }

    public void setNetworkViewFactory(CyNetworkViewFactory networkViewFactory)
    {
        this.networkViewFactory = networkViewFactory;
    }

    public CyNetworkViewManager getNetworkViewManager()
    {
        return networkViewManager;
    }

    public void setNetworkViewManager(CyNetworkViewManager networkViewManager)
    {
        this.networkViewManager = networkViewManager;
    }
    
    public CyLayoutAlgorithmManager getLayoutAlgorithmManager()
    {
        return layoutAlgorithmManager;
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
        return dialogTaskManager;
    }

    public VisualMappingManager getVisualMappingManager()
    {
        return visualMappingManager;
    }

    public void setVisualMappingManager(VisualMappingManager visualMappingManager)
    {
        this.visualMappingManager = visualMappingManager;
    }

    public RenderingEngineManager getRenderingEngineManager()
    {
        return renderingEngineManager;
    }

    public void setRenderingEngineManager(RenderingEngineManager renderingEngineManager)
    {
        this.renderingEngineManager = renderingEngineManager;
    }

    public CyEventHelper getEventHelper()
    {
        return eventHelper;
    }

    public void setEventHelper(CyEventHelper eventHelper)
    {
        this.eventHelper = eventHelper;
    }
    
    

}
