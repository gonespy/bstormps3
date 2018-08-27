package com.gonespy.service.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.gonespy.service.util.CertificateUtils.SERVER_DATA;
import static com.google.common.truth.Truth.assertThat;

public class TestStringUtils {

    // GOLDEN MD5 - this is what signature contains & what we need!
    private static final String EA_EMU_MD5 = "6a9f632a5175f4c4aeaf4c6b7a3615ea";

    @Test
    public void testGsLoginProof() {
        String md5 = StringUtils.gsLoginProof("password","user","clientchallenge","serverchallenge");
        assertThat(md5).isEqualTo("19a0f989ac154aaf8039b2af1ea314bb");
    }

    @Test
    public void testHashCertificate() {
        Map<String, String> certificateData = new LinkedHashMap<>();
        fillIntegers(certificateData);
        fillStrings(certificateData);
        fillPeerKey(certificateData);
        fillServerData(certificateData);

        String md5 = StringUtils.hashCertificate(certificateData);
        assertThat(md5).isEqualTo("535350f3423eb1d125a54b39e7b3a57f"); // verified in C v2
        assertThat(md5).isEqualTo(EA_EMU_MD5); // what we actually want
    }

    @Test
    @Ignore
    public void testHashCertificateExperiment() {
        Map<String, String> certificateData = new LinkedHashMap<>();
        fillIntegers(certificateData);
        fillStrings(certificateData);
        fillPeerKey(certificateData);
        fillServerData(certificateData);

        for(int partnerCode=0; partnerCode<=1; partnerCode++) {
            for(int namespaceId=0; namespaceId<=1; namespaceId++) {
                for(int version=0; version<=5; version++) {
                    for(int userId=0; userId<=10000; userId++) {
                        for (int profileId = 0; profileId <= 10000; profileId++) {
                            certificateData.put("version", "" + version);
                            certificateData.put("partnercode", "" + partnerCode);
                            certificateData.put("namespaceid", "" + namespaceId);
                            certificateData.put("userid", "" + userId);
                            certificateData.put("profileid", "" + profileId);
                            String md5 = StringUtils.hashCertificate(certificateData).toLowerCase();
                            //System.out.println(md5);
                            if (md5.equals(EA_EMU_MD5)) {
                                System.out.println("partnerCode=" + partnerCode + ",namespaceId=" + namespaceId + ",version=" + version);
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testHashCertificateIntegers() {
        Map<String, String> certificateData = new LinkedHashMap<>();
        fillIntegers(certificateData);

        String md5 = StringUtils.hashCertificate(certificateData);
        assertThat(md5).isEqualTo("7b5634841b57ff7bf9ccc97750f13621"); // verified in C
    }

    @Test
    public void testHashCertificateStrings() {
        Map<String, String> certificateData = new LinkedHashMap<>();
        fillIntegers(certificateData);
        fillStrings(certificateData);

        String md5 = StringUtils.hashCertificate(certificateData);
        assertThat(md5).isEqualTo("e2f084037c39bd2bcf3f936dd2fe2a81"); // verified in C
    }

    @Test
    public void testHashCertificateServerData() {
        Map<String, String> certificateData = new LinkedHashMap<>();
        fillIntegers(certificateData);
        fillStrings(certificateData);
        fillServerData(certificateData);

        String md5 = StringUtils.hashCertificate(certificateData);
        assertThat(md5).isEqualTo("c0ebc484d51e2797d23d6e737ce2cc04"); // verified in C
    }

    private static void fillIntegers(Map<String, String> certificateData) {
        certificateData.put("length", "303");
        certificateData.put("version", "1");
        certificateData.put("partnercode", "0");
        certificateData.put("namespaceid", "0");
        certificateData.put("userid", "11111");
        certificateData.put("profileid", "22222");
        certificateData.put("expiretime", "0");
    }

    private static void fillStrings(Map<String, String> certificateData) {
        certificateData.put("profilenick", "Jackalus");
        certificateData.put("uniquenick", "Jackalus");
        certificateData.put("cdkeyhash", "");
    }

    private static void fillPeerKey(Map<String, String> certificateData) {
        certificateData.put("peerkeymodulus",
                "95375465E3FAC4900FC912E7B30EF7171B0546DF4D185DB04F21C79153CE091859DF2EBDDFE5047D80C2EF86A2169B05A933AE2EAB2962F7B32CFE3CB0C25E7E3A26BB6534C9CF19640F1143735BD0CEAA7AA88CD64ACEC6EEB037007567F1EC51D00C1D2F1FFCFECB5300C93D6D6A50C1E3BDF495FC17601794E5655C476819");
        certificateData.put("peerkeyexponent", "010001");
    }

    private static void fillServerData(Map<String, String> certificateData) {
        certificateData.put("serverdata", SERVER_DATA);
    }


}
