package com.transaction_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TransactionMicroserviceApplication {

    public static void main( String[] args ) {
        SpringApplication.run( TransactionMicroserviceApplication.class, args );
    }

}
