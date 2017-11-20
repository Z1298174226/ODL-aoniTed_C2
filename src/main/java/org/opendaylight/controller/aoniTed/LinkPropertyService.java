/*
 * Copyright (c) 2014 Pacnet and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.aoniTed;

import java.util.Set;
import java.util.Map;
import java.util.List;
//import java.util.HashMap;
import java.io.IOException;

import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;

public interface LinkPropertyService {

    public void initTopo() throws IOException;
    public void initTopo_C1() throws IOException;

    public Map<Long, NodeConnectorRef> getNodeMapIngress();

    public int[][] getPhysicalTopoMatrix();

    public int[][] getPhysicalEdgesMatrix();

    public Map<Long, Long> getNodeMapDomain();

    public Map<Long, List<Long>> getPhysicalLinks();

    public Map<Long, List<Long>> getLogicalLinks();

    public Set<Map<Long, Long>> getPhysicalTopo();

    public Set<Map<Long, Long>> getLogicalTopo();

    public List<Long> getPhysicalNodes();

    public int[][] getPhysicalTopoMatrix(Long Domain_ID);

    public int[][] getPhysicalEdgesMatrix(Long Domain_ID);

    public Map<Long,Set<Map<Long, Long>>> getPhysicalTopoList();

    public Map<Long,Map<Long, List<Long>>> getPhysicalLinksList();


}