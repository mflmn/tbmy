package org.thingsboard.server.transport.goldcard.core.base;

import io.netty.channel.socket.SocketChannel;


/**
 * 协议编解码模块
 */
public interface ResolveService {

    /**
     * tpc协议初始化
     */
    void execute(SocketChannel ch);
}
