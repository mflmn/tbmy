/**
 * Copyright © 2016-2019 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.transport.goldcard;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.xml.internal.messaging.saaj.soap.StringDataContentHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.thingsboard.server.common.msg.EncryptionUtil;
import org.thingsboard.server.common.transport.SessionMsgListener;
import org.thingsboard.server.common.transport.TransportService;
import org.thingsboard.server.common.transport.TransportServiceCallback;
import org.thingsboard.server.common.transport.adaptor.AdaptorException;
import org.thingsboard.server.common.transport.service.AbstractTransportService;
import org.thingsboard.server.gen.transport.TransportProtos;
import org.thingsboard.server.gen.transport.TransportProtos.*;
import org.thingsboard.server.transport.goldcard.adaptors.GoldcardTransportAdaptor;
import org.thingsboard.server.transport.goldcard.session.DeviceSessionCtx;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.cert.X509Certificate;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author
 */
@Slf4j
public class GoldcardTransportHandler extends ChannelInboundHandlerAdapter implements GenericFutureListener<Future<? super Void>>, SessionMsgListener {


    private final UUID sessionId;
    private final GoldcardTransportContext context;
    private final GoldcardTransportAdaptor adaptor;
    private final TransportService transportService;

    private volatile SessionInfoProto sessionInfo;
    private volatile InetSocketAddress address;
    private volatile DeviceSessionCtx deviceSessionCtx;

    GoldcardTransportHandler(GoldcardTransportContext context) {
        this.sessionId = UUID.randomUUID();
        this.context = context;
        this.transportService = context.getTransportService();
        this.adaptor = context.getAdaptor();
        this.deviceSessionCtx = new DeviceSessionCtx(sessionId);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("[{}] Processing goldcard msg: {}", sessionId, msg);
        processGoldcardMsg(ctx, (String)msg);
    }

    private void processGoldcardMsg(ChannelHandlerContext ctx, String msg){
        address = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info("[{}:{}] Goldcard message received,session ID:{}", address.getHostName(), address.getPort(),sessionId);
        deviceSessionCtx.setChannel(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[{}] Unexpected Exception", sessionId, cause);
        ctx.close();
    }

    @Override
    public void operationComplete(Future<? super Void> future) throws Exception {

    }

    @Override
    public void onGetAttributesResponse(TransportProtos.GetAttributeResponseMsg response) {

    }

    @Override
    public void onAttributeUpdate(TransportProtos.AttributeUpdateNotificationMsg notification) {

    }

    @Override
    public void onRemoteSessionCloseCommand(TransportProtos.SessionCloseNotificationProto sessionCloseNotification) {

    }

    @Override
    public void onToDeviceRpcRequest(TransportProtos.ToDeviceRpcRequestMsg rpcRequest) {

    }

    @Override
    public void onToServerRpcResponse(TransportProtos.ToServerRpcResponseMsg rpcResponse) {

    }
}
