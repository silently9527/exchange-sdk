/*
 * Copyright 2019 Mek Global Limited
 */
package org.herman.future.impl.kucoin;


import org.herman.future.impl.kucoin.model.InstanceServer;

import java.util.List;
import java.util.Random;

public class RandomChooseStrategy implements ChooseServerStrategy {

    private final Random random = new Random();

    @Override
    public InstanceServer choose(List<InstanceServer> servers) {
        return servers.get(random.nextInt(servers.size()));
    }

}
