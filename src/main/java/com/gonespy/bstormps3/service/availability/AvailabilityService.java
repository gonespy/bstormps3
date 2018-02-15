package com.gonespy.bstormps3.service.availability;

import com.gonespy.bstormps3.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class AvailabilityService implements Runnable {
    private static final String DISPLAY_NAME = AvailabilityService.class.getSimpleName();
    private static final int AVAILABLE_SERVER_PORT = 27900;

    private static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);

    public static void main(String[] args) {
        new AvailabilityService().run();
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket( AVAILABLE_SERVER_PORT );
            LOG.info("Listening on port " + AVAILABLE_SERVER_PORT + "[UDP]");
        } catch ( SocketException e ) {
            LOG.error( "Could not open socket on port " + AVAILABLE_SERVER_PORT + ". " + e.getMessage());
            System.exit( 1 );
        }

        boolean loop = true;
        while (loop) {
            try {
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket( buffer, buffer.length );
                socket.receive(packet);
                new AvailabilityServerThread(socket, packet).run();
            } catch ( IOException e ) {
                LOG.error( "Could not spawn thread to handle connection: " + e.getMessage() );
                loop = false;
            }
        }
        socket.close();
    }

    public static class AvailabilityServerThread extends Thread {

        private final byte[] AVAILABLE_RESPONSE = new byte[] { (byte)0xFE, (byte)0xFD, 0x09, 0x00, 0x00, 0x00, 0x00};
        // private final byte[] UNAVAILABLE_RESPONSE = new byte[] {(byte)0xFE, (byte)0xFD, 0x09, 0x00, 0x00, 0x00, 0x01};
        // private final byte[] TEMP_UNAVAILABLE_RESPONSE = new byte[] {(byte)0xFE, (byte)0xFD, 0x09, 0x00, 0x00, 0x00, 0x02};

        private DatagramSocket socket = null;
        private DatagramPacket incomingPacket = null;

        public AvailabilityServerThread(DatagramSocket socket, DatagramPacket packet) {
            this(AvailabilityServerThread.class.getSimpleName(), socket, packet);
        }

        public AvailabilityServerThread(String name, DatagramSocket socket, DatagramPacket incomingPacket) {
            super(name);
            this.socket = socket;
            this.incomingPacket = incomingPacket;
        }

        public void run() {
            try {
                final byte[] buf = AVAILABLE_RESPONSE;
                socket.send(
                        new DatagramPacket(buf, buf.length, incomingPacket.getAddress(), incomingPacket.getPort())
                );
                LOG.info("Sent to " + incomingPacket.getSocketAddress().toString() + " - " + StringUtils.asciiToReadableHex(buf));
            } catch (IOException e) {
                LOG.error("Failed to send packet to " + incomingPacket.getSocketAddress() + ": " + e.getMessage());
            }
        }
    }

}
