package org.herman.future.impl;

import org.herman.utils.JsonWrapper;

@FunctionalInterface
public interface WebsocketResponseValidator {
    boolean validate(JsonWrapper json);
}
