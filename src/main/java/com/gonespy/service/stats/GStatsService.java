package com.gonespy.service.stats;

/**
 * Created by gonespy on 8/02/2018.
 *
 *  gstats.gamespy.com:29920
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class GStatsService implements Runnable {

    private static final String DISPLAY_NAME = GStatsService.class.getSimpleName();
    private static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);

    public static final int GSTATS_PORT_NUMBER = 29920;

    public static void main(String[] args) {
        new GStatsService().run();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(GSTATS_PORT_NUMBER)) {
            LOG.info("Listening on port " + GSTATS_PORT_NUMBER);
            while (true) {
                new GStatsServiceThread(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + GSTATS_PORT_NUMBER);
            System.exit(-1);
        }
    }

}
