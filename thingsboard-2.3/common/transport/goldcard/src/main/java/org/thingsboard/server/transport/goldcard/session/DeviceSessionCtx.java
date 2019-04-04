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
package org.thingsboard.server.transport.goldcard.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttQoS;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.thingsboard.server.common.transport.session.DeviceAwareSessionContext;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
public class DeviceSessionCtx extends DeviceAwareSessionContext {

//    private final ConcurrentMap<MqttTopicMatcher, Integer> mqttQoSMap;
    @Getter
    private ChannelHandlerContext channel;
    private AtomicInteger msgIdSeq = new AtomicInteger(0);

    public DeviceSessionCtx(UUID sessionId) {
        super(sessionId);
    }

//    public ConcurrentMap<MqttTopicMatcher, Integer> getMqttQoSMap() {
//        return mqttQoSMap;
//    }

//    public MqttQoS getQoSForTopic(String topic) {
//        List<Integer> qosList = mqttQoSMap.entrySet()
//                .stream()
//                .filter(entry -> entry.getKey().matches(topic))
//                .map(Map.Entry::getValue)
//                .collect(Collectors.toList());
//        if (!qosList.isEmpty()) {
//            return MqttQoS.valueOf(qosList.get(0));
//        } else {
//            return MqttQoS.AT_LEAST_ONCE;
//        }
//    }


    public void setChannel(ChannelHandlerContext channel) {
        this.channel = channel;
    }

    public int nextMsgId() {
        return msgIdSeq.incrementAndGet();
    }
}
