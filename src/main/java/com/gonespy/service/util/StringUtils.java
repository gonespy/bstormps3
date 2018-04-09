package com.gonespy.service.util;

import com.google.common.base.Strings;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;

import static com.gonespy.service.util.GsLargeInt.GS_LARGEINT_DIGIT_SIZE_BYTES;

public abstract class StringUtils {

    private static final String MD5_FILLER = Strings.padEnd("", 48, ' ');

    public static String asciiToHex(char[] chars) {
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    public static String asciiToReadableHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X ", b));
        }

        return hex.toString();
    }

    public static String asciiToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString((int) ch));
        }

        return hex.toString();
    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToReadableHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3 - 1];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = HEX_ARRAY[v >>> 4];
            hexChars[j * 3 + 1] = HEX_ARRAY[v & 0x0F];
            if(j != bytes.length - 1) {
                hexChars[j * 3 + 2] = ' ';
            }
        }
        return new String(hexChars);
    }

    private String getSaltString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String gsLoginProof(final String password, final String user, final String clientChallenge,
                                      final String serverChallenge) {
        final String passwordHash = DigestUtils.md5Hex(password);
        final String preHash = passwordHash + MD5_FILLER + user + serverChallenge + clientChallenge + passwordHash;
        return DigestUtils.md5Hex(preHash);
    }

    public static String hashCertificate(Map<String,String> certificateData) {
        MessageDigest md = DigestUtils.getMd5Digest();

        // integers
        //      temp = wsiMakeLittleEndian32(cert->mLength);
        //      MD5Update(&md5, (unsigned char*)&temp, 4);
        messageDigestUpdateInteger(md, "length", certificateData);
        messageDigestUpdateInteger(md, "version", certificateData);
        messageDigestUpdateInteger(md, "partnercode", certificateData);
        messageDigestUpdateInteger(md, "namespaceid", certificateData);
        messageDigestUpdateInteger(md, "userid", certificateData);
        messageDigestUpdateInteger(md, "profileid", certificateData);
        messageDigestUpdateInteger(md, "expiretime", certificateData);

        // strings
        //      MD5Update(&md5, (unsigned char*)&cert->mProfileNick, strlen(cert->mProfileNick));
        if(certificateData.get("profilenick") != null) md.update(certificateData.get("profilenick").getBytes(), 0, certificateData.get("profilenick").getBytes().length);
        if(certificateData.get("uniquenick") != null) md.update(certificateData.get("uniquenick").getBytes(), 0, certificateData.get("uniquenick").getBytes().length);
        if(certificateData.get("cdkeyhash") != null) md.update(certificateData.get("cdkeyhash").getBytes(), 0, certificateData.get("cdkeyhash").getBytes().length);

        // Largeints from peer public key
        //      gsLargeIntAddToMD5(&cert->mPeerPublicKey.modulus, &md5);
        if(certificateData.get("peerkeymodulus") != null) messageDigestUpdateLargeInt(md, "peerkeymodulus", certificateData);
        if(certificateData.get("peerkeyexponent") != null) messageDigestUpdateLargeInt(md, "peerkeyexponent", certificateData);

        // serverdata string
        // TODO: endian-ness? in SDK when reading the hex string from the cert: // switch endianess, e.g. first character in hexstring is HI byte
        if(certificateData.get("serverdata") != null) {
            byte[] serverData = hexStringToByteArray(certificateData.get("serverdata"));
            md.update(serverData, 0, serverData.length);
        }

        return Hex.encodeHexString(md.digest());
    }

    private static byte[] hexStringToByteArray(String hexString) {
        byte[] serverData = new byte[0];
        try {
            serverData = Hex.decodeHex(hexString.toCharArray());
        } catch (DecoderException d) {
        }
        return serverData;
    }

    // TODO: probably wrong.
    private static void messageDigestUpdateLargeInt(MessageDigest md, String field, Map<String, String> certificateData) {
        /*
        // hashing is made complicated by differing byte orders
        void gsLargeIntAddToMD5(const gsLargeInt_t * _lint, MD5_CTX * md5)
        {
            int byteLength = 0;
            gsi_u8 * dataStart = NULL;

            // Create a non-const copy so we can reverse bytes to add to the MD5 hash
            gsLargeInt_t lint;
            memcpy(&lint, _lint, sizeof(lint));

            // first, calculate the byte length
            byteLength = (int)gsLargeIntGetByteLength(&lint);
            if (byteLength == 0)
                return; // no data

            dataStart = (gsi_u8*)lint.mData;
            if ((byteLength % GS_LARGEINT_DIGIT_SIZE_BYTES) != 0)
                dataStart += GS_LARGEINT_DIGIT_SIZE_BYTES - (byteLength % GS_LARGEINT_DIGIT_SIZE_BYTES);

            // reverse to big-endian (MS) then hash
            gsLargeIntReverseBytes(&lint);
            MD5Update(md5, dataStart, (unsigned int)byteLength);
            gsLargeIntReverseBytes(&lint);
        }
         */

        /*GsLargeInt lint = GsLargeInt.gsLargeIntSetFromHexString(certificateData.get(field));

        int byteLength = lint.gsLargeIntGetByteLength();
        if(byteLength == 0) {
            return;
        }

        int dataStart = 0;
        if(byteLength % GS_LARGEINT_DIGIT_SIZE_BYTES != 0) {
            dataStart += GS_LARGEINT_DIGIT_SIZE_BYTES - (byteLength % GS_LARGEINT_DIGIT_SIZE_BYTES);
        }

        lint.gsLargeIntReverseBytes();
        md.update(lint.dataAsByteArray(), dataStart, byteLength);
        lint.gsLargeIntReverseBytes();*/

        byte[] serverData = hexStringToByteArray(certificateData.get(field));
        md.update(serverData, 0, serverData.length);
    }

    private static void messageDigestUpdateInteger(MessageDigest md, String field, Map<String, String> certificateData) {
        // need integers to be little endian
        md.update(certStringToByteArrayLittleEndian(certificateData.get(field)), 0, Integer.BYTES);
    }

    private static byte[] certStringToByteArrayLittleEndian(String s) {
        try {
            Integer i = Integer.parseInt(s);
            //i = Integer.reverseBytes(i); // java md5 does this for us apparently?
            return ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
        } catch(NumberFormatException nfe) {
            return new byte[4];
        }
    }



}
