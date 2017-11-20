/*
 * Copyright (c) 2014 Pacnet and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.aoniTed;


import org.opendaylight.controller.aoniVnm.PacketResource;
import org.opendaylight.controller.aoniVnm.PacketTopo;

import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.toporeply.rev160926.PacketTopoReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*file*/

public class LinkProperty implements LinkPropertyService {
    private static final Logger LOG = LoggerFactory.getLogger(LinkProperty.class);

    public static Map<Long, List<Long>> physicalLinks = new HashMap<Long, List<Long>>();
    public static Map<Long, List<Long>> logicalLinks = new LinkedHashMap<Long, List<Long>>();
    public static Set<Map<Long, Long>> physicalTopo = new HashSet<Map<Long, Long>>();//not all links between vertexs
    public static Set<Map<Long, Long>> logicalTopo = new HashSet<Map<Long, Long>>();
    public static List<Long> physicalNodes = new ArrayList<>();//Vertex in phyTopo
    public static Map<Long, Long> nodeMapDomain = new HashMap<Long, Long>();
    public static Map<Long, NodeConnectorRef> nodeMapIngress = new HashMap<Long, NodeConnectorRef>();
    public static int nodeCounter = 0;

    public static HashMap<Long, PacketTopo> VirTopoList = new HashMap<Long, PacketTopo>();
    public static HashMap<Long, PacketResource> VirResourceList = new HashMap<Long, PacketResource>();
    public static HashMap<Long, PacketTopo> PhyTopoList = new HashMap<Long, PacketTopo>();
    //  public static HashMap<Long, PacketResource> PhyResourceList = new HashMap<Long, PacketResource>();
    public int row = 200;
    public int column = 200;
    public int[][] topo = new int[row][column];
    public int[][] edges = new int[row][column];
    //new for cascade
    public Map<Long, int[][]> topoList = new HashMap<Long, int[][]>();//new for cascade
    public Map<Long, int[][]> edgesList = new HashMap<Long, int[][]>();//new for cascade
    public static Map<Long, Map<Long, List<Long>>> physicalLinksList = new HashMap<Long, Map<Long, List<Long>>>();//new for cascade
    public static Map<Long, Set<Map<Long, Long>>> physicalTopoList = new HashMap<Long, Set<Map<Long, Long>>>();//new for cascade

    public LinkProperty() {
    }

    public void initTopo() throws IOException {
        /*
        * Ckear the previous record
        * */

/*         nodeCounter = 0;
        physicalNodes.clear();
        nodeMapDomain.clear();
        physicalLinks.clear();
        physicalTopo.clear();
        */

 /*
* Read file to get the topo and edge mareix
* */
        File edge_file = new File("/root/copy/ReadFile/src/chapter1/topo_1000_edge_f.txt");
        BufferedReader buf = new BufferedReader(new FileReader(edge_file));
        for (int i = 0; i < row; i++) {
            String edge_line = buf.readLine();
            LOG.info("{} ", edge_line);
            String[] edge_line_split = edge_line.split(" ");
            for (int j = 0; j < column; j++) {
                edges[i][j] = Integer.parseInt(edge_line_split[j]);
                //      LOG.info("{} ",topo[i][j]);
                //       if (edges[i][j] > 0 && edges[i][j] < Integer.MAX_VALUE)//Dij
                if (edges[i][j] > 0 && edges[i][j] < 99999999)//Floyd
                    topo[i][j] = 1;
                else
                    topo[i][j] = 0;
            }
            // System.out.println("\n");
        }
        buf.close();
        /*
        * Get the topo information from matrix
        * */
        Map<Long, Long> addedLinks = new HashMap<Long, Long>();
        int link_num = 0;
        for (int i = 0; i < row; i++) {
            nodeCounter++;
            physicalNodes.add(new Long((long) (i + 1)));
            List<Long> setphysicalLink = new ArrayList<Long>();
            //  for (int j = column - i - 1 ; j < column; j++) {
            for (int j = 0; j < column; j++) {

                if (topo[i][j] == 1) {
                    link_num++;
                    Long node = new Long((long) (i + 1));
                    Long remoteNode = new Long((long) (j + 1));
                    Long nodeDomain = 0x0L;
                    Long remoteNodeDomain = 0x0L;
                    if (i >= 0 && i <= 199) {
                        nodeDomain = 0x1L;
                    } else if (i >= 200 && i <= 399) {
                        nodeDomain = 0x2L;
                    } else if (i >= 400 && i <= 599) {
                        nodeDomain = 0x3L;
                    } else if (i >= 600 && i <= 799) {
                        nodeDomain = 0x4L;
                    } else if (i >= 800 && i <= 999) {
                        nodeDomain = 0x5L;
                    }
                    if (j >= 0 && j <= 199) {
                        remoteNodeDomain = 0x1L;
                    } else if (j >= 200 && j <= 399) {
                        remoteNodeDomain = 0x2L;
                    } else if (j >= 400 && j <= 599) {
                        remoteNodeDomain = 0x3L;
                    } else if (j >= 600 && j <= 799) {
                        remoteNodeDomain = 0x4L;
                    } else if (j >= 800 && j <= 999) {
                        remoteNodeDomain = 0x5L;
                    } else
                        LOG.info("The nodeID ={} remodeID = {} is out of plan.", node, remoteNode);
                    /*
                    NodeProperty headNode = new NodeProperty(nodeDomain, node);
                    NodeProperty tailNode = new NodeProperty(remoteNodeDomain, remoteNode);
                    //  LOG.info("nodeID={}--domainID={},nodeID={}--domainID={}",node,nodeDomain,remoteNode,remoteNodeDomain);
                    LOG.info("[{},{}],[{},{}]", node, nodeDomain, remoteNode, remoteNodeDomain);
                    EdgeProperty edge = new EdgeProperty(headNode, tailNode);
                    topo.add(edge);
*/
                    addedLinks.put(node, remoteNode);
                    LOG.info("addedlinks are :{}", addedLinks);
                    nodeMapDomain.put(node, nodeDomain);
                    nodeMapDomain.put(remoteNode, remoteNodeDomain);
                    setphysicalLink.add(remoteNode);
                }
            }
            physicalLinks.put((long) (i + 1), setphysicalLink);
            LOG.info("physicalLinks:{}", physicalLinks);
        }
        LOG.info("The total link_num is :{}", link_num);
        physicalTopo.add(addedLinks);
        //  linkProperty.getPhysicalLinks();

    }

