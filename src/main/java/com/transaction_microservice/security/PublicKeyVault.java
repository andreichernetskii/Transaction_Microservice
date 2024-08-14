package com.transaction_microservice.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Getter
@Setter
@Component
public class PublicKeyVault {
    private PublicKey publicKey;

    public void convertStringToPublicKey( String publicKey ) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode( publicKey );
            X509EncodedKeySpec spec = new X509EncodedKeySpec( keyBytes );
            KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

            setPublicKey( keyFactory.generatePublic( spec ) );
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
