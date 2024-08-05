package com.transaction_microservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class KafkaConsumer {

    @KafkaListener( topics = "public_key_distribution", groupId = "public-key-consumer" )
    public void listen( String publicKey ) {
        System.out.println( "Received public key: " + publicKey );
    }
}
