package com.gonespy.service.util;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class TestGPMessageUtils {

    @Test
    public void testParseClientLogin() {
        String test = "\\login\\\\challenge\\ZXX7h9eiTe0EP5teW1yiajFqY5URTykw\\authtoken\\11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\\partnerid\\19\\response\\fdd3c298e993ba03b70575a743a4e323\\port\\6500\\productid\\12999\\gamename\\bstormps3\\namespaceid\\28\\sdkrevision\\59\\quiet\\0\\id\\1\\final\\";

        Map<String, String> map = GPMessageUtils.parseClientLogin(test);

        assertThat(map).hasSize(11);
        assertThat(map).doesNotContainKey("login");
        assertThat(map).doesNotContainKey("final");
        assertThat(map.get("challenge")).isEqualTo("ZXX7h9eiTe0EP5teW1yiajFqY5URTykw");
        assertThat(map.get("id")).isEqualTo("1");
        assertThat(map.get("partnerid")).isEqualTo("19");
    }

    @Test
    public void testCreateGPMessage() {
        Map<String,String> loginResponseData = new LinkedHashMap<>();
        loginResponseData.put("lc", "2"); // int
        loginResponseData.put("sesskey", "5555555555"); // int
        loginResponseData.put("userid", "1234567890"); // int
        loginResponseData.put("profileid", "9876543210"); // int
        loginResponseData.put("lt", "XdR2LlH69XYzk3KCPYDkTY__"); // string
        loginResponseData.put("proof", "someproof"); // int
        loginResponseData.put("id", "1"); // int
        String loginData = GPMessageUtils.createGPMessage(loginResponseData);
        assertThat(loginData).isEqualTo("\\lc\\2" +
                "\\sesskey\\5555555555" + // int
                "\\userid\\1234567890" + // int
                "\\profileid\\9876543210"  + // int
                "\\lt\\XdR2LlH69XYzk3KCPYDkTY__" + // string
                "\\proof\\someproof"+
                "\\id\\1" +
                "\\final\\");
    }

    @Test
    public void testCreateGPEmptyListMessage() {
        String blkData = GPMessageUtils.createGPEmptyListMessage("blk");
        assertThat(blkData).isEqualTo("\\blk\\0\\list\\\\final\\");
    }

    @Test
    public void testGetGPDirective() {
        String data = GPMessageUtils.getGPDirective("\\blah\\1\\foo\\x\\bar\\1\\final\\");
        assertThat(data).isEqualTo("blah");
    }

    @Test
    public void testGetGPDirectiveNull() {
        String data = GPMessageUtils.getGPDirective("blah\\1\\foo\\x\\bar\\1\\final\\");
        assertThat(data).isNull();
    }

}
