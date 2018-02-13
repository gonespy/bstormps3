package com.gonespy.bstormps3.service.available;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import static com.gonespy.bstormps3.service.util.StringUtils.bytesToReadableHex;

public class AvailableTestClient {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: java AvailableTestClient <hostname>");
            return;
        }

        // get a datagram socket
        DatagramSocket socket = new DatagramSocket();

        // send request
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 27900);
        socket.send(packet);

        // get response
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        // display response
        int len = packet.getLength();
        byte[] receivedBytes = Arrays.copyOfRange(packet.getData(), 0, len-1);
        String received = bytesToReadableHex(receivedBytes);
        System.out.println("Received: " + received);

        socket.close();
    }

}
