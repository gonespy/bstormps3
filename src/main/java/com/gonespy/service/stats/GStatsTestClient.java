package com.gonespy.service.stats;

/**
 * Created by gonespy on 8/02/2018.
 */

import com.gonespy.service.util.GPMessageUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class GStatsTestClient {
    public static void main(String[] args) {

        String hostName = "localhost";

        try (
                Socket socket = new Socket(hostName, GStatsService.GSTATS_PORT_NUMBER);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream()
        ) {

            PrintWriter p = new PrintWriter(os);
            p.write("\\searchunique\\\\sesskey\\5555\\profileid\\7777\\uniquenick\\CSlucher818\\namespaces\\28\\gamename\\bstormps3\\final\\");
            p.flush();

            String data2 = GPMessageUtils.readGPMessage(is);
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

    public static String readMessage(InputStream reader) throws IOException {
        StringBuilder msg = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;
        while((read = reader.read(buffer))!=-1) {
            String bytesRead = new String(buffer, 0, read);
            msg.append(bytesRead);
        }
        return msg.toString();
    }

}
