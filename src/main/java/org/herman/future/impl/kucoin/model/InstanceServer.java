/*
 * Copyright 2019 Mek Global Limited
 */
package org.herman.future.impl.kucoin.model;


public class InstanceServer {

    private long pingInterval;

    private String endpoint;

    private String protocol;

    private boolean encrypt;

    private long pingTimeout;

    public long getPingInterval() {
        return pingInterval;
    }

    public void setPingInterval(long pingInterval) {
        this.pingInterval = pingInterval;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public long getPingTimeout() {
        return pingTimeout;
    }

    public void setPingTimeout(long pingTimeout) {
        this.pingTimeout = pingTimeout;
    }
}
