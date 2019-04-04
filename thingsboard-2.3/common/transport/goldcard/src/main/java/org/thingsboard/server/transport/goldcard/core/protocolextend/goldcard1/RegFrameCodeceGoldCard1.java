package org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1;

import org.thingsboard.server.transport.goldcard.core.dto.ChannelPocket;
import org.thingsboard.server.transport.goldcard.core.dto.RegMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thingsboard.server.transport.goldcard.util.ByteUtil;

import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shanmh
 */
public class RegFrameCodeceGoldCard1 extends ByteToMessageCodec<RegMessage> {

    private static Logger logger = LoggerFactory.getLogger(RegFrameCodeceGoldCard1.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, RegMessage msg, ByteBuf out) throws Exception {
        out.writeCharSequence("JKGKJ_LINK_OK", Charset.defaultCharset());
        logger.info("devNo:{} channelId: {},send: {} ", msg.get("devNo"), ctx.channel().id(), "JKGKJ_LINK_OK");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        ChannelPocket pocket = ctx.channel().attr(ChannelPocket.KEY).get();
        logger.info("SP:{},channelId: {},deviceNo:{},reciver: {}", pocket.getProtocol().getProtocolName(),
                ctx.channel().id(), pocket.getDeviceNo(), ByteBufUtil.hexDump(buf).toUpperCase());
        byte[] bytes = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), bytes);
        String hex = ByteUtil.byteToHex(bytes).toUpperCase();
        if (hex != null && !hex.startsWith("4641")) {
            out.add(buf.copy());
            buf.clear();
            return;
        }

        if (buf.readableBytes() < 13) {
            return;
        }

        Pattern pattern = Pattern.compile("4641(.*?)46");
        Matcher matcher = pattern.matcher(hex);
        String group0 = null;
        String group1 = null;
        if (matcher.find()) {
            group0 = matcher.group(0);
            group1 = matcher.group(1);
        } else {
            out.add(buf.copy());
            buf.clear();
            return;
        }
        if (group0 != null) {
            RegMessage reg = new RegMessage();
            String devNo = new String(ByteUtil.hexToByte(group1));
            reg.set("devNo", devNo);
            reg.set("channelId", ctx.channel().id().toString());
            buf.clear();
            ctx.channel().writeAndFlush(reg);
        }
    }


}
