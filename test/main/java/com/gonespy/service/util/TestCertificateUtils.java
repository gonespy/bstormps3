package com.gonespy.service.util;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class TestCertificateUtils {
    @Test
    public void testGetCertificate() {
        Map<String, Object> input = ImmutableMap.of(
                "partnercode", "0",
                "namespaceid", "0",
                "version", "1"
        );
        Map<String, String> certificate = CertificateUtils.getCertificate(input);
        assertThat(certificate).hasSize(14);
        assertThat(certificate).containsEntry("length", "303");
        assertThat(certificate).containsEntry("version", "1");
        assertThat(certificate).containsEntry("partnercode", "0");
        assertThat(certificate).containsEntry("namespaceid", "0");
        assertThat(certificate).containsEntry("userid", "6666");
        assertThat(certificate).containsEntry("profileid", "7777");
        assertThat(certificate).containsEntry("expiretime", "0");
        assertThat(certificate).containsEntry("profilenick", "BulletstormPlayer");
        assertThat(certificate).containsEntry("uniquenick", "BulletstormPlayer");
        assertThat(certificate).containsEntry("cdkeyhash", "");
        assertThat(certificate).containsEntry("peerkeymodulus", "95375465E3FAC4900FC912E7B30EF7171B0546DF4D185DB04F21C79153CE091859DF2EBDDFE5047D80C2EF86A2169B05A933AE2EAB2962F7B32CFE3CB0C25E7E3A26BB6534C9CF19640F1143735BD0CEAA7AA88CD64ACEC6EEB037007567F1EC51D00C1D2F1FFCFECB5300C93D6D6A50C1E3BDF495FC17601794E5655C476819");
        assertThat(certificate).containsEntry("peerkeyexponent", "010001");
        assertThat(certificate).containsEntry("serverdata", "908EA21B9109C45591A1A011BF84A18940D22E032601A1B2DD235E278A9EF131404E6B07F7E2BE8BF4A658E2CB2DDE27E09354B7127C8A05D10BB4298837F96518CCB412497BE01ABA8969F9F46D23EBDE7CC9BE6268F0E6ED8209AD79727BC8E0274F6725A67CAB91AC87022E5871040BF856E541A76BB57C07F4B9BE4C6316");

        String signature = certificate.get("signature");
        assertThat(signature).hasLength(256);
        //assertThat(signature).isEqualTo("6846C883E473E0037E9D7C490F1676F49E868BCFCBBF98A28467435A4261C81D52D6F7C607A452376976D896DE8EF8440D8A4595233063D5F88AE80E3AF5CE36F89D4CF18322E109F23B2531F70BB4B3B3F413C1DD9026FA4DBE061468E1CEFCE6255C428071D8D25C6F27ED1A65DB775E9F4EA5EFA093D3629FE32C81B327BD");

        //String descrypted = decrypt(CertificateUtils.WS_AUTHSERVICE_SIGNATURE_KEY, CertificateUtils.WS_AUTHSERVICE_SIGNATURE_EXP, signature);
        //assertThat(descrypted).isEqualTo(
        //        CertificateUtils.CRYPTO_PREFIX + Strings.repeat("FF", 80) + CertificateUtils.CRYPTO_SEPARATOR_BYTE +
        //               CertificateUtils.MD5_HEADER_STRING + "e52fe9ab3aad190b99598845f07a3f3a"
        //);

    }

    private String decrypt(String modString, String expString, String base) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //String modString = mod.toHexString();
            //String expString = power.toHexString();
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modString, 16), new BigInteger(expString, 16));
            RSAPublicKey pubKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] cipherText = cipher.doFinal(GsLargeInt.gsLargeIntSetFromHexString(base).dataAsByteArray());
            //String encrypted = new String(cipherText);
            //System.out.println("cipher: " + encrypted);


            byte[] bytes = cipherText;
            StringBuilder sb = new StringBuilder();
            for(int i = bytes.length - 1; i >= 0; i -= Integer.BYTES) {
                for(int j = i-3; j <= i; j++) {
                    sb.append(String.format("%02X", bytes[j]));
                }
            }
            String decrypted = sb.toString();

            return decrypted;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
