package com.gonespy.bstormps3.service.util;

import com.gonespy.bstormps3.service.gpcm.GPCMMultiServerThread;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TestGPCMMultiServerThread {

    @Test
    public void testGsLoginProof() {
        String md5 = GPCMMultiServerThread.gsLoginProof("password","user","clientchallenge","serverchallenge");
        assertThat(md5).isEqualTo("19a0f989ac154aaf8039b2af1ea314bb");
    }
}
