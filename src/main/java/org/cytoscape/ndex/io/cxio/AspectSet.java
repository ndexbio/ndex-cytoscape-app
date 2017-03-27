package org.cytoscape.ndex.io.cxio;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.cxio.aspects.readers.CartesianLayoutFragmentReader;
import org.cxio.aspects.readers.CyGroupsFragmentReader;
import org.cxio.aspects.readers.CyTableColumnFragmentReader;
import org.cxio.aspects.readers.CyViewsFragmentReader;
import org.cxio.aspects.readers.CyVisualPropertiesFragmentReader;
import org.cxio.aspects.readers.EdgeAttributesFragmentReader;
import org.cxio.aspects.readers.EdgesFragmentReader;
import org.cxio.aspects.readers.HiddenAttributesFragmentReader;
import org.cxio.aspects.readers.NetworkAttributesFragmentReader;
import org.cxio.aspects.readers.NetworkRelationsFragmentReader;
import org.cxio.aspects.readers.NodeAttributesFragmentReader;
import org.cxio.aspects.readers.NodesFragmentReader;
import org.cxio.aspects.readers.SubNetworkFragmentReader;
import org.cxio.aspects.writers.CartesianLayoutFragmentWriter;
import org.cxio.aspects.writers.CyGroupsFragmentWriter;
import org.cxio.aspects.writers.CyTableColumnFragmentWriter;
import org.cxio.aspects.writers.CyViewsFragmentWriter;
import org.cxio.aspects.writers.EdgeAttributesFragmentWriter;
import org.cxio.aspects.writers.EdgesFragmentWriter;
import org.cxio.aspects.writers.HiddenAttributesFragmentWriter;
import org.cxio.aspects.writers.NetworkAttributesFragmentWriter;
import org.cxio.aspects.writers.NetworkRelationsFragmentWriter;
import org.cxio.aspects.writers.NodeAttributesFragmentWriter;
import org.cxio.aspects.writers.NodesFragmentWriter;
import org.cxio.aspects.writers.SubNetworkFragmentWriter;
import org.cxio.aspects.writers.VisualPropertiesFragmentWriter;
import org.cxio.core.interfaces.AspectFragmentReader;
import org.cxio.core.interfaces.AspectFragmentWriter;

/**
 * This class is primarily for storing of {@link Aspect Aspect identifiers} to
 * be imported or exported in {@link CxImporter} and {@link CxExporter}.
 *
 *
 * @see Aspect
 * @see CxImporter
 * @see CxExporter
 *
 */
public final class AspectSet {

    final private Set<Aspect> _aspects;

    /**
     * Constructor, creates an empty AspectSet.
     *
     */
    public AspectSet() {
        _aspects = new TreeSet<>();
    }

    /**
     * Constructor, creates an AspectSet containing Aspects identifiers.
     *
     * @param aspects
     *            the Aspects to initialize this AspectSet with
     */
    public AspectSet(final Collection<Aspect> aspects) {
        _aspects = new TreeSet<>();
        _aspects.addAll(aspects);
    }

    /**
     * To add a single Aspect.
     *
     * @param aspect
     *            the Aspect to add
     */
    public final void addAspect(final Aspect aspect) {
        _aspects.add(aspect);
    }

    final Set<Aspect> getAspects() {
        return _aspects;
    }

    final boolean contains(final Aspect aspect) {
        return _aspects.contains(aspect);
    }

    final Set<AspectFragmentWriter> getCySupportedAspectFragmentWriters() {
        final Set<AspectFragmentWriter> writers = new HashSet<>();
        if (_aspects.contains(Aspect.CARTESIAN_LAYOUT)) {
            writers.add(CartesianLayoutFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.EDGE_ATTRIBUTES)) {
            writers.add(EdgeAttributesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.EDGES)) {
            writers.add(EdgesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.NETWORK_ATTRIBUTES)) {
            writers.add(NetworkAttributesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.NODE_ATTRIBUTES)) {
            writers.add(NodeAttributesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.HIDDEN_ATTRIBUTES)) {
            writers.add(HiddenAttributesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.NODES)) {
            writers.add(NodesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.VISUAL_PROPERTIES)) {
            writers.add(VisualPropertiesFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.SUBNETWORKS)) {
            writers.add(SubNetworkFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.NETWORK_RELATIONS)) {
            writers.add(NetworkRelationsFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.GROUPS)) {
            writers.add(CyGroupsFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.VIEWS)) {
            writers.add(CyViewsFragmentWriter.createInstance());
        }
        if (_aspects.contains(Aspect.TABLE_COLUMN_LABELS)) {
            writers.add(CyTableColumnFragmentWriter.createInstance());
        }
        return writers;
    }

    final Set<AspectFragmentReader> getCySupportedAspectFragmentReaders() {
        final Set<AspectFragmentReader> readers = new HashSet<>();
        if (_aspects.contains(Aspect.CARTESIAN_LAYOUT)) {
            readers.add(CartesianLayoutFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.EDGE_ATTRIBUTES)) {
            readers.add(EdgeAttributesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.EDGES)) {
            readers.add(EdgesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.NETWORK_ATTRIBUTES)) {
            readers.add(NetworkAttributesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.NODE_ATTRIBUTES)) {
            readers.add(NodeAttributesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.HIDDEN_ATTRIBUTES)) {
            readers.add(HiddenAttributesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.NODES)) {
            readers.add(NodesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.VISUAL_PROPERTIES)) {
            readers.add(CyVisualPropertiesFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.SUBNETWORKS)) {
            readers.add(SubNetworkFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.GROUPS)) {
            readers.add(CyGroupsFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.NETWORK_RELATIONS)) {
            readers.add(NetworkRelationsFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.VIEWS)) {
            readers.add(CyViewsFragmentReader.createInstance());
        }
        if (_aspects.contains(Aspect.TABLE_COLUMN_LABELS)) {
            readers.add(CyTableColumnFragmentReader.createInstance());
        }
        return readers;
    }
}
