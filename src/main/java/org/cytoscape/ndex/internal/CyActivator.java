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

import java.io.File;
import java.util.Properties;
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.event.CyEventHelper;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.ndex.internal.singletons.CyObjectManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

/**
 * This class is the main communication center between Cytoscape and this App.
 * @author David Welker
 */
public class CyActivator extends AbstractCyActivator
{

    @Override
    /**
     * This method is where everything for the NDEx Cytoscape App begins. If your App is properly installed, Cytoscape 
     * will call this method upon startup. Here you can do things like tell Cytoscape what GUI menu items that ought to 
     * be displayed to the user. It is also where your constructors that need references to Cytoscape objects 
     * controlling core Cytoscape functionality are retrieved.
     * 
     * @param context BundleContext is an OSGi type that is used to do stuff like retrieve references to objects 
     * registered with Cytoscape (under the hood, Cytoscape depends on OSGi to manage modularity) and also do things
     * like register menu items with Cytoscape. To avoid trouble, you should probably avoid low-level use of the 
     * BundleContext directly and just pass it as a parameter into methods provided by CyActivator when it is needed.
     */
    public void start(BundleContext context) throws Exception
    {
        // This application manager, like many other Cytsocape-related objects, is registered using the OSGi framework.
        // This is a common alternative to using a singleton. 
        CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);
        
        AbstractCyAction action = null;
        Properties properties = null;
              
        // Unlike what you may be expecting if you are a Java Swing developer, instead of creating your own menu items,
        // you will often want to (and need to) delegate creating such menu items to Cytoscape instead. Looking at this
        // example, you may be curious where the "Select Server" menu item will go. You can find that out by looking in
        // the SelectServerMenuAction constructor. It is a common pattern in Cytoscape to seperate the name of the 
        // menu item from where it is located and that may be initially confusing if you are expecting these things to
        // be together. Finally, notice the last line in this code group. When we call "registerAllServices" we tell
        // Cytoscape about our menu item and Cytoscape puts it in the appropriate location upon start-up. If you are 
        // wondering why registerAllServices is plural, I don't have any idea either.
        action = new SelectServerMenuAction("Select Server", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new FindNetworksMenuAction("Find Networks", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new UploadNetworkMenuAction("Upload Network", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        //Get Cytocape objects needed by this app using the bundle context.
        CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
        CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
        CyNetworkManager networkManager = getService(context, CyNetworkManager.class);
        CyNetworkViewFactory networkViewFactory = getService(context, CyNetworkViewFactory.class);
        CyNetworkViewManager networkViewManager = getService(context, CyNetworkViewManager.class);      
        CyLayoutAlgorithmManager layoutAlgorithmManager = getService(context,CyLayoutAlgorithmManager.class);
        DialogTaskManager dialogTaskManager = getService(context,DialogTaskManager.class);
        VisualMappingManager visualMappingManager = getService(context, VisualMappingManager.class);
        RenderingEngineManager renderingEngineManager = getService(context,RenderingEngineManager.class);
        CyEventHelper eventHelper = getService(context, CyEventHelper.class);
        
        //Register these with the CyObjectManager singleton.
        CyObjectManager manager = CyObjectManager.INSTANCE;
        File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
        configDir.mkdirs();
        manager.setApplicationManager(applicationManager);
        manager.setConfigDir(configDir);
        manager.setNetworkFactory(networkFactory);
        manager.setNetworkManager(networkManager);
        manager.setNetworkViewFactory(networkViewFactory);
        manager.setNetworkViewManager(networkViewManager);
        manager.setLayoutAlgorithmManager(layoutAlgorithmManager);
        manager.setDialogTaskManager(dialogTaskManager);
        manager.setVisualMappingManager(visualMappingManager);
        manager.setRenderingEngineManager(renderingEngineManager);
        manager.setEventHelper(eventHelper);
    }

}
