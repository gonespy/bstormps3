package com.gonespy.service.util;

import com.gonespy.service.gpcm.GPCMServiceThread;
import com.google.common.base.Strings;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class CertificateUtils {

    // PUBLIC EXPONENT / POWER
    public static final String WS_AUTHSERVICE_SIGNATURE_EXP = "010001";

    // SHARED MODULUS
    // 256 chars = 128 bytes
    public static final String WS_AUTHSERVICE_SIGNATURE_KEY =
            "BF05D63E93751AD4A59A4A7389CF0BE8" +
                    "A22CCDEEA1E7F12C062D6E194472EFDA"+
                    "5184CCECEB4FBADF5EB1D7ABFE911814"+
                    "53972AA971F624AF9BA8F0F82E2869FB"+
                    "7D44BDE8D56EE50977898F3FEE758696"+
                    "22C4981F07506248BD3D092E8EA05C12"+
                    "B2FA37881176084C8F8B8756C4722CDC"+
                    "57D2AD28ACD3AD85934FB48D6B2D2027";

    // don't know what this is :(
    // value here is commented out from AuthService.c - worth a try
    public static final String WS_AUTHSERVICE_SIGNATURE_PRIVATE_EXP = "D589F4433FAB2855F85A4EB40E6311F0" +
	        "284C7882A72380938FD0C55CC1D65F7C" +
            "6EE79EDEF06C1AE5EDE139BDBBAB4219" +
            "B7D2A3F0AC3D3B23F59F580E0E89B9EC" +
            "787F2DD5A49788C633D5D3CE79438934" +
            "861EA68AE545D5BBCAAAD917CE9F5C7C" +
            "7D1452D9214F989861A7511097C35E60" +
            "A7273DECEA71CB5F8251653B26ACE781";

    // PEER KEY
    // 256 chars = 128 bytes
    public static final String PEER_KEY_MODULUS =
            "95375465E3FAC4900FC912E7B30EF717"+
                    "1B0546DF4D185DB04F21C79153CE0918" +
                    "59DF2EBDDFE5047D80C2EF86A2169B05" +
                    "A933AE2EAB2962F7B32CFE3CB0C25E7E" +
                    "3A26BB6534C9CF19640F1143735BD0CE" +
                    "AA7AA88CD64ACEC6EEB037007567F1EC" +
                    "51D00C1D2F1FFCFECB5300C93D6D6A50" +
                    "C1E3BDF495FC17601794E5655C476819";

    // 256 chars = 128 bytes
    public static final String SERVER_DATA =
            "908EA21B9109C45591A1A011BF84A189" +
                    "40D22E032601A1B2DD235E278A9EF131" +
                    "404E6B07F7E2BE8BF4A658E2CB2DDE27" +
                    "E09354B7127C8A05D10BB4298837F965" +
                    "18CCB412497BE01ABA8969F9F46D23EB" +
                    "DE7CC9BE6268F0E6ED8209AD79727BC8" +
                    "E0274F6725A67CAB91AC87022E587104" +
                    "0BF856E541A76BB57C07F4B9BE4C6316";

    public static final String MD5_HEADER_STRING = "3020300C06082A864886F70D020505000410";
    public static final String CRYPTO_PREFIX = "0001";
    public static final String CRYPTO_SEPARATOR_BYTE = "00";

    public static Map<String, String> getCertificate(Map<String, Object> inputData) {
        Map<String, String> certificateData = new LinkedHashMap<>();
        certificateData.put("length", "303");
        certificateData.put("version", (String)inputData.get("version"));
        certificateData.put("partnercode", (String)inputData.get("partnercode"));
        certificateData.put("namespaceid", (String)inputData.get("namespaceid"));
        certificateData.put("userid", GPCMServiceThread.DUMMY_USER_ID);
        certificateData.put("profileid", GPCMServiceThread.DUMMY_PROFILE_ID);
        certificateData.put("expiretime", "0");
        certificateData.put("profilenick", GPCMServiceThread.DUMMY_UNIQUE_NICK);
        certificateData.put("uniquenick", GPCMServiceThread.DUMMY_UNIQUE_NICK);
        certificateData.put("cdkeyhash", "");

        certificateData.put("peerkeymodulus", PEER_KEY_MODULUS); // 256 chars = 128 bytes
        certificateData.put("peerkeyexponent", WS_AUTHSERVICE_SIGNATURE_EXP);
        certificateData.put("serverdata", SERVER_DATA); // 256 chars = 128 bytes

        String md5 = StringUtils.hashCertificate(certificateData);
        String signature = generateSignature(md5);
        certificateData.put("signature", signature);

        return certificateData;
    }

    // // A user's login certificate, signed by the GameSpy AuthService
    //// The certificate is public and may be freely passed around
    //// Avoid use of pointer members so that structure may be easily copied

    // gsi_u8 = unsigned char (0 -> 255)
    // gsi_u32 = l_word = unsigned int

    //typedef struct GSLoginCertificate
    //{
    //	gsi_bool mIsValid;
    //
    //	gsi_u32 mLength;
    //	gsi_u32 mVersion;
    //	gsi_u32 mPartnerCode; // aka Account space
    //	gsi_u32 mNamespaceId;
    //	gsi_u32 mUserId;
    //	gsi_u32 mProfileId;
    //	gsi_u32 mExpireTime;
    //	gsi_char mProfileNick[WS_LOGIN_NICK_LEN];
    //	gsi_char mUniqueNick[WS_LOGIN_UNIQUENICK_LEN];
    //	gsi_char mCdKeyHash[WS_LOGIN_KEYHASH_LEN];       // hexstr - bigendian
    // 	gsCryptRSAKey mPeerPublicKey;
    //	gsi_u8 mSignature[GS_CRYPT_RSA_BYTE_SIZE];   // binary - bigendian // 128
    //	gsi_u8 mServerData[WS_LOGIN_SERVERDATA_LEN]; // binary - bigendian // 128
    //} GSLoginCertificate;
    //

    // typedef struct
    //{
    //	gsLargeInt_t modulus;
    //	gsLargeInt_t exponent;
    //} gsCryptRSAKey;

    // GS_LARGEINT_DIGIT_TYPE = gsi_u32
    // GS_LARGEINT_MAX_DIGITS = 2048 / (4*8) = 64
    // typedef struct gsLargeInt_s
    //{
    //	GS_LARGEINT_DIGIT_TYPE mLength;
    //	GS_LARGEINT_DIGIT_TYPE mData[GS_LARGEINT_MAX_DIGITS];
    //} gsLargeInt_t;


    //// Private information for the owner of the certificate only
    //// -- careful! private key information must be kept secret --
    //typedef struct GSLoginCertificatePrivate
    //{
    //	gsCryptRSAKey mPeerPrivateKey;
    //	char mKeyHash[GS_CRYPT_MD5_HASHSIZE];
    //} GSLoginPrivateData;

    // client reads cert ints like this: (no endian stuff when reading the SOAP)
    // if (gsi_is_false(gsXmlReadChildAsInt      (reader, "length",     (int*)&cert->mLength))      ||

    // strings like this: (null-terminated)
    // gsi_is_false(gsXmlReadChildAsTStringNT (reader, "profilenick", cert->mProfileNick, WS_LOGIN_NICK_LEN))       ||

    // peerkeymodulus / peerkeyexponent - gsLargeIntSetFromHexString implemented in gonespy. hex -> int array weirdness
    //    GS_CRYPT_RSA_BYTE_SIZE = 128  ,  times 2 chars per byte = 256 characters,  plus one for null termination
    // 		gsi_is_false(gsXmlReadChildAsStringNT (reader, "peerkeymodulus", hexstr, GS_CRYPT_RSA_BYTE_SIZE*2 +1)) ||
    //		gsi_is_false(gsLargeIntSetFromHexString(&cert->mPeerPublicKey.modulus, hexstr)) ||

    // serverdata - WS_LOGIN_SERVERDATA_LEN = 128
    // 		gsi_is_false(gsXmlReadChildAsHexBinary(reader, "serverdata", cert->mServerData, WS_LOGIN_SERVERDATA_LEN, &hexlen)) ||

    // signature - WS_LOGIN_SIGNATURE_LEN = 128
    //		gsi_is_false(gsXmlReadChildAsHexBinary(reader, "signature", cert->mSignature, WS_LOGIN_SIGNATURE_LEN, &hexlen))


    // and private exponent
    // 				gsi_is_false(gsXmlReadChildAsLargeInt(theResponseXml, "peerkeyprivate", &response.mPrivateData.mPeerPrivateKey.exponent))

    // peer privatekey modulus is same as peer public key modulus
    // memcpy(&response.mPrivateData.mPeerPrivateKey.modulus, &cert->mPeerPublicKey.modulus, sizeof(cert->mPeerPublicKey.modulus));

    // verify certificate
    // cert->mIsValid = wsLoginCertIsValid(cert);


    private static String generateSignature(String md5) {

        // raw length should be 128 bytes / 256 chars
        // Signature format:
        //      [ 0x00 0x01 0xFF ... 0x00 HashHeader Hash(Data) ]
        // gsi_u8 md5Header[18] =  {0x30,0x20,0x30,0x0C,0x06,0x08,0x2A,0x86,0x48,0x86,0xF7,0x0D,0x02,0x05,0x05,0x00,0x04,0x10};
        // md5 is 32 characters long

        // eg. 256 - (4 + 2 + 36 + 32) = 182 = 91 pairs of FF as padding
        int paddingCount = (
                256 - CRYPTO_PREFIX.length() - CRYPTO_SEPARATOR_BYTE.length() - MD5_HEADER_STRING.length() - md5.length()
        ) / 2 - 22;

        String rawSignature = CRYPTO_PREFIX + Strings.repeat("FF", paddingCount) + CRYPTO_SEPARATOR_BYTE +
                MD5_HEADER_STRING + md5;

        /*
            // "decrypt" the signature
            lintRSASignature.mLength = (l_word)(sigLen / 4);
            memcpy(lintRSASignature.mData, sig, sigLen);
            gsLargeIntReverseBytes(&lintRSASignature);
            gsLargeIntPowerMod(&lintRSASignature, &publicKey->exponent, &publicKey->modulus, &lintRSASignature);
            gsLargeIntReverseBytes(&lintRSASignature);
         */

        GsLargeInt lint = GsLargeInt.gsLargeIntSetFromHexString(rawSignature);

        // TODO: should be encrypting with PRIVATE key - what is it?
        // client decrypts with public key WS_AUTHSERVICE_SIGNATURE_KEY / WS_AUTHSERVICE_SIGNATURE_EXP
        lint.gsLargeIntReverseBytes();
        GsLargeInt encryptedLint = GsLargeInt.encrypt(
                lint,
                GsLargeInt.gsLargeIntSetFromHexString(WS_AUTHSERVICE_SIGNATURE_PRIVATE_EXP),
                GsLargeInt.gsLargeIntSetFromHexString(WS_AUTHSERVICE_SIGNATURE_KEY)
        );
        encryptedLint.gsLargeIntReverseBytes();

        return encryptedLint.toHexString();
    }

    // copied from eaEmu https://github.com/teknogods/eaEmu/blob/master/eaEmu/gamespy/webServices.py
    // for c&c red alert 3 - doesn't seem to work for other games?
    // Possibly because version/partnercode/namespaceid are different, thus affect the signature
    public static Map<String, String> getCertificateEaEmu(Map<String, Object> inputData) {
        Map<String, String> certificateData = new LinkedHashMap<>();
        certificateData.put("length", "303");
        certificateData.put("version", (String)inputData.get("version")); // 1?
        certificateData.put("partnercode", (String)inputData.get("partnercode")); // what is this for c&c ra3?
        certificateData.put("namespaceid", (String)inputData.get("namespaceid")); // what is this for c&c ra3?
        certificateData.put("userid", "11111");
        certificateData.put("profileid", "22222");
        certificateData.put("expiretime", "0");
        certificateData.put("profilenick", "Jackalus");
        certificateData.put("uniquenick", "Jackalus");
        certificateData.put("cdkeyhash", "");
        certificateData.put("peerkeymodulus", PEER_KEY_MODULUS);
        certificateData.put("peerkeyexponent", WS_AUTHSERVICE_SIGNATURE_EXP);
        certificateData.put("serverdata", SERVER_DATA);
        certificateData.put("signature",
                "181A4E679AC27D83543CECB8E1398243113EF6322D630923C6CD26860F265FC031C2C61D4F9D86046C07BBBF9CF86894903BD867E3CB59A0D9EFDADCB34A7FB3CC8BC7650B48E8913D327C38BB31E0EEB06E1FC1ACA2CFC52569BE8C48840627783D7FFC4A506B1D23A1C4AEAF12724DEB12B5036E0189E48A0FCB2832E1FB00");


        return certificateData;
    }
}
