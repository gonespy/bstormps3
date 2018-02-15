package com.gonespy.bstormps3.service.util;

import com.google.common.base.Strings;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

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

}
