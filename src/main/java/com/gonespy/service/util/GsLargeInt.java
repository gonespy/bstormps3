package com.gonespy.service.util;


import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

public class GsLargeInt {
    private static int GS_LARGEINT_BINARY_SIZE = 2048;
    public static int GS_LARGEINT_DIGIT_SIZE_BYTES = 4;
    private static int GS_LARGEINT_DIGIT_SIZE_BITS = GS_LARGEINT_DIGIT_SIZE_BYTES * 8;
    private static int GS_LARGEINT_MAX_DIGITS = GS_LARGEINT_BINARY_SIZE / GS_LARGEINT_DIGIT_SIZE_BITS;

    public int length = 0;
    // using long because Java doesn't have unsigned int
    public long[] data = new long[64]; // 2048 / 32

    public static GsLargeInt gsLargeIntSetFromHexString(String hex) {
        int len = hex.length();
        GsLargeInt ret = new GsLargeInt();
        if (len == 0) {
            ret.length = 0;
            ret.data[0] = 0;
            return ret;
        }
        if (len / 2 > GS_LARGEINT_MAX_DIGITS * GS_LARGEINT_DIGIT_SIZE_BYTES) {
            ret.length = 0;
            ret.data[0] = 0;
            return ret;
        }

        ret.length = ((len + (2 * GS_LARGEINT_DIGIT_SIZE_BYTES - 1)) / (2 * GS_LARGEINT_DIGIT_SIZE_BYTES));
        ret.data[ret.length - 1] = 0;

        int byteIndex = 0;
        long temp;

        int writePos = 0;
        while (len > 0) {
            if (len >= 2) {
                String s = hex.substring(len - 2, len);
                temp = ((Character.digit(s.charAt(0), 16) << 4)
                        + Character.digit(s.charAt(1), 16));
            } else {
                String s = hex.substring(len - 1, len);
                temp = (Character.digit(s.charAt(0), 16));
            }
            ret.data[writePos] |= (temp << (byteIndex * 8));
            if (++byteIndex == GS_LARGEINT_DIGIT_SIZE_BYTES) {
                writePos++;
                byteIndex = 0;
            }
            len -= Math.min(2, len);
        }
        return ret;
    }

    public void gsLargeIntReverseBytes() {
        int left = 0;
        int right = this.length - 1;
        long temp;
        while(right >= 0 && left < right) {
            temp = this.data[left];
            this.data[left++] = this.data[right];
            this.data[right--] = temp;
        }
    }


    public int gsLargeIntGetByteLength() {
        /*
        ///////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////
        // Length in bytes so leading zeroes can be dropped from hex strings
        gsi_u32  gsLargeIntGetByteLength(const gsLargeInt_t *lint)
        {
        int intSize = (int)lint->mLength;
        int byteSize = 0;
        int i=0;
        l_word mask = 0xFF;

        // skip leading zeroes
        while(intSize > 0 && lint->mData[intSize-1] == 0)
            intSize --;
        if (intSize == 0)
            return 0; // no data

        byteSize = intSize * (gsi_i32)sizeof(l_word);

        // subtract bytes for each leading 0x00 byte
        mask = 0xFF;
        for (i=1; i < GS_LARGEINT_DIGIT_SIZE_BYTES; i++)
        {
            if (lint->mData[intSize-1] <= mask)
            {
                byteSize -= sizeof(l_word)-i;
                break;
            }
            mask = (l_word)((mask << 8) | 0xFF);
        }

        return (gsi_u32)byteSize;
        }
        */

        int intSize = this.length;

        // skip leading zeroes
        while(intSize > 0 && this.data[intSize-1] == 0)
            intSize --;
        if (intSize == 0)
            return 0; // no data

        int byteSize = intSize * Integer.BYTES;

        // subtract bytes for each leading 0x00 byte
        long mask = 0xFF;
        for (int i=1; i < GS_LARGEINT_DIGIT_SIZE_BYTES; i++)
        {
            if (this.data[intSize-1] <= mask)
            {
                byteSize -= Integer.BYTES-i;
                break;
            }
            mask = ((mask << 8) | 0xFF);
        }

        return byteSize;
    }

    // GsLargeInt.data in C can be passed to MD5 function as byte array parameter by casting a pointer
    // in Java we need to convert each long (acting as an unsigned int) explictly to 4 bytes
    public byte[] dataAsByteArray() {
        byte[] bytes = new byte[this.length * Integer.BYTES];
        for(int integerIndex = 0; integerIndex < this.length; integerIndex++) {
            long byteVal = this.data[integerIndex];
            for(int byteIndex = 0; byteIndex < Integer.BYTES; byteIndex++) {
                if(byteIndex > 0) {
                    byteVal >>>= 8;
                }
                bytes[Integer.BYTES * integerIndex + byteIndex] = (byte)byteVal;
            }
        }
        return bytes;
    }

    //gsi_bool gsLargeIntPowerMod(const gsLargeInt_t *b, const gsLargeInt_t *p, const gsLargeInt_t *m, gsLargeInt_t *dest)
    public static GsLargeInt encrypt(GsLargeInt base, GsLargeInt privateExp, GsLargeInt privateMod) {
        // let's use RSA stuff instead of the C custom implementation
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            String modString = privateMod.toHexString();
            String expString = privateExp.toHexString();
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modString, 16), new BigInteger(expString, 16));
            RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherBytes = cipher.doFinal(base.dataAsByteArray());

            StringBuilder sb = new StringBuilder();
            for(int i = cipherBytes.length - 1; i >= 0; i -= Integer.BYTES) {
                for(int j = i-3; j <= i; j++) {
                    sb.append(String.format("%02X", cipherBytes[j]));
                }
            }
            String encrypted = sb.toString();

            return GsLargeInt.gsLargeIntSetFromHexString(encrypted);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public String toHexString() {
        byte[] bytes = dataAsByteArray();
        StringBuilder sb = new StringBuilder();
        for(int i = bytes.length - 1; i >= 0; i -= Integer.BYTES) {
            for(int j = i-3; j <= i; j++) {
                sb.append(String.format("%02X", bytes[j]));
            }
        }
        return sb.toString();
    }

    public String toString() {
        String arrData = "";
        for (long i : data) {
            arrData += i + ",";
        }
        return "{length=" + length + ", data=" + arrData + "}";
    }

}