    @SuppressWarnings("unchecked")
    public void initTopo_C1() throws IOException {
        //解析收到的包 PhyTopoList
        PhyTopoList = (HashMap)CreateReceivedPackage.message;

        Iterator iter_PhyTopoList = PhyTopoList.keySet().iterator();
        while (iter_PhyTopoList.hasNext()) {
            //得到物理拓扑 Domain_ID topo[][] edge[][]
            Long Domain_ID = (Long) iter_PhyTopoList.next();//Domain_ID = vnmID
            // PacketTopo phytopo = PhyTopoList.get(Domain_ID);
            int[][] phytopo = PhyTopoList.get(Domain_ID).topo;
            int[][] phyedges = PhyTopoList.get(Domain_ID).topo;
            //得到链路信息
 /*
        * Get the topo information from matrix
        * */
            Map<Long,Long> addedLinks = new HashMap<Long,Long>();
            int link_num = 0 ;
            for(int i = 0 ; i < phytopo.length ; i++) {
                nodeCounter++;
                //physicalNodes.add(new Long((long) (i + 1)));
                List<Long> setphysicalLink =  new ArrayList<Long>() ;
                //  for (int j = column - i - 1 ; j < column; j++) {
                for (int j = 0; j < phytopo.length; j++) {

                    if (topo[i][j] == 1) {
                        link_num++;
                        Long node = new Long((long) (i + 1));
                        Long remoteNode = new Long((long) (j + 1));
                        Long nodeDomain = Domain_ID;
                        Long remoteNodeDomain = Domain_ID;

                        addedLinks.put(node, remoteNode);
                        LOG.info("addedlinks are :{}",addedLinks);
                        nodeMapDomain.put(node, nodeDomain);
                        nodeMapDomain.put(remoteNode, remoteNodeDomain);
                        setphysicalLink.add(remoteNode);
                    }
                }
                physicalLinks.put((long)(i+1),setphysicalLink);
                LOG.info("physicalLinks:{}", physicalLinks);
            }
            LOG.info("The total link_num is :{}",link_num);
            physicalTopo.add(addedLinks);
            physicalLinksList.put(Domain_ID,physicalLinks);
            physicalTopoList.put(Domain_ID,physicalTopo);
            //  linkProperty.getPhysicalLinks();
        }


    }


