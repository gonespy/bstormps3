package com.gonespy.bstormps3.service.util;

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

    public static String createSoapTagWithValue(String tag, String value) {
        return openSoapTag(tag) + value + closeSoapTag(tag);
    }

    private static String openSoapTag(String tag) {
        return "<" + TAG_PREAMBLE + tag + ">";
    }

    private static String closeSoapTag(String tag) {
        return "</" + TAG_PREAMBLE + tag + ">";
    }


}
