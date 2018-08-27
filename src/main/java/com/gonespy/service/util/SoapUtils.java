package com.gonespy.service.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SoapUtils {
    private static final String TAG_PREAMBLE = "ns1:";
    private static final String SOAP_POSTAMBLE = "</SOAP-ENV:Body></SOAP-ENV:Envelope>\r\n";
    private static final String SOAP_PREAMBLE_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns1=\"http://gamespy.net/";
    private static final String SOAP_PREAMBLE_2 = "\"><SOAP-ENV:Body>";

    public static String wrapSoapData(String path, String type, String soap) {
        final String soapPreamble = SOAP_PREAMBLE_1 + path + SOAP_PREAMBLE_2;
        return soapPreamble + openSoapTag(type) + soap + closeSoapTag(type) + SOAP_POSTAMBLE;
    }

    public static String emptySoapTag(String tag) {
        return openSoapTag(tag) + closeSoapTag(tag);
    }

    public static String createSoapTagWithValue(String tag, Object value) {
        if(value instanceof String) {
            return openSoapTag(tag) + value + closeSoapTag(tag);
        } else {
            // map
            Map<String, String> map = (Map)value;
            String valueString = "";
            for(String key : map.keySet()) {
                valueString += openSoapTag(key) + map.get(key) + closeSoapTag(key);
            }
            return openSoapTag(tag) + valueString + closeSoapTag(tag);
        }
    }

    private static String openSoapTag(String tag) {
        return "<" + TAG_PREAMBLE + tag + ">";
    }

    private static String closeSoapTag(String tag) {
        return "</" + TAG_PREAMBLE + tag + ">";
    }

    public static Map<String, Object> parseSoapData(String soapData) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Map<String, Object> data = new HashMap<>();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch(ParserConfigurationException e) {
            return null;
        }
        InputStream stream = new ByteArrayInputStream(soapData.getBytes(StandardCharsets.UTF_8));
        Document doc;
        try {
            doc = dBuilder.parse(stream);
        } catch (IOException|SAXException e) {
            return null;
        }
        doc.getDocumentElement().normalize();
        Node soapBody = doc.getDocumentElement().getFirstChild(); // soap-env-body
        if(!soapBody.getNodeName().equals("SOAP-ENV:Body")) {
            return null;
        }
        Node ns1TopNode = soapBody.getFirstChild();
        data.put("ns1", stripTagPreamble(ns1TopNode.getNodeName()));
        NodeList nodeList = ns1TopNode.getChildNodes();
        for(int i=0; i<nodeList.getLength(); i++) {
            Node n  = nodeList.item(i);

            NodeList nl = n.getChildNodes();
            for(int j=0; j<nl.getLength(); j++) {
                Node n2 = nl.item(j);
                if(n2.getNodeType() == Node.TEXT_NODE) {
                    data.put(stripTagPreamble(n.getNodeName()), n2.getNodeValue());
                } else {
                    data.put(stripTagPreamble(n.getNodeName()), nodesToMap(n2));
                }
            }

        }
        return data;
    }

    private static Map<String, Object> nodesToMap(Node node) {
        Map<String, Object> data = new HashMap<>();
        NodeList nl = node.getChildNodes();
        for(int j=0; j<nl.getLength(); j++) {
            Node n2 = nl.item(j);
            if(n2.getNodeType() == Node.TEXT_NODE) {
                data.put(stripTagPreamble(node.getNodeName()), n2.getNodeValue());
            } else {
                data.put(stripTagPreamble(n2.getNodeName()), nodesToMap(n2));
            }
        }
        return data;
    }

    private static String stripTagPreamble(String nodeName) {
        String ret = nodeName;
        if (nodeName.startsWith(TAG_PREAMBLE)) {
            ret = nodeName.replaceFirst(TAG_PREAMBLE, "");
        }
        return ret;
    }


}
