/*
 * Copyright (c) 2014 Pacnet and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.controller.aoniTed;

//import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.toporeply.rev160926.PacketTopoReply;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.toporeply.rev160926.PacketTopoReplyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import java.util.List;



class AoniTedHandler implements PacketTopoReplyListener {

    private static final Logger LOG = LoggerFactory.getLogger(AoniTedHandler.class);

    public LinkProperty linkProperty = new LinkProperty();

    @Override
    public void onPacketTopoReply(PacketTopoReply notification) {

        LOG.info("*****LYJ AoniTedHandler onPacketTopoReply-{}",notification);
        linkProperty.linkHanlder(notification);
    }
}