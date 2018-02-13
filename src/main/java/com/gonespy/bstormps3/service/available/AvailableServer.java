package com.gonespy.bstormps3.service.available;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static com.gonespy.bstormps3.service.util.StringUtils.asciiToReadableHex;

public class AvailableServer {
    private static final String DISPLAY_NAME = AvailableServer.class.getSimpleName();
    public static final int AVAILABLE_SERVER_PORT = 27900;

    private static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);

    public static void main(String[] args) throws IOException {
        LOG.info("Listening on port " + AVAILABLE_SERVER_PORT);
        new AvailableServerThread().start();
    }

    public static class AvailableServerThread extends Thread {

        private final byte[] AVAILABLE_RESPONSE = new byte[] { (byte)0xFE, (byte)0xFD, 0x09, 0x00, 0x00, 0x00, 0x00};
        // private final byte[] UNAVAILABLE_RESPONSE = new byte[] {(byte)0xFE, (byte)0xFD, 0x09, 0x00, 0x00, 0x00, 0x01};
        // private final byte[] TEMP_UNAVAILABLE_RESPONSE = new byte[] {(byte)0xFE, (byte)0xFD, 0x09, 0x00, 0x00, 0x00, 0x02};

        protected DatagramSocket socket = null;

        public AvailableServerThread() throws IOException {
            this("AvailableServerThread");
        }

        public AvailableServerThread(String name) throws IOException {
            super(name);
            socket = new DatagramSocket(AVAILABLE_SERVER_PORT);
        }

        public void run() {
            boolean loop = true;
            while(loop) {
                try {
                    byte[] buf = new byte[256];
                    // receive request
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    buf = AVAILABLE_RESPONSE; // always available :)
                    LOG.info("Sent to " + packet.getSocketAddress() + " - " + asciiToReadableHex(buf));
                    packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                    loop = false;
                }
            }
            socket.close();
        }
    }
}
