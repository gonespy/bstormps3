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

    public static String asciiToReadableHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X ", b));
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

    public static String gsLoginProof(final String password, final String user, final String clientChallenge,
                                      final String serverChallenge) {
        final String passwordHash = DigestUtils.md5Hex(password);
        final String preHash = passwordHash + MD5_FILLER + user + serverChallenge + clientChallenge + passwordHash;
        return DigestUtils.md5Hex(preHash);
    }

    private static byte[] hexStringToByteArray(String hexString) {
        byte[] serverData = new byte[0];
        try {
            serverData = Hex.decodeHex(hexString.toCharArray());
        } catch (DecoderException d) {
        }
        return serverData;
    }

    public static String hashCertificate(Map<String,String> certificateData) {
        MessageDigest md = DigestUtils.getMd5Digest();

        // integers
        //      temp = wsiMakeLittleEndian32(cert->mLength);
        //      MD5Update(&md5, (unsigned char*)&temp, 4);
        messageDigestUpdateInteger(md, certificateData.get("length"));
        messageDigestUpdateInteger(md, certificateData.get("version"));
        messageDigestUpdateInteger(md, certificateData.get("partnercode"));
        messageDigestUpdateInteger(md, certificateData.get("namespaceid"));
        messageDigestUpdateInteger(md, certificateData.get("userid"));
        messageDigestUpdateInteger(md, certificateData.get("profileid"));
        messageDigestUpdateInteger(md, certificateData.get("expiretime"));

        // strings
        //      MD5Update(&md5, (unsigned char*)&cert->mProfileNick, strlen(cert->mProfileNick));
        messageDigestUpdateString(md, certificateData.get("profilenick"));
        messageDigestUpdateString(md, certificateData.get("uniquenick"));
        messageDigestUpdateString(md, certificateData.get("cdkeyhash"));

        // Largeints from peer public key
        // XML: big-endian
        // SDK read: converts to little-endian internally
        // SDK MD5: converts back to big-endian before MD5 update
        //      gsLargeIntAddToMD5(&cert->mPeerPublicKey.modulus, &md5);
        messageDigestUpdateLargeInt(md, certificateData.get("peerkeymodulus"));
        messageDigestUpdateLargeInt(md, certificateData.get("peerkeyexponent"));

        // serverdata string
        // In XML: hex
        // SDK read: converts from hex to string on read
        // SDK MD5:
        // TODO: endian-ness? in SDK when reading the hex string from the cert: // switch endianess, e.g. first character in hexstring is HI byte
        messageDigestUpdateHexString(md, certificateData.get("serverdata"));

        return Hex.encodeHexString(md.digest());
    }

    private static void messageDigestUpdateInteger(MessageDigest md, String str) {
        if(str != null) {
            Integer i = Integer.parseInt(str);
            byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(i).array();
            md.update(bytes, 0, Integer.BYTES);
        }
    }

    private static void messageDigestUpdateString(MessageDigest md, String str) {
        if(str != null) {
            byte[] arr = str.getBytes();
            md.update(arr, 0, arr.length);
        }
    }

    private static void messageDigestUpdateHexString(MessageDigest md, String str) {
        if(str != null) {
            byte[] arr = hexStringToByteArray(str);
            md.update(arr, 0, arr.length);
        }
    }

    private static void messageDigestUpdateLargeInt(MessageDigest md, String str) {
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

        if(str != null) {
            GsLargeInt lint = GsLargeInt.gsLargeIntSetFromHexString(str);

            int byteLength = lint.gsLargeIntGetByteLength();
            if (byteLength == 0) {
                return;
            }

            int dataStart = 0;
            if (byteLength % GS_LARGEINT_DIGIT_SIZE_BYTES != 0) {
                dataStart += GS_LARGEINT_DIGIT_SIZE_BYTES - (byteLength % GS_LARGEINT_DIGIT_SIZE_BYTES);
            }

            lint.gsLargeIntReverseBytes();
            md.update(lint.dataAsByteArray(), dataStart, byteLength);
            lint.gsLargeIntReverseBytes();
        }
    }

}
