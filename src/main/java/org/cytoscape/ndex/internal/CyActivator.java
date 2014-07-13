package org.cytoscape.ndex.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator
{

	@Override
	public void start(BundleContext context) throws Exception
	{

		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);

		MenuAction action = new MenuAction(cyApplicationManager, "Select Server");

		Properties properties = new Properties();

		registerAllServices(context, action, properties);
	}

}
