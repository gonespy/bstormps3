package com.gonespy.service.testing;

import com.gonespy.service.shared.Constants;
import com.gonespy.service.util.CertificateUtils;
import org.junit.Ignore;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSACipherTesting {

    @Test
    @Ignore
    public void atlasCheck() throws Exception {
        // gamespy ATLAS keys - doesnt work without private key
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_KEY, 16), new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_EXP, 16)); // game client
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_KEY, 16), new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_PRIVATE_EXP, 16)); // gamespy server - PRIVATE KEY UNKNOWN

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // encrypt the message
        byte [] encrypted = encrypt(privateKey, "This is a secret message");
        System.out.println(new String(encrypted));  // <<encrypted message>>

        // decrypt the message
        byte[] secret = decrypt(pubKey, encrypted);
        System.out.println(DatatypeConverter.printHexBinary(secret));
        System.out.println(new String(secret));     // This is a secret message
    }

    @Test
    public void eaEmuCheck() throws Exception {
        // test peer keys from eaEmu
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(CertificateUtils.PEER_KEY_MODULUS, 16), new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_EXP, 16));
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(CertificateUtils.PEER_KEY_MODULUS, 16), new BigInteger(Constants.PEER_KEY_PRIVATE, 16));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // encrypt the message
        byte [] encrypted = encrypt(privateKey, "This is a secret message");
        System.out.println(new String(encrypted));  // <<encrypted message>>

        // decrypt the message
        byte[] secret = decrypt(pubKey, encrypted);
        System.out.println(new String(secret));     // This is a secret message
    }

    @Test
    public void atlasWithEaEmuSig() throws Exception {
        // gamespy ATLAS keys
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_KEY, 16), new BigInteger(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_EXP, 16)); // game client

        // try decrypting this:
        byte[] encrypted = DatatypeConverter.parseHexBinary("181A4E679AC27D83543CECB8E1398243113EF6322D630923C6CD26860F265FC031C2C61D4F9D86046C07BBBF9CF86894903BD867E3CB59A0D9EFDADCB34A7FB3CC8BC7650B48E8913D327C38BB31E0EEB06E1FC1ACA2CFC52569BE8C48840627783D7FFC4A506B1D23A1C4AEAF12724DEB12B5036E0189E48A0FCB2832E1FB00");

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);

        System.out.println(new String(encrypted));  // <<encrypted message>>

        // decrypt the message

        // decrypts to [md5Header][md5Hash]
        // [3020300C06082A864886F70D020505000410][6A9F632A5175F4C4AEAF4C6B7A3615EA]
        // (why is the header/padding missing? does java decrypt remove it for you?)
        byte[] secret = decrypt(pubKey, encrypted);
        System.out.println(DatatypeConverter.printHexBinary(secret));
        System.out.println(new String(secret));     // This is a secret message
    }

    @Test
    public void exampleCheck() throws Exception {
        final String EXAMPLE_MOD = ("00df1e0a07074067271ff8853d03ab" +
                "f2124f42da7b7725585f7c105363fb" +
                "72886a108de34c4f56630bb9010d43" +
                "a7794e8ea8412c8ce6993192c74f7d" +
                "38e57f8e953f88e54068d72b8bf247" +
                "32e61f6457f499f80ba759221d2966" +
                "50f0dca2282439b808b93ea923f805" +
                "85701e7aa704c17516b0c3739d4556" +
                "ccb27854b899a5c1e7");
        final String EXAMPLE_PUBLIC_EXP = "10001";
        final String EXAMPLE_PRIVATE_EXP = ("00d68804ae435bba93951b19c9d408" +
                "f5c6832dcdf41f58ea434d80491e7e" +
                "bcdecbd54508c3ec192d3d2d530495" +
                "03a8114ffc1a46a2e86b6e8e2a5495" +
                "1c2b175e58f9c2fcb9d4944bf99781" +
                "b6f6cb6f2260e64a950eab6f9724d6" +
                "995a4ca8f22307ec1057d1d1c50916" +
                "c6a2927b214b199dfa3ee40b65efb5" +
                "71335da094d6fc0399");

        // test some generated keys
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(EXAMPLE_MOD, 16), new BigInteger(EXAMPLE_PUBLIC_EXP, 16));
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(EXAMPLE_MOD, 16), new BigInteger(EXAMPLE_PRIVATE_EXP, 16));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        // encrypt the message
        byte [] encrypted = encrypt(privateKey, "This is a secret message");
        System.out.println(new String(encrypted));  // <<encrypted message>>

        // decrypt the message
        byte[] secret = decrypt(pubKey, encrypted);
        System.out.println(new String(secret));     // This is a secret message
    }

    private static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(message.getBytes());
    }

    private static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encrypted);
    }

}