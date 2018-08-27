package com.gonespy.service.gpcm;

/**
 * Created by gonespy on 8/02/2018.
 */

import com.gonespy.service.shared.GPState;
import com.gonespy.service.util.GPNetworkException;
import com.gonespy.service.util.StringUtils;
import com.gonespy.service.shared.Constants;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.gonespy.service.shared.GPState.*;
import static com.gonespy.service.util.GPMessageUtils.*;

public class GPCMServiceThread extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(GPCMServiceThread.class);

    public static final int INT_AUTH_LENGTH = 4;
    private static final String DUMMY_SERVER_CHALLENGE = "ZXX7h9eiTe0EP5teW1yiajFqY5URTykw";
    private static final String DUMMY_SESSION_KEY = Strings.padEnd("", INT_AUTH_LENGTH, '5');
    public static final String DUMMY_USER_ID = Strings.padEnd("", INT_AUTH_LENGTH, '6');
    public static final String DUMMY_PROFILE_ID = Strings.padEnd("", INT_AUTH_LENGTH, '7');
    private static final String DUMMY_LOGIN_TOKEN = "XdR2LlH69XYzk3KCPYDkTY__";
    public static final String DUMMY_UNIQUE_NICK = "BulletstormPlayer";

    private Socket socket;

    public GPCMServiceThread(Socket socket) {
        super("GPSPServiceThread");
        this.socket = socket;
    }

    // additional calls seen:

    // UT3
    // \addbuddy\\sesskey\5555\newprofileid\0\reason\PS3 Buddy Sync\final\\addbuddy\\sesskey\5555\newprofileid\0\reason\PS3 Buddy Sync\final\

    public void run() {

        GPState state = CONNECT;

        try (
                InputStream reader = socket.getInputStream();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {

            while(socket.isConnected() && state != DONE) {

                if(state == CONNECT) {
                    // server talks first
                    // \lc\1\challenge\ZXX7h9eiTe0EP5teW1yiajFqY5URTykw\id\1\final\
                    Map<String,String> responseDataMap = new LinkedHashMap<>();
                    responseDataMap.put("lc", "1");
                    responseDataMap.put("challenge", DUMMY_SERVER_CHALLENGE);
                    responseDataMap.put("id", "1");
                    out.print(createGPMessage(responseDataMap));
                    out.flush();
                    state = WAIT;
                } else if(state == WAIT) {
                    sleep(10);

                    final String clientString = readGPMessage(reader);
                    final String directive = getGPDirective(clientString);

                    if(directive == null) {
                        if(clientString != null && clientString.length() > 0) {
                            // unrecognized message format
                            LOG.warn("Unrecognized message format. Message: " + clientString);
                        }
                    } else if(directive.equals("login")) {

                        // request should look like:
                        // \login\\challenge\l0OtMmxlm2pPSy5ra4lynHSMB2Qbci7M\authtoken\1111\partnerid\19\response\d3e1370bf11b79770e2dc6b36cecfb6a\port\6500\productid\12999\gamename\bstormps3\namespaceid\28\sdkrevision\59\quiet\0\id\1\final\
                        Map<String, String> inputMap = parseClientLogin(clientString);
                        LOG.info(clientString);

                        // response should look like:
                        // \blk\0\list\\final\\bdy\0\list\\final\\lc\2\sesskey\55555555555555555555555555555555\ userid\66666666666666666666666666666666\profileid\77777777777777777777777777777777\lt\XdR2LlH69XYzk3KCPYDkTY__\proof\10504cc226cc97f1d15f8c3269407500\id\1\final\
                        final String user = inputMap.get("authtoken"); // user = authtoken for PS3 preauth
                        final String clientChallenge = inputMap.get("challenge");

                        // block list - empty
                        final String blkData = createGPEmptyListMessage("blk");

                        // buddy list - empty
                        final String bdyData = createGPEmptyListMessage("bdy");

                        // login data
                        Map<String, String> responseDataMap = new LinkedHashMap<>();
                        responseDataMap.put("lc", "2"); // int
                        responseDataMap.put("sesskey", DUMMY_SESSION_KEY); // int
                        responseDataMap.put("userid", DUMMY_USER_ID); // int
                        responseDataMap.put("profileid", DUMMY_PROFILE_ID); // int
                        responseDataMap.put("uniquenick", DUMMY_UNIQUE_NICK); // should be PSNID from PSN login - don't think we have any way of knowing this
                        responseDataMap.put("lt", DUMMY_LOGIN_TOKEN); // string // login token
                        // password = partnerChallenge for PS3 preauth
                        responseDataMap.put("proof", StringUtils.gsLoginProof(Constants.DUMMY_PARTNER_CHALLENGE, user, clientChallenge, DUMMY_SERVER_CHALLENGE));
                        responseDataMap.put("id", "1"); // int

                        out.print(blkData + bdyData + createGPMessage(responseDataMap));
                        out.flush();
                    } else if(directive.equals("updatepro")) {
                        // \ updatepro\\sesskey\5555\publicmask\0\partnerid\19\final\

                        /*// update profile, setting publicmask=0 ? maybe making the profile invisible to the rest of
                        // the gamespy network because it is a shadow PS account?
                        // try sending back current user's info
                        Map<String, String> loginResponseData = new LinkedHashMap<>();
                        loginResponseData.put("pi", ""); // int
                        loginResponseData.put("profileid", Strings.padEnd("", INT_AUTH_LENGTH, '7')); // int
                        loginResponseData.put("nick", "someguy");
                        loginResponseData.put("uniquenick", "someguy");
                        loginResponseData.put("sig", "xxx"); // don't know what this is
                        String loginData = createGPMessage(loginResponseData);
                        out.print(loginData);*/
                        out.flush();
                    } else if(directive.equals("ka")) {
                        // \ka\\final\
                        // keep-alive - just send it back (client will ignore)
                        Map<String,String> responseData = new LinkedHashMap<>();
                        responseData.put("ka", "");
                        out.print(createGPMessage(responseData));
                        out.flush();
                    } else {
                        // directive unknown/not implemented yet
                        String clientRequestString = readGPMessage(reader);
                        if(clientRequestString != null && clientRequestString.length() > 0) {
                            LOG.info("unimplemented directive '" + directive + "': " + clientRequestString);
                        }
                    }

                }

            }

        } catch (GPNetworkException e) {
            LOG.info("Client closed connection");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
