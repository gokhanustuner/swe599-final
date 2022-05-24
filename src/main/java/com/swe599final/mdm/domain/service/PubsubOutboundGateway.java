package com.swe599final.mdm.domain.service;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Service;

@Service
@MessagingGateway(defaultRequestChannel = "pubsubOutputChannel")
public interface PubsubOutboundGateway {
    void sendToPubsub(String text);
}