package com.transaction_microservice.kafka.consumer;

import com.transaction_microservice.security.PublicKeyVault;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    @Value( value = "${spring.kafka.bootstrap-servers}" )
    private String kafkaBootstrapAddress;

    @Value( value = "${kafka.consumer.group1}" )
    private String publicKeyGroup;

    private final PublicKeyVault publicKeyVault;

    //todo: rework producer in auth microservice
    //todo: how to config offset reset? and am i need this?
    @Bean
    public ConsumerFactory<String, String> publicKeyConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress );
        props.put( ConsumerConfig.GROUP_ID_CONFIG, publicKeyGroup );
        props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
        props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );

        return new DefaultKafkaConsumerFactory<>( props );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> limitsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory( publicKeyConsumerFactory() );

        return factory;
    }

//    @KafkaListener( topics = "public_key_distribution", groupId = "public-key-consumer" )
//    public void listen( String publicKey ) {
//        publicKeyVault.convertStringToPublicKey( publicKey );
//        System.out.println( publicKey );
//    }
//
//    @KafkaListener( topics = "new_limit", groupId = "limit-consumer" )
//    public void listenForLimitTypes( String limit ) {
//        System.out.println( limit );
//    }
}
