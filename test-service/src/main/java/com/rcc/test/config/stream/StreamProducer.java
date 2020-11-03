package com.rcc.test.config.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding({ StreamClient.class })
@Component
public class StreamProducer {

    @Autowired
    private StreamClient streamClient;

    public Boolean produceMsg(Object message) {
        return streamClient.output().send(MessageBuilder.withPayload(message).build());
    }
}