package com.gonespy.service.gpcm;

/**
 * Created by gonespy on 8/02/2018.
 */

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.gonespy.service.util.GPMessageUtils.readGPMessage;

public class GPCMTestClient {
    public static void main(String[] args) {

        String hostName = "localhost";

        try (
                Socket socket = new Socket(hostName, GPCMService.GPCM_PORT_NUMBER);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
        ) {

            String data = readGPMessage(is);
            System.out.println("Response: " + data);

            PrintWriter p = new PrintWriter(os);
            p.write("\\login\\\\challenge\\9uJW3IzSHMRgzUF6lYLiVMxODy1a1ENo\\authtoken\\11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\\partnerid\\19\\response\\ef38c71bfa573a0c4a075c8f3b738f62\\port\\6500\\productid\\12999\\gamename\\bstormps3\\namespaceid\\28\\sdkrevision\\59\\quiet\\0\\id\\1\\final\\");
            p.flush();

            String data2 = readGPMessage(is);
            System.out.println("Response: " + data2);

            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

}
