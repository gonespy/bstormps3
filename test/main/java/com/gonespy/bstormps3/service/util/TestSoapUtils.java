package com.gonespy.bstormps3.service.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TestSoapUtils {

    @Test
    public void testEmptySoapTag() {
        String tag = SoapUtils.emptySoapTag("sometag");
        assertThat(tag).isEqualTo("<ns1:sometag></ns1:sometag>");
    }

    @Test
    public void testCreateSoapTagWithValue() {
        String tag = SoapUtils.createSoapTagWithValue("sometag", "somevalue");
        assertThat(tag).isEqualTo("<ns1:sometag>somevalue</ns1:sometag>");
    }

    @Test
    public void testWrapSoapData() {
        String path = "mypath";
        String type = "mytype";
        String soapData = "SOME_SOAP_DATA";
        String finalSoapData = SoapUtils.wrapSoapData(path, type, soapData);
        assertThat(finalSoapData).isEqualTo(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns1=\"http://gamespy.net/" + path + "\"><SOAP-ENV:Body>" +
                        "<ns1:" + type + ">" + soapData + "</ns1:" + type + ">" +
                        "</SOAP-ENV:Body></SOAP-ENV:Envelope>\r\n"
        );

    }

}
