package com.gonespy.service.testing;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.security.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RsaExample {
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

        return DatatypeConverter.printHexBinary(cipherText);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        //byte[] bytes = Base64.getDecoder().decode(cipherText);
        byte[] bytes = DatatypeConverter.parseHexBinary(cipherText);

                Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), UTF_8);
    }

    public static String sign(String plainText, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("MD5withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(plainText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return DatatypeConverter.printHexBinary(signature);
    }

    public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("MD5withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(plainText.getBytes(UTF_8));

        byte[] signatureBytes = DatatypeConverter.parseHexBinary(signature);

        return publicSignature.verify(signatureBytes);
    }

    public static void main(String... argv) throws Exception {
        //First generate a public/private key pair
        KeyPair pair = generateKeyPair();

        //Our secret message
        String message = "the answer to life the universe and everything";

        //Encrypt the message
        String cipherText = encrypt(message, pair.getPublic());

        //Now decrypt it
        String decipheredMessage = decrypt(cipherText, pair.getPrivate());

        System.out.println(decipheredMessage);

        //Let's sign our message
        String signature = sign("foobar", pair.getPrivate());

        // md5
        String md5 = DigestUtils.md5Hex("foobar");
        // 3858f62230ac3c915f300c664312c63f

        // decrypt instead of checking signing
        String decryptedSignature = decrypt(signature, pair.getPublic());
        // [3020300C06082A864886F70D020505000410][3858F62230AC3C915F300C664312C63F]

        // cf. decrypted signature from eaEmu - MD5 header matches :)
        // [3020300C06082A864886F70D020505000410][6A9F632A5175F4C4AEAF4C6B7A3615EA]

        //Let's check the signature
        boolean isCorrect = verify("foobar", signature, pair.getPublic());
        System.out.println("Signature correct: " + isCorrect);
    }

    public static String decrypt(String cipherText, PublicKey publicKey) throws Exception {
        //byte[] bytes = Base64.getDecoder().decode(cipherText);
        byte[] bytes = DatatypeConverter.parseHexBinary(cipherText);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, publicKey);

        return DatatypeConverter.printHexBinary(decriptCipher.doFinal(bytes));
    }
}