package com.gonespy.bstormps3.service.gpsp;

/**
 * Created by gonespy on 8/02/2018.
 *
 *  gpsp.gamespy.com:29901
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class GPSPService implements Runnable {

    private static final String DISPLAY_NAME = GPSPService.class.getSimpleName();
    private static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);

    public static final int GPSP_PORT_NUMBER = 29901;

    public static void main(String[] args) {
        new GPSPService().run();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(GPSP_PORT_NUMBER)) {
            LOG.info("Listening on port " + GPSP_PORT_NUMBER);
            while (true) {
                new GPSPServiceThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + GPSP_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
