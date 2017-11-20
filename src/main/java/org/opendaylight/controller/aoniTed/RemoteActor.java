package org.opendaylight.controller.aoniTed;

import akka.actor.UntypedActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by root on 11/14/17.
 */
@SuppressWarnings("unchecked")
public class RemoteActor extends UntypedActor {
    private static final Logger LOG = LoggerFactory.getLogger(RemoteActor.class);
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof String) {
            LOG.info("RemoteActor received message : " + message);
            sender().tell("Hello from the RemoteActor", self());
        }else if(message instanceof Map) {
            LOG.info("RemoteActor received  : " + message);
            LOG.info("map!!!!!!!");
            CreateReceivedPackage.message = (HashMap)message;
            sender().tell("RemoteActor received nodeId successfully", self());
        }
    }
}
