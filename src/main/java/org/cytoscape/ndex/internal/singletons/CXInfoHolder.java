package org.cytoscape.ndex.internal.singletons;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.cxio.core.interfaces.AspectElement;
import org.cxio.metadata.MetaDataCollection;
import org.ndexbio.model.cx.NamespacesElement;
import org.ndexbio.model.cx.Provenance;

public class CXInfoHolder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<Long,Long> cyNode2cxNodeMapping;
	private Map<Long,Long> cyEdge2cxEdgeMapping;
	
	private MetaDataCollection metadata;
	
	private Provenance provenance;
	
	private NamespacesElement namespaces;
	
	private UUID networkId;
	
	private int subNetCount;
	
	private Map<String,Collection<AspectElement>> opaqueAspectsTable;
	
	public CXInfoHolder () {
		this.cyEdge2cxEdgeMapping = new TreeMap<>();
		this.cyNode2cxNodeMapping = new TreeMap<>();
		this.opaqueAspectsTable = new TreeMap<>();
		
	}

/*	public Map<Long,Long> getCyNode2cxNodeMapping() {
		return cyNode2cxNodeMapping;
	}

	public void setCyNode2cxNodeMapping(Map<Long,Long> cyNode2cxNodeMapping) {
		this.cyNode2cxNodeMapping = cyNode2cxNodeMapping;
	}

	public Map<Long,Long> getCyEdge2cxEdgeMapping() {
		return cyEdge2cxEdgeMapping;
	}

	public void setCyEdge2cxEdgeMapping(Map<Long,Long> cyEdge2cxEdgeMapping) {
		this.cyEdge2cxEdgeMapping = cyEdge2cxEdgeMapping;
	} */

	public Map<String,Collection<AspectElement>> getOpaqueAspectsTable() {
		return opaqueAspectsTable;
	}

	public void setOpaqueAspectsTable(Map<String,Collection<AspectElement>> opaqueAspectsTable) {
		this.opaqueAspectsTable = opaqueAspectsTable;
	} 
	
	public void addNodeMapping(Long cyNodeId, Long cxNodeId) {
		this.cyNode2cxNodeMapping.put(cyNodeId, cxNodeId);
	}

	public void addEdgeMapping (Long cyEdgeId, Long cxEdgeId) {
		this.cyEdge2cxEdgeMapping.put(cyEdgeId, cxEdgeId);
	}
	
	public Long getCXNodeId(Long cyNodeId) {
		return this.cyNode2cxNodeMapping.get(cyNodeId);
	}
	
	public Long getCXEdgeId(Long cyEdgeId) {
		return this.cyEdge2cxEdgeMapping.get(cyEdgeId);
	}
	
	public void addOpaqueAspect(String aspectName, Collection<AspectElement> elements) {
		this.opaqueAspectsTable.put(aspectName, elements);
	}

	public MetaDataCollection getMetadata() {
		return metadata;
	}

	public void setMetadata(MetaDataCollection metadata) {
		this.metadata = metadata;
	}

	public Provenance getProvenance() {
		return provenance;
	}

	public void setProvenance(Provenance provenance) {
		this.provenance = provenance;
	}

	public UUID getNetworkId() {
		return networkId;
	}

	public void setNetworkId(UUID networkId) {
		this.networkId = networkId;
	}

	public int getSubNetCount() {
		return subNetCount;
	}

	public void setSubNetCount(int subNetCount) {
		this.subNetCount = subNetCount;
	}

	public NamespacesElement getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(NamespacesElement namespaces) {
		this.namespaces = namespaces;
	}
}
