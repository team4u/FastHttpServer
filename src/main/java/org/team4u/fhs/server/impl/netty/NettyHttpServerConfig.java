package org.team4u.fhs.server.impl.netty;

/**
 * @author Jay Wu
 */
public class NettyHttpServerConfig {
    /**
     * 请求消息最大限制字节数
     */
    int maxRequestMessageSize = 1024 * 1024;
    /**
     * Http Session最大空闲时间（秒）,0为不开启
     */
    private int httpSessionTimeoutSecs = 0;
    /**
     * 最大有效连接数
     */
    private int maxActiveConnection = -1;
    /**
     * 是否启用Http KeepAlive
     */
    private boolean httpKeepAlive = true;
    /**
     * webSocket最大空闲时间（秒）
     */
    private long webSocketTimeoutSecs = 10;
    /**
     * keepAlive最大空闲时间（秒）
     */
    private long httpKeepAliveTimeoutSecs = 5;
    /**
     * socket发送缓冲字节数大小
     */
    private int socketSndbufSize = 8192;
    /**
     * socket接收缓冲字节数大小
     */
    private int socketRcvbufSize = 8192;
    /**
     * socket-BACKLOG
     */
    private int socketBackLog = 256;

    private int connectTimeoutMillis = 5000;

    private int socketTimeoutMillis = 5000;

    /**
     * So_ReuseAddr
     */
    private boolean socketReuseAddr = true;
    /**
     * tcp-nodelay
     */
    private boolean tcpNoDelay = true;

    public int getHttpSessionTimeoutSecs() {
        return httpSessionTimeoutSecs;
    }

    public NettyHttpServerConfig setHttpSessionTimeoutSecs(int httpSessionTimeoutSecs) {
        this.httpSessionTimeoutSecs = httpSessionTimeoutSecs;
        return this;
    }

    public int getMaxActiveConnection() {
        return maxActiveConnection;
    }

    public NettyHttpServerConfig setMaxActiveConnection(int maxActiveConnection) {
        this.maxActiveConnection = maxActiveConnection;
        return this;
    }

    public int getMaxRequestMessageSize() {
        return maxRequestMessageSize;
    }

    public NettyHttpServerConfig setMaxRequestMessageSize(int maxRequestMessageSize) {
        this.maxRequestMessageSize = maxRequestMessageSize;
        return this;
    }

    public boolean isHttpKeepAlive() {
        return httpKeepAlive;
    }

    public NettyHttpServerConfig setHttpKeepAlive(boolean httpKeepAlive) {
        this.httpKeepAlive = httpKeepAlive;
        return this;
    }

    public int getSocketSndbufSize() {
        return socketSndbufSize;
    }

    public NettyHttpServerConfig setSocketSndbufSize(int socketSndbufSize) {
        this.socketSndbufSize = socketSndbufSize;
        return this;
    }

    public int getSocketRcvbufSize() {
        return socketRcvbufSize;
    }

    public NettyHttpServerConfig setSocketRcvbufSize(int socketRcvbufSize) {
        this.socketRcvbufSize = socketRcvbufSize;
        return this;
    }

    public int getSocketBackLog() {
        return socketBackLog;
    }

    public NettyHttpServerConfig setSocketBackLog(int socketBackLog) {
        this.socketBackLog = socketBackLog;
        return this;
    }

    public boolean isSocketReuseAddr() {
        return socketReuseAddr;
    }

    public NettyHttpServerConfig setSocketReuseAddr(boolean socketReuseAddr) {
        this.socketReuseAddr = socketReuseAddr;
        return this;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public NettyHttpServerConfig setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    public long getHttpKeepAliveTimeoutSecs() {
        return httpKeepAliveTimeoutSecs;
    }

    public NettyHttpServerConfig setHttpKeepAliveTimeoutSecs(long httpKeepAliveTimeoutSecs) {
        this.httpKeepAliveTimeoutSecs = httpKeepAliveTimeoutSecs;
        return this;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public NettyHttpServerConfig setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    public int getSocketTimeoutMillis() {
        return socketTimeoutMillis;
    }

    public NettyHttpServerConfig setSocketTimeoutMillis(int socketTimeoutMillis) {
        this.socketTimeoutMillis = socketTimeoutMillis;
        return this;
    }

    public long getWebSocketTimeoutSecs() {
        return webSocketTimeoutSecs;
    }

    public NettyHttpServerConfig setWebSocketTimeoutSecs(long webSocketTimeoutSecs) {
        this.webSocketTimeoutSecs = webSocketTimeoutSecs;
        return this;
    }
}
