package org.cytoscape.ndex.internal;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedEvent;
import org.cytoscape.model.events.NetworkAboutToBeDestroyedListener;

public class NdexNetworkAboutToBeDestroyedListener implements NetworkAboutToBeDestroyedListener {

	@Override
	public void handleEvent(NetworkAboutToBeDestroyedEvent arg0) {
		CyNetwork cyNetwork = arg0.getNetwork();
		System.out.println( "Netwwork to be destroyed: " + cyNetwork.getSUID());
	}

}
