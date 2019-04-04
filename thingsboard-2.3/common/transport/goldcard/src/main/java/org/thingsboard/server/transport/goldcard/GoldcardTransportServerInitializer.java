/**
 * Copyright © 2016-2019 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.transport.goldcard;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.Attribute;
import org.thingsboard.server.transport.goldcard.core.base.ResolveService;
import org.thingsboard.server.transport.goldcard.core.dto.ChannelPocket;
import org.thingsboard.server.transport.goldcard.core.dto.MeterProtocol;
import org.thingsboard.server.transport.goldcard.core.protocolextend.goldcard1.Goldcard1Impl;
import org.thingsboard.server.transport.goldcard.util.SpringContextHolder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
public class GoldcardTransportServerInitializer extends ChannelInitializer<SocketChannel> {

    private final GoldcardTransportContext context;

    public GoldcardTransportServerInitializer(GoldcardTransportContext context) {
        this.context = context;
    }

    @Override
    public void initChannel(SocketChannel ch) {
//        pipeline.addLast("decoder", new GoldcardDecoder(context.getMaxPayloadSize()));
//        pipeline.addLast("encoder", GoldcardEncoder.INSTANCE);


        GoldcardTransportService goldcardTransportService = SpringContextHolder.getApplicationContext().getBean(GoldcardTransportService.class);
        Attribute<ChannelPocket> att = ch.attr(ChannelPocket.KEY);
        ChannelPocket channelPocket = new ChannelPocket();
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 180, TimeUnit.SECONDS));
        channelPocket.setTimeOutCount(0);
        channelPocket.setRegisterTime(new Date());
        MeterProtocol meterProtocol = new MeterProtocol();
        meterProtocol.setProtocolName("goldcard1");
        meterProtocol.setProtocolId(6011001);
        channelPocket.setProtocol(meterProtocol);
        channelPocket.setPort(3000);
        att.setIfAbsent(channelPocket);
        ResolveService resolveService = SpringContextHolder.getBean(Goldcard1Impl.class);
        resolveService.execute(ch);
        GoldcardTransportHandler handler = new GoldcardTransportHandler(context);
        ch.pipeline().addLast(handler);
        ch.closeFuture().addListener(handler);
    }

}
