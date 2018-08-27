package com.gonespy.service.stats;

/**
 * Created by gonespy on 8/02/2018.
 *
 */

import com.gonespy.service.shared.GPState;
import com.gonespy.service.util.GPMessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.gonespy.service.shared.GPState.DONE;
import static com.gonespy.service.shared.GPState.WAIT;

public class GStatsServiceThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(GStatsServiceThread.class);

    private Socket socket;

    public GStatsServiceThread(Socket socket) {
        super("GStatsServiceThread");
        this.socket = socket;
    }

    public void run() {

        GPState state = WAIT;

        try (
                InputStream reader = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {

            while(socket.isConnected() && state != DONE) {

                if(state == WAIT) {
                    sleep(10);

                    String clientString = GPMessageUtils.readGPMessage(reader);
                    //String directive = GPMessageUtils.getGPDirective(clientString);

                    if(clientString != null && clientString.length() > 0) {
                        LOG.info("received: " + clientString);
                        Map<String, String> responseDataMap = new LinkedHashMap<>();
                        out.print(GPMessageUtils.createGPMessage(responseDataMap));
                        out.flush();
                    }

                    /*if(directive == null) {
                        if(clientString != null && clientString.length() > 0) {
                            // unrecognized message format
                            LOG.warn("Unrecognized message format. Message: " + clientString);
                        }
                    } else if(directive.equals("searchunique")) {
                        // this is querying everyone on your PSN friends list one by one, by their PSN username
                        // this is necessary in order to populate the friends list when you want to send game invites
                        Map<String, String> inputMap = GPMessageUtils.parseClientLogin(clientString);

                        // \searchunique\\sesskey\5555\profileid\7777\ uniquenick\CSlucher818\namespaces\28\gamename\bstormps3\final\

                        final String name = inputMap.get("uniquenick");

                        // searchunique result data
                        Map<String,String> responseDataMap = new LinkedHashMap<>();
                        responseDataMap.put("bsr", ""); // TODO: make a random profileid
                        responseDataMap.put("nick", name);
                        responseDataMap.put("uniquenick", name);
                        responseDataMap.put("namespaceid", inputMap.get("namespaces"));
                        responseDataMap.put("bsrdone", "");

                        out.print(GPMessageUtils.createGPMessage(responseDataMap));
                        out.flush();

                    } else {
                        // directive unknown/not implemented yet
                        String clientRequestString = GPMessageUtils.readGPMessage(reader);
                        if(clientRequestString != null && clientRequestString.length() > 0) {
                            LOG.info("UNKNOWN DIRECTIVE: " + clientRequestString);
                        }
                    }*/

                }

            }

        } catch (IOException | InterruptedException e) {
            LOG.info("Client closed exception");
            //e.printStackTrace();
        } finally {
            try {
                LOG.info("Closing socket");
                socket.close();
            } catch(IOException e) {
                LOG.info("Could not close socket. Oh well..");
            }
        }
    }

}
