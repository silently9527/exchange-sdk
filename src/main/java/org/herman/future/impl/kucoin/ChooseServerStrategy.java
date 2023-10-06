/*
 * Copyright 2019 Mek Global Limited
 */
package org.herman.future.impl.kucoin;


import org.herman.future.impl.kucoin.model.InstanceServer;

import java.util.List;

public interface ChooseServerStrategy {

    /**
     * For choose web socket connect server
     *
     * @param servers
     * @return A server instance.
     */
    InstanceServer choose(List<InstanceServer> servers);

}
