package org.thingsboard.server.transport.goldcard.core.base;

import org.thingsboard.server.transport.goldcard.core.dto.BusinessMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.thingsboard.server.transport.goldcard.util.ByteUtil;

/**
 * @author rym
 */
@ChannelHandler.Sharable
public abstract class BusinessMessageCodec extends MessageToMessageCodec<ByteBuf, BusinessMessage> {

    private static Logger logger = LoggerFactory.getLogger(BusinessMessageCodec.class);


    //服务端tcp发送消息
    protected void sendTcpCommand(String commandByte, Channel channel) {
        if (!StringUtils.isEmpty(commandByte)) {
            byte[] data = ByteUtil.hexToByte(commandByte);
            ByteBuf out = channel.alloc().buffer(data.length);
            out.writeBytes(data);
            ChannelFuture future = channel.writeAndFlush(out);
            future.addListener(mfuture -> {
                if (mfuture.isSuccess()) {
                    logger.info("success send-->:{} to remoteAddress:{}", commandByte, channel.remoteAddress());
                } else {
                    logger.error(" fail send-->:{} to remoteAddress:{}", commandByte, channel.remoteAddress());
                }
            });

        }
    }

}
