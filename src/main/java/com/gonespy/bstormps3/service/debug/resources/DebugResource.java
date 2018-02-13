package com.gonespy.bstormps3.service.debug.resources;

import com.gonespy.bstormps3.service.util.SoapUtils;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

// TODO: should really have AuthService & sake running separately!
// AuthService = HTTPS : 443
// sake = HTTP : 80

@Api(value = "gonespy")
@Path("/")
@Produces(MediaType.TEXT_XML)
public class DebugResource {
    public static final int STRING_AUTH_LENGTH = 32;
    public static final int INT_AUTH_LENGTH = 4;

    private static final String DUMMY_AUTH_TOKEN = Strings.padEnd("", STRING_AUTH_LENGTH, '1');
    public static final String DUMMY_PARTNER_CHALLENGE = Strings.padEnd("", STRING_AUTH_LENGTH, '2');

    private static final String LOGIN_PS3_CERT_RESULT = "LoginPs3CertResult";
    private static final String SAKE_GET_MY_RECORDS = "GetMyRecords";

    private static final Logger LOG = LoggerFactory.getLogger(DebugResource.class);

    public DebugResource() {

    }

    @POST
    @Consumes(MediaType.TEXT_XML)
    @Path("AuthService/AuthService.asmx")
    public Response authService(String request /*HttpConnection debugInformation*/) {
        return generateResponse(request);
    }

    @POST
    @Consumes(MediaType.TEXT_XML)
    @Path("SakeStorageServer/StorageServer.asmx")
    public Response sakeStorageServer(String request /*HttpConnection debugInformation*/) {
        return generateSakeResponse(request);
    }


    // just for testing without SSH/POST getting in the way
    /*@GET
    @Path("/AuthService/GetAuthService.asmx")
    public Response getAuthService(String request) {
        return generateResponse(request);
    }*/

    // demo leash unlocked?
    // <?xml version="1.0" encoding="UTF-8"?><SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:ns1="http://gamespy.net/sake"><SOAP-ENV:Body><ns1:GetMyRecords><ns1:gameid>3144</ns1:gameid><ns1:secretKey>2skyJh</ns1:secretKey><ns1:loginTicket>XdR2LlH69XYzk3KCPYDkTY__</ns1:loginTicket><ns1:tableid>DemoLeash</ns1:tableid><ns1:fields><ns1:string>unlocked</ns1:string></ns1:fields></ns1:GetMyRecords></SOAP-ENV:Body></SOAP-ENV:Envelope>

