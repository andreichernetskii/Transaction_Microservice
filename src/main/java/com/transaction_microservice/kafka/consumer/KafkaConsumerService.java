package com.transaction_microservice.kafka.consumer;

import com.transaction_microservice.security.PublicKeyVault;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    public static final Logger logger = LoggerFactory.getLogger( KafkaConsumerService.class );
    private final PublicKeyVault publicKeyVault;

    @KafkaListener( topics = "public_key_distribution",
            groupId = "${kafka.consumer.group1}",
            containerFactory = "publicKeyKafkaListenerContainerFactory" )
    public void listen( String publicKey ) {
        publicKeyVault.convertStringToPublicKey( publicKey );
        System.out.println( publicKey );
    }
}
