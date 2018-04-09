package com.gonespy.service.util;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

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

    @Test
    public void testParseSoapData() {
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:ns1=\"http://gamespy.net/AuthService/\"><SOAP-ENV:Body><ns1:LoginPs3Cert><ns1:version>1</ns1:version><ns1:gameid>2188</ns1:gameid><ns1:partnercode>0</ns1:partnercode><ns1:namespaceid>0</ns1:namespaceid><ns1:npticket><ns1:Value>IQEAAAAAAPAwAACkAAgAFPIyPmD0qSOwfjWy2CLrmF65ZbzVAAEABAAAAQAABwAIAAABYnMkGq4ABwAIAAABYnMtP4AAAgAIFBsdFI2xVbMABAAgRG9jdG9yX19fUm9ja3pvAAAAAAAAAAAAAAAAAAAAAAAACAAEdXMAAQAEAARkMQAAAAgAGFVQMTAxOC1CTFVTMzAyNTFfMDAAAAAAAAABAAQkAAIAAAAAAAAAAAAwAgBEAAgABCJfNjgACAA4MDQCGGPoGctsiTp228enBIE0uh3YBvuxMQ1QzgIYbjniP4oW4FdH8X5r7i8sDuw09WTBzvsoAAA=</ns1:Value></ns1:npticket></ns1:LoginPs3Cert></SOAP-ENV:Body></SOAP-ENV:Envelope>";
        Map<String, Object> map = SoapUtils.parseSoapData(data);
        assertThat(map).hasSize(6);
        assertThat(map).containsEntry("gameid", "2188");
        assertThat(map).containsEntry("namespaceid", "0");
        assertThat(map).containsEntry("partnercode", "0");
        assertThat(map).containsEntry("ns1", "LoginPs3Cert");
        assertThat(map).containsEntry("version", "1");
        assertThat(map).containsEntry("npticket", ImmutableMap.of("Value", "IQEAAAAAAPAwAACkAAgAFPIyPmD0qSOwfjWy2CLrmF65ZbzVAAEABAAAAQAABwAIAAABYnMkGq4ABwAIAAABYnMtP4AAAgAIFBsdFI2xVbMABAAgRG9jdG9yX19fUm9ja3pvAAAAAAAAAAAAAAAAAAAAAAAACAAEdXMAAQAEAARkMQAAAAgAGFVQMTAxOC1CTFVTMzAyNTFfMDAAAAAAAAABAAQkAAIAAAAAAAAAAAAwAgBEAAgABCJfNjgACAA4MDQCGGPoGctsiTp228enBIE0uh3YBvuxMQ1QzgIYbjniP4oW4FdH8X5r7i8sDuw09WTBzvsoAAA="));
    }

}