    // what are my friends' scores for this map?
    // <?xml version="1.0" encoding="UTF-8"?><SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:ns1="http://gamespy.net/sake"><SOAP-ENV:Body><ns1:SearchForRecords><ns1:gameid>3144</ns1:gameid><ns1:secretKey>2skyJh</ns1:secretKey><ns1:loginTicket>XdR2LlH69XYzk3KCPYDkTY__</ns1:loginTicket><ns1:tableid>PlayerStats_v14</ns1:tableid><ns1:filter>Nick&#x20;IN&#x20;(&apos;CSlucher818&apos;,&#x20;&apos;Lightn1ng-_-&apos;,&#x20;&apos;TheMain-pro-&apos;,&#x20;&apos;hiyoshi-&apos;,&#x20;&apos;Flatline_Freedom&apos;,&#x20;&apos;Erroneus&apos;,&#x20;&apos;StrangeRoostar&apos;,&#x20;&apos;Mitchell3212&apos;,&#x20;&apos;KeanuKou&apos;,&#x20;&apos;RickertdeH&apos;,&#x20;&apos;The_Cydonian&apos;,&#x20;&apos;Nabil-D-Jin&apos;,&#x20;&apos;hopewell26&apos;,&#x20;&apos;ANTZ4SHAW&apos;,&#x20;&apos;Ironduke-70&apos;,&#x20;&apos;tessarai&apos;,&#x20;&apos;noobsauce2010&apos;,&#x20;&apos;JakeTheDude&apos;,&#x20;&apos;Chunhea&apos;,&#x20;&apos;CRASHBASHUK&apos;,&#x20;&apos;StrangleholdCCR&apos;,&#x20;&apos;Qismat&apos;,&#x20;&apos;TumekeMus&apos;,&#x20;&apos;Big_mac_53&apos;,&#x20;&apos;vitalive&apos;,&#x20;&apos;CreateToBeGreat&apos;,&#x20;&apos;BlazinDankAllDay&apos;,&#x20;&apos;THE-BLACK_JIN&apos;,&#x20;&apos;NickMeister77&apos;,&#x20;&apos;lMATT-SNIPERl&apos;,&#x20;&apos;THEPEOPLESASS00&apos;,&#x20;&apos;ChampZilla&apos;,&#x20;&apos;NeM2k3&apos;,&#x20;&apos;lordzeze&apos;,&#x20;&apos;Medrah-&apos;,&#x20;&apos;sundevilfan50&apos;,&#x20;&apos;FerrariFSX&apos;,&#x20;&apos;shbris&apos;,&#x20;&apos;Karumph&apos;,&#x20;&apos;feba-slb&apos;,&#x20;&apos;Willmoar&apos;,&#x20;&apos;Kaerion42&apos;,&#x20;&apos;phoenixbelg&apos;,&#x20;&apos;Xehinor&apos;,&#x20;&apos;Radiophoenix&apos;,&#x20;&apos;Ahmed13&apos;,&#x20;&apos;McFood2&apos;,&#x20;&apos;chucknorris078&apos;,&#x20;&apos;Warchild0350&apos;,&#x20;&apos;WWE_Kratos2810&apos;,&#x20;&apos;PeppyRou&apos;,&#x20;&apos;Naulziator&apos;,&#x20;&apos;BearGrylls78&apos;,&#x20;&apos;ryannumber3gamer&apos;,&#x20;&apos;PickIe_Rick-91&apos;,&#x20;&apos;TiGeRthaCReaTOR&apos;,&#x20;&apos;florinisa&apos;,&#x20;&apos;GrimVenomReaver&apos;,&#x20;&apos;StrangeRooster&apos;,&#x20;&apos;Kuervo_001&apos;,&#x20;&apos;Nellio84&apos;,&#x20;&apos;MadDogSquids&apos;,&#x20;&apos;chris0crusade&apos;,&#x20;&apos;gruelingsix32&apos;,&#x20;&apos;LegionOfFish56&apos;,&#x20;&apos;shiro-ltp-809&apos;,&#x20;&apos;Repolaine&apos;,&#x20;&apos;staker158&apos;,&#x20;&apos;mickgook&apos;,&#x20;&apos;TripYoRift&apos;,&#x20;&apos;cloud25679&apos;,&#x20;&apos;SquidZzGamer1456&apos;,&#x20;&apos;ProblemedPopler&apos;,&#x20;&apos;Sizz07&apos;,&#x20;&apos;mordeckai81&apos;,&#x20;&apos;Edwards_0127&apos;,&#x20;&apos;Motley1969Crue&apos;,&#x20;&apos;MadGadabout&apos;,&#x20;&apos;kheoth&apos;,&#x20;&apos;spencer26587&apos;,&#x20;&apos;Jambici&apos;,&#x20;&apos;LandersWagstaff&apos;,&#x20;&apos;geroldhulahoop&apos;,&#x20;&apos;HULKnDaSTYX&apos;,&#x20;&apos;MaverickHunterX&apos;,&#x20;&apos;Aazesyne&apos;,&#x20;&apos;Modifiedw0lves&apos;,&#x20;&apos;Hitsugaya900&apos;,&#x20;&apos;MorbusIff&apos;,&#x20;&apos;rpg49ers&apos;,&#x20;&apos;lawrencejohnliu&apos;,&#x20;&apos;Spade-77&apos;,&#x20;&apos;Doctor___Rockzo&apos;,&#x20;&apos;DoM-the1337kid&apos;,&#x20;&apos;elite_hero99&apos;)</ns1:filter><ns1:sort>STAT_ChaosMap_WaveHighScore0_ScoreChase5&#x20;desc</ns1:sort><ns1:offset>0</ns1:offset><ns1:max>95</ns1:max><ns1:surrounding>0</ns1:surrounding><ns1:ownerids></ns1:ownerids><ns1:cacheFlag>0</ns1:cacheFlag><ns1:fields><ns1:string>STAT_ChaosMap_WaveHighScore0_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore1_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore2_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore3_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore4_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore5_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore6_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore7_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore8_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore9_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore10_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore11_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore12_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore13_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore14_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore15_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore16_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore17_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore18_ScoreChase5</ns1:string><ns1:string>STAT_ChaosMap_WaveHighScore19_ScoreChase5</ns1:string><ns1:string>ownerid</ns1:string><ns1:string>row</ns1:string><ns1:string>Nick</ns1:string></ns1:fields></ns1:SearchForRecords></SOAP-ENV:Body></SOAP-ENV:Envelope>

