package com.transaction_microservice.kafka;

import com.transaction_microservice.security.PublicKeyVault;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final PublicKeyVault publicKeyVault;

    @KafkaListener( topics = "public_key_distribution", groupId = "public-key-consumer" )
    public void listen( String publicKey ) {
        System.out.println( "Received public key: " + publicKey );

        publicKeyVault.convertStringToPublicKey( publicKey );
    }
}
