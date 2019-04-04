package org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1;


import org.thingsboard.server.transport.goldcard.core.base.AbstractResolveService;
import org.thingsboard.server.transport.goldcard.core.base.MessageHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1.BusinessMessageCodecGoldCard1;


/**
 * 安全传输模块
 */
@Service("goldcard1")
public class Goldcard1Impl extends AbstractResolveService {

    protected static final Logger logger = LoggerFactory
            .getLogger(Goldcard1Impl.class);


    @Override
    public void execute(SocketChannel ch) {
        //注册帧编解码器处理
        ch.pipeline().addLast(new org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1.RegFrameCodeceGoldCard1());

        //长度编解码器
        //ch.pipeline().addLast(new MyLengthDecoder(order,Integer.MAX_VALUE, offset, size, lengthAdjustment, initialBytesToStrip, true, servicePoint.getProtocol().getName()));


        //帧模板编解码器
        ch.pipeline().addLast(new BusinessMessageCodecGoldCard1());

        //消息处理
        ch.pipeline().addLast(new MessageHandler());
    }
}