    // statistics
    // <?xml version="1.0" encoding="UTF-8"?><SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:ns1="http://gamespy.net/sake"><SOAP-ENV:Body><ns1:SearchForRecords><ns1:gameid>3144</ns1:gameid><ns1:secretKey>2skyJh</ns1:secretKey><ns1:loginTicket>XdR2LlH69XYzk3KCPYDkTY__</ns1:loginTicket><ns1:tableid>PlayerStats_v14</ns1:tableid><ns1:filter>MPGlobal&#x20;&gt;&#x20;0</ns1:filter><ns1:sort>Kills_MPGlobal&#x20;desc</ns1:sort><ns1:offset>0</ns1:offset><ns1:max>1</ns1:max><ns1:surrounding>0</ns1:surrounding><ns1:ownerids><ns1:int>7777</ns1:int></ns1:ownerids><ns1:cacheFlag>0</ns1:cacheFlag><ns1:fields><ns1:string>Kills_MPGlobal</ns1:string><ns1:string>Revives_MPGlobal</ns1:string><ns1:string>Skillpoints_MPGlobal</ns1:string><ns1:string>Skillshots_MPGlobal</ns1:string><ns1:string>ChallengesComplete_MPGlobal</ns1:string><ns1:string>WavesComplete_MPGlobal</ns1:string><ns1:string>FullMatchesComplete_MPGlobal</ns1:string><ns1:string>ownerid</ns1:string><ns1:string>row</ns1:string><ns1:string>Nick</ns1:string></ns1:fields></ns1:SearchForRecords></SOAP-ENV:Body></SOAP-ENV:Envelope>

    // leaderboards
    // <?xml version="1.0" encoding="UTF-8"?><SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:ns1="http://gamespy.net/sake"><SOAP-ENV:Body><ns1:SearchForRecords><ns1:gameid>3144</ns1:gameid><ns1:secretKey>2skyJh</ns1:secretKey><ns1:loginTicket>XdR2LlH69XYzk3KCPYDkTY__</ns1:loginTicket><ns1:tableid>PlayerStats_v14</ns1:tableid><ns1:filter>MPGlobal&#x20;&gt;&#x20;0</ns1:filter><ns1:sort>Kills_MPGlobal&#x20;desc</ns1:sort><ns1:offset>0</ns1:offset><ns1:max>1</ns1:max><ns1:surrounding>0</ns1:surrounding><ns1:ownerids><ns1:int>7777</ns1:int></ns1:ownerids><ns1:cacheFlag>0</ns1:cacheFlag><ns1:fields><ns1:string>Kills_MPGlobal</ns1:string><ns1:string>Revives_MPGlobal</ns1:string><ns1:string>ownerid</ns1:string><ns1:string>row</ns1:string><ns1:string>Nick</ns1:string></ns1:fields></ns1:SearchForRecords></SOAP-ENV:Body></SOAP-ENV:Envelope>

    private Response generateSakeResponse(String request) {
        LOG.info("REQUEST:");
        LOG.info(request);

        Map<String,String> soapData = new LinkedHashMap<>();
        soapData.put("responseCode", "0");
        soapData.put("authToken", DUMMY_AUTH_TOKEN);
        soapData.put("partnerChallenge", DUMMY_PARTNER_CHALLENGE);

        String response = generateSakeSoapResponse(SAKE_GET_MY_RECORDS, soapData);
        LOG.info("RESPONSE:");
        LOG.info(response);
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }


    // sakeRequestRead.c
    private String generateSakeSoapResponse(String type, Map<String, String> soapData)  {
        // TODO: all sake responses return no table rows for now
        return SoapUtils.wrapSoapData("sake", type, SoapUtils.emptySoapTag("values"));
    }


    private Response generateResponse(String request) {
        LOG.info("REQUEST:");
        LOG.info(request);

        Map<String,String> soapData = new LinkedHashMap<>();
        soapData.put("responseCode", "0");
        soapData.put("authToken", DUMMY_AUTH_TOKEN);
        soapData.put("partnerChallenge", DUMMY_PARTNER_CHALLENGE);

        String response = generateSoapResponse(LOGIN_PS3_CERT_RESULT, soapData);
        LOG.info("RESPONSE:");
        LOG.info(response);
        return Response.status(Response.Status.OK)
                .entity(response)
                .build();
    }

    private String generateSoapResponse(String type, Map<String, String> soapData)  {
        StringBuilder b = new StringBuilder();
        for(String key : soapData.keySet()) {
            b.append(SoapUtils.createSoapTagWithValue(key, soapData.get(key)));
        }
        return SoapUtils.wrapSoapData("AuthService", type, b.toString());
    }

}
