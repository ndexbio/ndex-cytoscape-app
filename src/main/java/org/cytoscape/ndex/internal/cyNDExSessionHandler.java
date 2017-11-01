package org.cytoscape.ndex.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.cytoscape.ndex.internal.server.Server;
import org.cytoscape.ndex.internal.server.ServerList;
import org.cytoscape.ndex.internal.singletons.NetworkManager;
import org.cytoscape.ndex.internal.singletons.ServerManager;
import org.cytoscape.session.events.SessionAboutToBeSavedEvent;
import org.cytoscape.session.events.SessionAboutToBeSavedListener;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.ndexbio.model.exceptions.NdexException;
import org.ndexbio.model.object.NdexStatus;
import org.ndexbio.rest.client.NdexRestClientModelAccessLayer;

public class cyNDExSessionHandler implements SessionLoadedListener, SessionAboutToBeSavedListener {

	private static final String ndexSessionFile = "cyNdexSession";
	
	private static final String appName = "ndex2";

	@Override
	public void handleEvent(SessionAboutToBeSavedEvent e) {
		System.out.println("about to save.");

		String tmpDir = System.getProperty("java.io.tmpdir");
		File networkManagerFile = new File(tmpDir, ndexSessionFile);

		try (FileOutputStream fout = new FileOutputStream(networkManagerFile)) {
			try (ObjectOutputStream oos = new ObjectOutputStream(fout)) {
				NdexSessionData sessionData = new NdexSessionData();
				sessionData.setCxNetworkInfoTable(NetworkManager.INSTANCE.getInfoTable());
				sessionData.setNetworkIdTable(NetworkManager.INSTANCE.getIdTable());
				sessionData.setSelectedServerName(ServerManager.INSTANCE.getSelectedServer() == null ? null
						: ServerManager.INSTANCE.getSelectedServer().getName());
				oos.writeObject(sessionData);
				oos.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		ArrayList<File> files = new ArrayList<>();
		files.add(networkManagerFile);
		try {
			e.addAppFiles(appName, files);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public void handleEvent(SessionLoadedEvent e) {
		System.out.println("session loaded");
		if (e.getLoadedSession().getAppFileListMap() == null || e.getLoadedSession().getAppFileListMap().size() == 0) {
			return;
		}
		List<File> files = e.getLoadedSession().getAppFileListMap().get(appName);
		for (File f : files) {
			String s = f.getName();
			if (s.equals(ndexSessionFile)) {
				try (InputStream fin = new FileInputStream(f)){
					try (ObjectInputStream ois = new ObjectInputStream(fin)) {
						NdexSessionData sessionData = (NdexSessionData) ois.readObject();
						NetworkManager.INSTANCE.setcxNetworkInfoTable(sessionData.getCxNetworkInfoTable());
						NetworkManager.INSTANCE.setNetworkIdTable(sessionData.getNetworkIdTable());
						String serverName = sessionData.getSelectedServerName();
						
						
						// set the currenselected server
						if ( serverName != null) {
							ServerList servers = ServerManager.INSTANCE.getAvailableServers();
							for ( Server server : servers.getAllServers()) {
								if ( server.getName().equals(serverName)) {
									ServerManager.INSTANCE.setSelectedServer(server);
									NdexRestClientModelAccessLayer mal = server.getModelAccessLayer();
									if( server.check(mal) ) {
										 NdexStatus status = mal.getServerStatus();
						                    String description = "Number of Networks: " + status.getNetworkCount();
						                    server.setDescription(description);
						                    ServerList availableServers = ServerManager.INSTANCE.getAvailableServers();
						                    availableServers.serverDescriptionChanged(server);
						                    availableServers.save();
									}
									break;
								}
							}
							
						}
						
					} catch (ClassNotFoundException | NdexException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} catch ( IOException e1) {
					return;
				}
			
				
				
			}
		}
	}

}
