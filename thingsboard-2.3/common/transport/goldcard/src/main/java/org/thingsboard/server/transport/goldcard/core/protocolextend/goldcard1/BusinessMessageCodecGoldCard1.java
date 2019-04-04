package org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1;

import org.thingsboard.server.transport.goldcard.core.base.BusinessMessageCodec;
import org.thingsboard.server.transport.goldcard.core.dto.BusinessMessage;
import org.thingsboard.server.transport.goldcard.core.dto.ChannelPocket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shanmh
 */
@ChannelHandler.Sharable
public class BusinessMessageCodecGoldCard1 extends BusinessMessageCodec {

    private static Logger logger = LoggerFactory.getLogger(BusinessMessageCodecGoldCard1.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, BusinessMessage msg, List<Object> out) {

        msg.getBaseFrame().encodeFrame(msg);
        sendTcpCommand(msg.getBaseFrame().toHexString(), ctx.channel());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        ChannelPocket pocket = ctx.channel().attr(ChannelPocket.KEY).get();
        BusinessMessage businessMessage = new BusinessMessage(pocket.getProtocol().getProtocolName());
        byte[] original = new byte[msg.readableBytes()];
        msg.readBytes(original);
        org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1.FrameGoldCard1 frameGoldCard1 = new org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1.FrameGoldCard1();
        try {
            frameGoldCard1.resolveFrame(original);
            businessMessage.setFunCode(frameGoldCard1.getFunCode());
            businessMessage.setBaseFrame(frameGoldCard1);
            frameGoldCard1.analysisData(businessMessage);
            msg.clear();
            out.add(businessMessage);
        } catch (Exception e) {
            logger.error("帧解析出错:{}", frameGoldCard1.getOriginal(), e);
        }
    }


}
