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

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationConfiguration;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.CyNetworkTableManager;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;
import org.cytoscape.ndex.internal.singletons.CyObjectManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.events.SessionAboutToBeSavedListener;
import org.cytoscape.session.events.SessionLoadedListener;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * This class is the main communication center between Cytoscape and this App.
 * @author David Welker
 */
public class CyActivator extends AbstractCyActivator
{

    private static String appVersion;
    private static String cytoscapeVersion;

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
        
        Bundle[] bs = context.getBundles();
        for ( Bundle b : bs) {
        		if (b.getSymbolicName().equals("org.cytoscape.api-bundle")) {
        			cytoscapeVersion = b.getVersion().toString();
        			break;
        		}
        } 
        appVersion = context.getBundle().getVersion().toString();
              
        // Unlike what you may be expecting if you are a Java Swing developer, instead of creating your own menu items,
        // you will often want to (and need to) delegate creating such menu items to Cytoscape instead. Looking at this
        // example, you may be curious where the "Select Server" menu item will go. You can find that out by looking in
        // the SelectServerMenuAction constructor. It is a common pattern in Cytoscape to seperate the name of the 
        // menu item from where it is located and that may be initially confusing if you are expecting these things to
        // be together. Finally, notice the last line in this code group. When we call "registerAllServices" we tell
        // Cytoscape about our menu item and Cytoscape puts it in the appropriate location upon start-up. If you are 
        // wondering why registerAllServices is plural, I don't have any idea either.
        action = new ChangeNdexServerMenuAction("Sign in To/Change NDEx Server", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);

        action = new ImportNetworksMenuAction("Import Networks from NDEx", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new ExportCurrentNetworkMenuAction("Export Current Network to NDEx", applicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);

        
        //Get Cytocape objects needed by this app using the bundle context.
        CyApplicationConfiguration config = getService(context,CyApplicationConfiguration.class);
        CySwingAppAdapter appAdapter = getService(context, CySwingAppAdapter.class);
        final CyNetworkTableManager networkTableManager = getService(context, CyNetworkTableManager.class);
        
        //Register these with the CyObjectManager singleton.
        CyObjectManager manager = CyObjectManager.INSTANCE;
        File configDir = config.getAppConfigurationDirectoryLocation(CyActivator.class);
        configDir.mkdirs();
        manager.setConfigDir(configDir);
        manager.setCySwingAppAdapter(appAdapter);
        manager.setNetworkTableManager(networkTableManager);
        
        
        
        // copied from cxio-impl
        BundleContext bc = context;
    /*    final StreamUtil streamUtil = getService(bc, StreamUtil.class);
        final CyLayoutAlgorithmManager layoutManager = getService(bc, CyLayoutAlgorithmManager.class);

        final CytoscapeCxFileFilter cx_filter = new CytoscapeCxFileFilter(new String[] { "cx" },
                                                                          new String[] { "application/json" },
                                                                           "CX JSON",
                                                                          DataCategory.NETWORK,
                                                                          streamUtil);

        // Writer:
        final VisualMappingManager visual_mapping_manager = getService(bc, VisualMappingManager.class);
        final CyApplicationManager application_manager = getService(bc, CyApplicationManager.class);
        final CyNetworkViewManager networkview_manager = getService(bc, CyNetworkViewManager.class);
        final CyNetworkManager network_manager = getService(bc, CyNetworkManager.class);
        final CyGroupManager group_manager = getService(bc, CyGroupManager.class);
        final CyNetworkTableManager table_manager = getService(bc, CyNetworkTableManager.class);
        final CyNetworkViewFactory network_view_factory = getService(bc, CyNetworkViewFactory.class);

        final CxNetworkWriterFactory network_writer_factory = new CxNetworkWriterFactory(cx_filter,
                                                                                         visual_mapping_manager,
                                                                                         application_manager,
                                                                                         networkview_manager,
                                                                                         network_manager,
                                                                                         group_manager,
                                                                                         table_manager);

        final Properties cx_writer_factory_properties = new Properties();

        cx_writer_factory_properties.put(ID, "cxNetworkWriterFactory");

        registerAllServices(bc, network_writer_factory, cx_writer_factory_properties);

        // Reader:
        final CyNetworkFactory network_factory = getService(bc, CyNetworkFactory.class);
        final CyRootNetworkManager root_network_manager = getService(bc, CyRootNetworkManager.class);
        final RenderingEngineManager rendering_engine_manager = getService(bc, RenderingEngineManager.class);
        final VisualStyleFactory visual_style_factory = getService(bc, VisualStyleFactory.class);
        final CyGroupFactory group_factory = getService(bc, CyGroupFactory.class);
        final CytoscapeCxFileFilter cxfilter = new CytoscapeCxFileFilter(new String[] { "cx" },
                                                                                   new String[] { "application/json" },
                                                                                  "CX JSON",
                                                                                   DataCategory.NETWORK,
                                                                                   streamUtil);

        final VisualMappingFunctionFactory vmfFactoryC = getService(bc,
                                                                    VisualMappingFunctionFactory.class,
                                                                    "(mapping.type=continuous)");
        final VisualMappingFunctionFactory vmfFactoryD = getService(bc,
                                                                    VisualMappingFunctionFactory.class,
                                                                    "(mapping.type=discrete)");
        final VisualMappingFunctionFactory vmfFactoryP = getService(bc,
                                                                    VisualMappingFunctionFactory.class,
                                                                    "(mapping.type=passthrough)");

        final CytoscapeCxNetworkReaderFactory cx_reader_factory = new CytoscapeCxNetworkReaderFactory(cxfilter,
                                                                                                      application_manager,
                                                                                                      network_factory,
                                                                                                      network_manager,
                                                                                                      root_network_manager,
                                                                                                      visual_mapping_manager,
                                                                                                      visual_style_factory,
                                                                                                      group_factory,
                                                                                                      rendering_engine_manager,
                                                                                                      network_view_factory,
                                                                                                      vmfFactoryC,
                                                                                                      vmfFactoryD,
                                                                                                      vmfFactoryP,
                                                                                                      layoutManager

        );
        final Properties reader_factory_properties = new Properties();

        // This is the unique identifier for this reader. 3rd party developer
        // can use this service by using this ID.
        reader_factory_properties.put(ID, "cytoscapeCxNetworkReaderFactory");
        registerService(bc, cx_reader_factory, InputStreamTaskFactory.class, reader_factory_properties);
*/
        //
        registerService(bc,new NdexNetworkAboutToBeDestroyedListener(), NetworkAboutToBeDestroyedListener.class, new Properties());
     // Register the two listeners in the CyActivator class
        cyNDExSessionHandler ndexSessionHandler = new cyNDExSessionHandler();
        registerService(bc,ndexSessionHandler,SessionAboutToBeSavedListener.class, new Properties());
        registerService(bc,ndexSessionHandler,SessionLoadedListener.class, new Properties());

        
    }
    
    public static String getAppVersion() {return appVersion;}
    public static String getCyVersion() { return cytoscapeVersion;}

}
