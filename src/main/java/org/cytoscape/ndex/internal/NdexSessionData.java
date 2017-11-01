package org.cytoscape.ndex.internal;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import org.cytoscape.ndex.internal.singletons.CXInfoHolder;

public class NdexSessionData implements Serializable {

	private static final long serialVersionUID = -6177740380666496127L;
	
	  private  Map<Long, CXInfoHolder> cxNetworkInfoTable;
	  private  Map<Long,UUID> networkIdTable; // store the network ids for collections from NDEx
	  
	  private String selectedServerName;

	public String getSelectedServerName() {
		return selectedServerName;
	}

	public void setSelectedServerName(String selectedServerName) {
		this.selectedServerName = selectedServerName;
	}

	public Map<Long,UUID> getNetworkIdTable() {
		return networkIdTable;
	}

	public void setNetworkIdTable(Map<Long,UUID> networkIdTable) {
		this.networkIdTable = networkIdTable;
	}

	public Map<Long, CXInfoHolder> getCxNetworkInfoTable() {
		return cxNetworkInfoTable;
	}

	public void setCxNetworkInfoTable(Map<Long, CXInfoHolder> cxNetworkInfoTable) {
		this.cxNetworkInfoTable = cxNetworkInfoTable;
	}

}
