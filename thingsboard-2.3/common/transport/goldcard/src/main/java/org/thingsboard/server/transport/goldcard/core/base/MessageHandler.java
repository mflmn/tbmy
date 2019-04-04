package org.thingsboard.server.transport.goldcard.core.base;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.MessageToMessageCodec;
import org.thingsboard.server.transport.goldcard.core.dto.BusinessMessage;
import org.thingsboard.server.transport.goldcard.core.dto.ChannelPocket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thingsboard.server.transport.goldcard.core.dto.MeterProtocol;
import org.thingsboard.server.transport.goldcard.util.DateStyleEnum;
import org.thingsboard.server.transport.goldcard.util.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rym
 */
@Component
@ChannelHandler.Sharable
public class MessageHandler extends MessageToMessageCodec<BusinessMessage, BusinessMessage> {


    private static Logger logger = LoggerFactory.getLogger(MessageHandler.class);


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BusinessMessage businessMessage, List<Object> list) throws Exception {
        list.add(businessMessage);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, BusinessMessage msg, List<Object> list) throws Exception {
        ChannelPocket pocket = ctx.channel().attr(ChannelPocket.KEY).get();
        MeterProtocol protocol = pocket.getProtocol();
        String spId = "1";//服务点id，张东旭增加
        String deviceNo = pocket.getDeviceNo();
        if (deviceNo == null) {
            deviceNo = msg.get("deviceNo");
            pocket.setDeviceNo(deviceNo);
        }
        msg.set("channelId", ctx.channel().id().toString());
        msg.set("original", msg.getBaseFrame().getOriginal());
        msg.set("protocolName", protocol.getProtocolName());
        msg.set("protocolId", protocol.getProtocolId().toString());
        msg.set("createTime", DateUtils.formatDate(DateStyleEnum.YYMMDDHHMMSS, new Date()));
        msg.set("deviceNo", deviceNo);
        msg.set("spId", spId);//服务点Id，张东旭增加
        if (msg.get("cmdId") == null || "".equals(msg.get("cmdId"))) {
            msg.set("cmdId", DateUtils.formatDate(DateStyleEnum.YYMMDDHHMMSS, new Date()));
        }
        msg.setStandardCode("createTime", DateUtils.formatDate(DateStyleEnum.YYMMDDHHMMSS, new Date()));
        msg.setStandardCode("deviceNo", "SP0");
        msg.setStandardCode("spId", "SP0");
        msg.setStandardCode("cmdId", "SP0");
        msg.setStandardCode("protocolId", "SP0");
        msg.setStandardCode("protocolName", "SP0");
        msg.setStandardCode("channelId", "SP0");
        msg.set("reqCmdId", pocket.getReqCmdId());
        msg.set("serialNumber", "1");
        String attribute = JSONObject.toJSONString(msg.getAttributeMap());
        String standardCode = JSONObject.toJSONString(msg.getStandardCodeMap());
        String message = "{\"attribute\":" + attribute + ",\"standardCode\":" + standardCode + "}";
        pocket.setWaitingFlag(Boolean.FALSE);
        pocket.setSendReadingFlag(Boolean.TRUE);
        list.add(message);
    }
}
