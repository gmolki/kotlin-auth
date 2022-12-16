package com.gmolki.kotlinauth.security.keys

import org.springframework.util.ResourceUtils
import java.nio.file.Files
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

class RSAKeyPair {
    companion object {
        fun getPrivateKey() : PrivateKey {
            val rsaPrivateKeySpec = PKCS8EncodedKeySpec(
                Files.readAllBytes(
                    ResourceUtils.getFile("classpath:rsa_private_key.der").toPath()
                )
            )

            return KeyFactory.getInstance("RSA").generatePrivate(rsaPrivateKeySpec)
        }

        fun getPublicKey() : PublicKey {
            val rsaPublicKeySpec = X509EncodedKeySpec(
                Files.readAllBytes(
                    ResourceUtils.getFile("classpath:rsa_public_key.der").toPath()
                )
            )

            return KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec)
        }
    }
}