package com.gonespy.service;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public abstract class DropwizardProbe {

    static {
        disableSslVerification();
    }

    // wait for service to be running before continuing
    public static void probeOnPort(int port, boolean waitForConnect) {
        boolean keepGoing = true;
        while (keepGoing) {
            try {
                URL url;
                if(port == 443) {
                    url = new URL("https://localhost/probe");
                } else {
                    url = new URL("http://localhost/probe");
                }
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                System.out.println("Port " + port + " is responding: " + con.getResponseCode());
                break;
            } catch (IOException io) {
                System.out.println("Waiting for port " + port + " to accept connection");
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {}
            }
            // stop if we don't want to retry
            keepGoing = waitForConnect;
        }
    }


    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException|KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