    public void linkHanlder(PacketTopoReply replyBody) {

        LOG.info("---------linkHandler Start---------");
        NodeConnectorRef ingress = replyBody.getIngress();
        java.lang.Long node_IP = replyBody.getNodeIP();
        java.lang.Long domain_ID = replyBody.getDomainID();
        List<java.lang.Long> remote_node_list = replyBody.getRemoteNodeIP();

        nodeMapDomain.put(node_IP, domain_ID);
        nodeMapIngress.put(node_IP, ingress);

        if (domain_ID == 0x1L || domain_ID == 0x2L || domain_ID == 0x3L) {
            LOG.info("=-=Store logicalTopo:node_IP={}, remote_node={}", node_IP, remote_node_list);
            logicalLinks.put(node_IP, remote_node_list);
            for (Long remoteNode : remote_node_list) {
                if (node_IP.longValue() > remoteNode.longValue()) {
                    Map<Long, Long> nodePair = new HashMap<Long, Long>();
                    nodePair.put(node_IP, remoteNode);
                    logicalTopo.add(nodePair);
                }
            }


        }

        if (domain_ID == 0x4L || domain_ID == 0x5L || domain_ID == 0x6L) {
            LOG.info("=-=Store physicalTopo:node_IP={}, remote_node={}", node_IP, remote_node_list);
            physicalLinks.put(node_IP, remote_node_list);
            for (Long remoteNode : remote_node_list) {
                if (node_IP.longValue() > remoteNode.longValue()) {
                    Map<Long, Long> nodePair = new HashMap<Long, Long>();
                    nodePair.put(node_IP, remoteNode);
                    physicalTopo.add(nodePair);
                }
            }
        }
        nodeCounter++;
        LOG.info("Finish storing topology info of node:{}", nodeCounter);

    }

    //   @Override
    public int[][] getPhysicalTopoMatrix() {
        LOG.info("===AoniTed LinkProperty:topomatrix={}", topo);//just show the first line
        return topo;
    }

    public int[][] getPhysicalEdgesMatrix() {
        LOG.info("===AoniTed LinkProperty:Edgematrix={}", edges);//just show the first line
        return edges;
    }

    @Override
    public Map<Long, List<Long>> getLogicalLinks() {
        LOG.info("===AoniTed LinkProperty:logicalLinks={}", logicalLinks);
        return logicalLinks;
    }

    @Override
    public Map<Long, List<Long>> getPhysicalLinks() {
        LOG.info("===AoniTed LinkProperty:physicalLinks={}", physicalLinks);
        return physicalLinks;
    }

    @Override
    public List<Long> getPhysicalNodes() {
        LOG.info("===AoniTed LinkProperty:physicalNodes={}", physicalNodes);
        return physicalNodes;
    }

    @Override
    public Map<Long, Long> getNodeMapDomain() {
        // LOG.info("===AoniTed LinkProperty:nodeMapDomain={}",nodeMapDomain);

        return nodeMapDomain;
    }

    @Override
    public Map<Long, NodeConnectorRef> getNodeMapIngress() {
        LOG.info("===AoniTed LinkProperty:nodeMapIngress={}", nodeMapIngress);
        return nodeMapIngress;
    }

    @Override
    public Set<Map<Long, Long>> getPhysicalTopo() {
        LOG.info("===LYJ physicalTopo-{}", physicalTopo);

        return physicalTopo;
    }

    @Override
    public Set<Map<Long, Long>> getLogicalTopo() {
        LOG.info("===LYJ logicalTopo-{}", logicalTopo);

        return logicalTopo;
    }
//new for cascade
    public int[][] getPhysicalTopoMatrix(Long Domain_ID) {
       // topoList.get(Domain_ID);
        LOG.info("===C1 AoniTed LinkProperty:topomatrix={}", topoList.get(Domain_ID));//just show the first line
        return topoList.get(Domain_ID);
    }

    public int[][] getPhysicalEdgesMatrix(Long Domain_ID) {
        LOG.info("===C1 AoniTed LinkProperty:Edgematrix={}", edgesList.get(Domain_ID));//just show the first line
        return edgesList.get(Domain_ID);
    }
    @Override
    public Map<Long,Set<Map<Long, Long>>> getPhysicalTopoList() {
        LOG.info("===LYJ physicalTopo-{}", physicalTopoList);
        return physicalTopoList;
    }
    @Override
    public Map<Long,Map<Long, List<Long>>> getPhysicalLinksList() {
        LOG.info("===AoniTed LinkProperty:physicalLinks={}", physicalLinksList);
        return physicalLinksList;
    }




}