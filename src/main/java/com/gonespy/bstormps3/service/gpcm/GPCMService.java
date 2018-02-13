package com.gonespy.bstormps3.service.gpcm;

/**
 * Created by gonespy on 8/02/2018.
 */

import java.io.IOException;
import java.net.ServerSocket;

public class GPCMService {

    public static final int GPCM_PORT_NUMBER = 29900;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(GPCM_PORT_NUMBER)) {
            while (true) {
                new GPCMMultiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + GPCM_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
