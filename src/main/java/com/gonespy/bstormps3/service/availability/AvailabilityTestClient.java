package com.gonespy.bstormps3.service.availability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import static com.gonespy.bstormps3.service.util.StringUtils.bytesToReadableHex;

public class AvailabilityTestClient {
    private static final Logger LOG = LoggerFactory.getLogger(AvailabilityTestClient.class.getSimpleName());

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        if (args.length == 1) {
            host = args[0];
        }

        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(5000);

        // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 27900);
        LOG.info("Sending packet to " + packet.getSocketAddress());
        socket.send(packet);

        // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        // display response
        byte[] receivedBytes = Arrays.copyOfRange(packet.getData(), 0, packet.getLength() - 1);
        String received = bytesToReadableHex(receivedBytes);
        LOG.info("Received: " + received);

        socket.close();
    }

}
