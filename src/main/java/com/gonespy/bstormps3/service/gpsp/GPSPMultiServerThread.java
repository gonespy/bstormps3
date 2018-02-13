package com.gonespy.bstormps3.service.gpsp;

/**
 * Created by gonespy on 8/02/2018.
 *
 */

import com.gonespy.bstormps3.service.util.NetworkUtils;
import com.gonespy.bstormps3.service.shared.GPState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.gonespy.bstormps3.service.shared.GPState.DONE;
import static com.gonespy.bstormps3.service.shared.GPState.WAIT;

public class GPSPMultiServerThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(GPSPMultiServerThread.class);

    private Socket socket;

    public GPSPMultiServerThread(Socket socket) {
        super("GPSPMultiServerThread");
        this.socket = socket;
    }

    // SEE GP/gpiSearch.c

    // requests:
    //      search - search profile
    //      searchunique - search profile uniquenick
    //      valid - check if valid email address?
    //      nicks - search nicks
    //      pmatch - search players (other players playing this game?)
    //      check - search check
    //      newuser - new user?
    //      others - search other's buddy
    //      otherslist - search others buddy list
    //      uniquesearch - search suggest unique (for suggesting a unique nickname, given a preferred nickname)

    // responses:
    //      search
    //      searchunique
    //          bsr / bsrdone / more (multiple)
    //      valid
    //          vr
    //      nicks
    //          nr / ndone (multiple)
    //      pmatch
    //          psr / psrdone  (multiple)
    //      check
    //          cur
    //      newuser
    //          nur
    //      others
    //          o / odone (multiple)
    //      otherslist
    //          o / oldone (multiple)
    //      uniquesearch
    //          us / usdone
    //
    // * start of record's value is the profile id (eg. bsr)
    // * end of list has no value (eg. bsrdone)

    public void run() {

        GPState state = WAIT;

        try (
                InputStream reader = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {

            while(socket.isConnected() && state != DONE) {

                if(state == WAIT) {
                    sleep(10);

                    String clientString = NetworkUtils.readGPMessage(reader);
                    String directive = NetworkUtils.getGPDirective(clientString);

                    if(directive == null) {
                        if(clientString != null && clientString.length() > 0) {
                            // unrecognized message format
                            LOG.warn("Unrecognized message format. Message: " + clientString);
                        }
                    } else if(directive.equals("searchunique")) {
                        Map<String, String> inputMap = NetworkUtils.parseClientLogin(clientString);

                        // \searchunique\\sesskey\5555\profileid\7777\ uniquenick\CSlucher818\namespaces\28\gamename\bstormps3\final\

                        final String name = inputMap.get("uniquenick");

                        // searchunique result data
                        Map<String,String> responseDataMap = new LinkedHashMap<>();
                        responseDataMap.put("bsr", ""); // TODO: make a random profileid
                        responseDataMap.put("nick", name);
                        responseDataMap.put("uniquenick", name);
                        responseDataMap.put("namespaceid", inputMap.get("namespaces"));
                        responseDataMap.put("bsrdone", "");

                        out.print(NetworkUtils.createGPMessage(responseDataMap));
                        out.flush();

                    } else {
                        // directive unknown/not implemented yet
                        String clientRequestString = NetworkUtils.readGPMessage(reader);
                        if(clientRequestString != null && clientRequestString.length() > 0) {
                            LOG.info("UNKNOWN DIRECTIVE: " + clientRequestString);
                        }
                    }

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
