package com.gonespy.bstormps3.service.gpsp;

/**
 * Created by gonespy on 8/02/2018.
 *
 *  gpsp.gamespy.com:29901
 *
 */

import java.io.IOException;
import java.net.ServerSocket;

public class GPSPService {

    public static final int GPSP_PORT_NUMBER = 29901;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(GPSP_PORT_NUMBER)) {
            while (true) {
                new GPSPMultiServerThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + GPSP_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
