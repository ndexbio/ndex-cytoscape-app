package org.cytoscape.ndex.internal;

import java.util.Properties;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator
{

    @Override
    public void start(BundleContext context) throws Exception
    {
        CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
        
        AbstractCyAction action = null;
        Properties properties = null;
                
        action = new SelectServerMenuAction("Select Server", cyApplicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
        
        action = new FindNetworksMenuAction("Find Networks", cyApplicationManager);
        properties = new Properties();
        registerAllServices(context, action, properties);
    }

}
