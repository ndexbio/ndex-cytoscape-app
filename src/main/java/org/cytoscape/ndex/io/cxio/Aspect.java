package org.cytoscape.ndex.io.cxio;

import org.cxio.aspects.datamodels.CartesianLayoutElement;
import org.cxio.aspects.datamodels.CyGroupsElement;
import org.cxio.aspects.datamodels.CyTableColumnElement;
import org.cxio.aspects.datamodels.CyViewsElement;
import org.cxio.aspects.datamodels.CyVisualPropertiesElement;
import org.cxio.aspects.datamodels.EdgeAttributesElement;
import org.cxio.aspects.datamodels.EdgesElement;
import org.cxio.aspects.datamodels.HiddenAttributesElement;
import org.cxio.aspects.datamodels.NetworkAttributesElement;
import org.cxio.aspects.datamodels.NetworkRelationsElement;
import org.cxio.aspects.datamodels.NodeAttributesElement;
import org.cxio.aspects.datamodels.NodesElement;
import org.cxio.aspects.datamodels.SubNetworkElement;

/**
 * This enumeration is used to identify aspects relevant for Cytoscape networks
 * and tables.
 *
 * @see AspectSet
 *
 */
public enum Aspect {

    NODES,
    EDGES,
    CARTESIAN_LAYOUT,
    EDGE_ATTRIBUTES,
    NODE_ATTRIBUTES,
    NETWORK_ATTRIBUTES,
    SUBNETWORKS,
    VISUAL_PROPERTIES,
    NETWORK_RELATIONS,
    GROUPS,
    VIEWS,
    HIDDEN_ATTRIBUTES,
    TABLE_COLUMN_LABELS;
/*
    NODES(NodesElement.ASPECT_NAME),
    EDGES(EdgesElement.ASPECT_NAME),
    CARTESIAN_LAYOUT(CartesianLayoutElement.ASPECT_NAME),
    EDGE_ATTRIBUTES(EdgeAttributesElement.ASPECT_NAME),
    NODE_ATTRIBUTES(NodeAttributesElement.ASPECT_NAME),
    NETWORK_ATTRIBUTES(NetworkAttributesElement.ASPECT_NAME),
    SUBNETWORKS(SubNetworkElement.ASPECT_NAME),
    VISUAL_PROPERTIES(CyVisualPropertiesElement.ASPECT_NAME),
    NETWORK_RELATIONS(NetworkRelationsElement.ASPECT_NAME),
    GROUPS(CyGroupsElement.ASPECT_NAME),
    VIEWS(CyViewsElement.ASPECT_NAME),
    HIDDEN_ATTRIBUTES(HiddenAttributesElement.ASPECT_NAME),
    TABLE_COLUMN_LABELS(CyTableColumnElement.ASPECT_NAME); */

  /*  private final String _s;

    private Aspect(final String s) {
        _s = s;
    }*/

    /**
     * This returns an aspect identifier as String. The returned String is the
     * official name of the aspect in question.
     *
     *
     * @return official name of the aspect identifier
     */
  /*  private final String asString() {
        return _s;
    }

    @Override
    public String toString() {
        return asString();
    }
*/
}
