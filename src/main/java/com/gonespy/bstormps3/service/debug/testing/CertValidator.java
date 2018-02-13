package com.gonespy.bstormps3.service.debug.testing;

import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

public class CertValidator {

    static final byte GS_SSL_HANDSHAKE_SERVERHELLO = 0x02;

    static class GHIBuffer {
        int pos;
        char[] data;

        public GHIBuffer(){}

        public int length() {
            return data.length;
        }
    }

    static class GHIBufferOut {
        char[] data;

        public GHIBufferOut(){}

        public int length() {
            return data.length;
        }
    }

    static class GHIValue {
        int value = 0;

        public GHIValue(int v){ value = v;}

    }

    public static void main(String args[]) {

        String hexString = "16030003b80200004603005a7e6a43f4a4aab2b1c27b181e132a037efc0bdbdb8dd1a542a357520cc97987205a7e6a43e0ec589eae9b8692b92086a94d3a3b58a575738e4feb4e8a8c2990e40004000b0003660003630003603082035c30820244020900be35efcd5788b9a0300d06092a864886f70d01010505003070310b30090603550406130255533113301106035504080c0a43616c69666f726e69613116301406035504070c0d53616e204672616e636973636f3110300e060355040a0c0747616d65737079310c300a060355040b0c035053333114301206035504030c0b67616d657370792e636f6d301e170d3138303231303032333130315a170d3139303231303032333130315a3070310b30090603550406130255533113301106035504080c0a43616c69666f726e69613116301406035504070c0d53616e204672616e636973636f3110300e060355040a0c0747616d65737079310c300a060355040b0c035053333114301206035504030c0b67616d657370792e636f6d30820122300d06092a864886f70d01010105000382010f003082010a0282010100d0025dbfcbd1277cb178781e2000212d529023d44fbf47f3c6b4b79d9eb5037feb6585a7348ca4995351ef94660241e301746184461d39c1f2e52c89301a3cf1a3cb76f4b8470ec0a0eb77a6e2692174ccfbae0faa1fd1008e15db9b5a53d63506649be6408ea3b32540692c38117c0e8054f001f1b6e4bb3f98b82d610d99c57bfe65e8fe9df42cc547b3c770a95fb5eae006638a64fef918444e21ac0bbfae8ad50249e2c1a10dbb644fe043552d28995840c74d14ce5663eed09baef436d05a58c2b4fae4bd54c2c0dff877de84fa3ae5a55c4dbdffc83b87457575a494b0f2d217e7cb2694e5b8129dcfabfd6e224ac825708caa8ba8af0435c7575f10810203010001300d06092a864886f70d01010505000382010100956e9f446bcc17421e0062b5720fa30d5b112a7be2a0ac73dd2f3748852aa5f1b9d3a503d09b341b3d46187e54ca6ab6e93848b8756493158c29f728d316a5a4727a6bfe7553f57e84167db9f545572b37ddbb777ae80f47f3a0e488639f87e15008456c6b815fc69ac449f9b9745461d488216bc4495efaecb1e732d1903f2045eda84d3e6831127cbfacb1e76de307d6320a01249fb039d4d795e78f58626ee042e9e2eb6f44db3da9f700d2595c40581e79475020fe19f7d24d700d780261af59f6dce563959442508d9e2e683703325a4b6a7e65e39c4e503c4e4d5f9eb5533d9f08e8d440b72911bbcb15bd55219c904060224035327f460481461a1a140e000000";

        try {
            char[] byteArray = hexString.toCharArray();

            GHIBuffer buffer = new GHIBuffer();
            buffer.data = byteArray;
            buffer.pos=0;

            //ghiEncryptorProcessSSLHandshake(buffer);
            


        } catch(Exception e) {

        }



    }

    private static void CHECK(boolean b) {
        if(!b) {
            System.out.println("VALIDATION ERROR");
        }
    }

    // Read data from a buffer with a garunteed length
    static boolean ghiReadDataFromBufferFixed
    (
            GHIBuffer bufferIn,    // the GHIBuffer to read from
            GHIBufferOut bufferOut, // the raw buffer to write to
            int         bytesToCopy  // number of bytes to read
    )
    {
        // Verify parameters
        assert(bufferIn != null);
        if (bytesToCopy == 0)
            return true;

        // Make sure the bufferIn isn't too small
        if (bufferIn.length() < bytesToCopy)
            return false;

        // Copy the bytes
        bufferOut.data = Arrays.copyOfRange(bufferIn.data, bufferIn.pos, bufferIn.pos + bytesToCopy);

        // Adjust the bufferIn read position
        bufferIn.pos += bytesToCopy;
        return true;
    }
/*
    static boolean ghiEncryptorReadNBOLength(GHIBuffer data, GHIValue value, int size)
    {
        GHIBufferOut bo = new GHIBufferOut();
        bo.data = new char[] { value.value };
        if (!ghiReadDataFromBufferFixed(data, ((char*)value)+(sizeof(int)-size), size))
            return false;

        return true;
    }

    private static void ghiEncryptorProcessSSLHandshake(GHIBuffer data)
    {
        int pos = 0;

        while(pos < data.length())
        {
            // Parse each SSL handshake message (there may be multiple)
            int  messageStart = pos;
            GHIBufferOut messageTypeB = new GHIBufferOut();
            CHECK(ghiReadDataFromBufferFixed(data, messageTypeB, 1));
            byte messageType = new Byte(new String(messageTypeB.data));

            if (messageType == GS_SSL_HANDSHAKE_SERVERHELLO)
            {
                int totalMsgLen = 0; // length of header + data
                int msgDataLen = 0;  // length of data
                int tempInt = 0;
                char tempChar = '\0';

                CHECK(ghiEncryptorReadNBOLength(data, &msgDataLen, 3));

                // check reported size against the actual bytes remaining
                if (msgDataLen > (data->len - pos))
                    return GHIEncryptionResult_Error; // abort connection

                // skip SSL version
                //    (length check not required because we did that above)
                pos += 2;

                // store server random (used for key generation)
                CHECK(ghiReadDataFromBufferFixed(data, (char*)&sslInterface->serverRandom[0], 32));

                // store session information (length followed by data)
                CHECK(ghiReadDataFromBufferFixed(data, &tempChar, 1));
                CHECK(ghiReadDataFromBufferFixed(data, (char*)sslInterface->sessionData, tempChar));
                sslInterface->sessionLen = (int)tempChar;

                // store cipher suite
                CHECK(ghiEncryptorReadNBOLength(data, &tempInt, 2));
                sslInterface->cipherSuite = (unsigned short)tempInt;

                // skip compression algorithms (should always be 0x00 since we don't support any!)
                CHECK(ghiReadDataFromBufferFixed(data, &tempChar, 1));
                if (tempChar != 0x00)
                    return GHIEncryptionResult_Error;

                // add it to the running handshake hash
                totalMsgLen = pos - messageStart;
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)&data->data[messageStart], (unsigned int)totalMsgLen);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)&data->data[messageStart], (unsigned int)totalMsgLen);
            }
            else if (messageType == GS_SSL_HANDSHAKE_CERTIFICATE)
            {
                int msgLength = 0;    // combined length of the message (size in SSL message header)
                int certListLen = 0;  // combined length of all certificates
                int totalMsgLen = 0;  // our calculated msg length (for handshake hashing)

                int certCount = 0;
                int certListEndPos = 0;

                CHECK(ghiEncryptorReadNBOLength(data, &msgLength, 3));
                CHECK(ghiEncryptorReadNBOLength(data, &certListLen, 3));
                if (msgLength != certListLen + 3)
                    return GHIEncryptionResult_Error;

                // make sure we don't have the certificates already (e.g. dupe message)
                //if (sslInterface->certificateArray != NULL)
                //	return GHIEncryptionResult_Error; // abort connection

                // make sure we have enough data to cover the certificate list 
                certListEndPos = pos + certListLen;
                if (certListLen > (data->len - pos))
                    return GHIEncryptionResult_Error;

                // read the certificates
                while(pos < certListEndPos)
                {
                    int certLength = 0;
                    int certStartPos = 0;

                    int temp = 0;
                    int version = 0;

                    // Must start with a 3 byte length
                    CHECK(ghiEncryptorReadNBOLength(data, &certLength, 3));

                    // Make sure we have enough data to cover this certificate
                    if (certLength > (data->len - pos))
                        return GHIEncryptionResult_Error; // certificate too big

                    // 0xFFFF is max message size in SSL v3.0, we don't currently support
                    // split messages
                    if (certLength > 0xFFFF)
                        return GHIEncryptionResult_Error;

                    certStartPos = pos; // remember this for a shortcut later
                    certCount++;

                    // make a copy of the certificate data
                    //certCopy = gsimalloc(certLength);
                    //if (certCopy == NULL)
                    //{
                    //	gsDebugFormat(GSIDebugCat_HTTP, GSIDebugType_Memory, GSIDebugLevel_WarmError,
                    //		"SSL failed to allocate certificate #%d (out of memory)\r\n", certCount);
                    //	return GHIEncryptionResult_Error;
                    //}
                    //memcpy(certCopy, &data[browsePos], certLength);
                    //ArrayAppend(sslInterface->certificateArray, &certCopy);

                    // The first certificate holds the server's public key
                    if (certCount == 1)
                    {
                        // X.509 format is rather convoluted.  Since we only support
                        // one variation anyways, I'm hardcoding the specific values
                        // we require.  Anything else is a protocol error.
                        //    0x30 marks the start of a sequence.  next byte is a length field size
                        //    0x82 is a length tag, meaning the next two bytes contain the length
                        //    0x81 is the same thing, only the next one byte contains the length
                        //    The other values usually denote required types

                        // Certificate SEQUENCE
                        int seqLen = 0;
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        // todo: verify reported length of this sequence

                        // TBSCertificate SEQUENCE
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        // todo: verify reported length of this sequence

                        // EXPLICIT Version (must be one of: 0x03,0x02,0x01)
                        if (5 > (data->len - pos)) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0xa0) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x03) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x02) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x01) return GHIEncryptionResult_Error;
                        version = (unsigned char)data->data[pos++];

                        // Serial Number (variable length, with 2-byte length field)
                        if ((unsigned char)data->data[pos++] != 0x02) return GHIEncryptionResult_Error;
                        temp = (unsigned char)data->data[pos++]; // len of serial number
                        if (pos + temp > certListEndPos) return GHIEncryptionResult_Error;
                        pos += temp; // skip the serial number

                        // Signature algorithm identifier SEQUENCE 
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        pos += seqLen; // skip algorithm ID (todo: verify signatures)

                        // Issuer Name
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        pos += seqLen; // skip the issuer name sequence

                        // Validity
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        pos += seqLen; // skip the validity sequence

                        // Subject Name
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        pos += seqLen; // skip the subject name sequence

                        // Subject Public Key Info
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        //     AlgorithmIdentifier
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));
                        if (seqLen != 0x0d) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x06) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x09) return GHIEncryptionResult_Error;

                        if (0 != memcmp(&data->data[pos], gsSslRsaOid, sizeof(gsSslRsaOid)))										// { 0x2a, 0x86, 0x48, 0x86, 0xf7, 0x0d, 0x01, 0x01, 0x01 };
                        return GHIEncryptionResult_Error; // only RSA certs are supported
                        pos += sizeof(gsSslRsaOid);
                        if ((unsigned char)data->data[pos++] != 0x05) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x00) return GHIEncryptionResult_Error;



                        //     Bitstring (subject public key)
                        if (2 > (certListEndPos - pos)) return GHIEncryptionResult_Error;
                        if ((unsigned char)data->data[pos++] != 0x03) return GHIEncryptionResult_Error; // bitstring

                        // GOES WRONG HERE

                        if ((unsigned char)data->data[pos++] != 0x81) return GHIEncryptionResult_Error; // 1 byte len field      	// 0x82 !!!
                        if (temp > (certListEndPos - pos)) return GHIEncryptionResult_Error;
                        temp = (unsigned char)data->data[pos++]; // remaining data size (check or ignore)                 		// 0x01 - ignored

                        // 0x0f

                        if ((unsigned char)data->data[pos++] != 0x00) return GHIEncryptionResult_Error;							// 0x00 - OK

                        //     Start of the public key modulus
                        CHECK(ghiEncryptorParseASN1Sequence(data, &seqLen));

                        // Read out the public key modulus
                        if (data->data[pos++] != 0x02) return GHIEncryptionResult_Error; // integer tag							// 0x82 !!!
                        if ((data->data[pos]&0x80)==0x80) // ASN1 variable length field											// 
                        {
                            int lensize = data->data[pos++]&0x7f;
                            if (lensize > 4)
                                return GHIEncryptionResult_Error;
                            temp = 0;
                            while(lensize-- > 0)
                                temp = (temp << 8) | (unsigned char)data->data[pos++];
                        }
                        else
                        {
                            temp = (unsigned char)data->data[pos++];
                        }
                        if (pos + temp > certListEndPos) return GHIEncryptionResult_Error;
                        if (data->data[pos++] != 0x00) return GHIEncryptionResult_Error; // ignore bits must be 0x00				// OK
                        if (temp-1 > GS_LARGEINT_BINARY_SIZE/sizeof(char)) return GHIEncryptionResult_Error;							// GS_LARGEINT_BINARY_SIZE = 256
                        sslInterface->serverpub.modulus.mLength = (unsigned int)((temp-1)/GS_LARGEINT_DIGIT_SIZE_BYTES); //-1 to subtract the previous 0x00 byte
                        gsLargeIntSetFromMemoryStream(&sslInterface->serverpub.modulus, (const gsi_u8*)&data->data[pos], (gsi_u32)temp-1);
                        pos += temp-1;

                        // Read out the public key exponent
                        if (data->data[pos++] != 0x02) return GHIEncryptionResult_Error; // integer								// OK

                        // 0x03

                        if ((data->data[pos]&0x80)==0x80)
                        {
                            int lensize = data->data[pos++]&0x7f;
                            if (lensize > 4)
                                return GHIEncryptionResult_Error;
                            temp = 0;
                            while(lensize-- > 0)
                                temp = (temp << 8) | (unsigned char)data->data[pos++];
                        }
                        else
                        {
                            temp = (unsigned char)data->data[pos++];																// 3
                        }
                        if (pos + temp > certListEndPos) return GHIEncryptionResult_Error;
                        if (temp == 0) return GHIEncryptionResult_Error; // no exponent?
                        if (temp > GS_LARGEINT_BINARY_SIZE/sizeof(char)) return GHIEncryptionResult_Error;
                        sslInterface->serverpub.exponent.mLength = (unsigned int)(((temp-1)/GS_LARGEINT_DIGIT_SIZE_BYTES)+1); // ceiling of temp/4
                        gsLargeIntSetFromMemoryStream(&sslInterface->serverpub.exponent, (const gsi_u8*)&data->data[pos], (gsi_u32)temp);
                        pos += temp;
                    }

                    // update the position
                    pos = certStartPos + certLength;

                    GSI_UNUSED(version);
                }
                if (pos != certListEndPos)
                    return GHIEncryptionResult_Error; // bytes hanging off the end!

                // todo: verify certificate chain
                //       first certificate is the server's, the rest likely belong to CA
                if  (GHTTPFalse == ghiCertificateChainIsValid(sslInterface))
                    return GHIEncryptionResult_Error;

                // add it to the running handshake hash
                totalMsgLen = pos - messageStart;
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)&data->data[messageStart], (unsigned int)totalMsgLen);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)&data->data[messageStart], (unsigned int)totalMsgLen);
            }
            else if (messageType == GS_SSL_HANDSHAKE_SERVERHELLODONE)
            {
                // Process the hello done
                // Respond with 3 messages
                //    ClientKeyExchange
                //    ChangeCipherSpec
                //    Finished (final handshake)
                int i=0;

                gsSSLClientKeyExchangeMsg* clientKeyExchange = NULL;
                gsSSLRecordHeaderMsg* changeCipherSpec = NULL;
                gsSSLRecordHeaderMsg* finalHandshake = NULL;

                unsigned char temp[7];
                unsigned char hashTempMD5[GS_CRYPT_MD5_HASHSIZE];
                unsigned char hashTempSHA1[GS_CRYPT_SHA1_HASHSIZE];
                int tempInt = 0;

                // ServerHelloDone has a zero length data field
                CHECK(ghiEncryptorReadNBOLength(data, &tempInt, 3));
                if (tempInt != 0x00) return GHIEncryptionResult_Error;

                // add it to the running handshake hash
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)&data->data[messageStart], (unsigned int)(pos - messageStart));
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)&data->data[messageStart], (unsigned int)(pos - messageStart));

                // Make sure there is room in the send buffer for the response messages
                tempInt = (int)(sizeof(gsSSLClientKeyExchangeMsg) + sslInterface->serverpub.modulus.mLength*GS_LARGEINT_DIGIT_SIZE_BYTES);
                while (connection->sendBuffer.size - connection->sendBuffer.len < tempInt)
                {
                    // not enough room in send buffer, try to grow it
                    if (GHTTPFalse == ghiResizeBuffer(&connection->sendBuffer, connection->sendBuffer.sizeIncrement))
                    return GHIEncryptionResult_Error;
                }

                // 1) Client key exchange,
                //    create the pre-master-secret
                sslInterface->premastersecret[0] = GS_SSL_VERSION_MAJOR;
                sslInterface->premastersecret[1] = GS_SSL_VERSION_MINOR;
                for (i=2; i<GS_SSL_MASTERSECRET_LEN; i++)
                {
				#if defined(GS_CRYPT_NO_RANDOM)
					#pragma message ("!!!WARNING: SSL Random disable for debug purposes.  SSL not secured!!!")
                    // Use zero as the random so we can packet sniff for debug
                    //   warning! : this compromises the SSL security
                    sslInterface->premastersecret[i] = 0; // rand()
				#else
                    Util_RandSeed(current_time());
                    sslInterface->premastersecret[i] = (unsigned char)(Util_RandInt(0, 0x0100)); // range = [0...FF]
				#endif
                }

                clientKeyExchange = (gsSSLClientKeyExchangeMsg*)&connection->sendBuffer.data[connection->sendBuffer.len];
                connection->sendBuffer.len += sizeof(gsSSLClientKeyExchangeMsg);
                clientKeyExchange->header.contentType = GS_SSL_CONTENT_HANDSHAKE;
                clientKeyExchange->header.versionMajor = GS_SSL_VERSION_MAJOR;
                clientKeyExchange->header.versionMinor = GS_SSL_VERSION_MINOR;
                ghiEncryptorWriteNBOLength(clientKeyExchange->header.lengthNBO, (int)(sslInterface->serverpub.modulus.mLength*GS_LARGEINT_DIGIT_SIZE_BYTES+4), 2);
                clientKeyExchange->handshakeType = GS_SSL_HANDSHAKE_CLIENTKEYEXCHANGE;
                ghiEncryptorWriteNBOLength(clientKeyExchange->lengthNBO, (int)(sslInterface->serverpub.modulus.mLength*GS_LARGEINT_DIGIT_SIZE_BYTES), 3);
                //    encrypt the preMasterSecret using the server's public key (store result in sendbuffer)
                gsCryptRSAEncryptBuffer(&sslInterface->serverpub, sslInterface->premastersecret,
                        GS_SSL_MASTERSECRET_LEN, (unsigned char*)&connection->sendBuffer.data[connection->sendBuffer.len]);
                connection->sendBuffer.len += sslInterface->serverpub.modulus.mLength*GS_LARGEINT_DIGIT_SIZE_BYTES;

                // add it to the running handshake hash
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)clientKeyExchange+sizeof(gsSSLRecordHeaderMsg),
                    sizeof(gsSSLClientKeyExchangeMsg) - sizeof(gsSSLRecordHeaderMsg) +
                            sslInterface->serverpub.modulus.mLength*GS_LARGEINT_DIGIT_SIZE_BYTES);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)clientKeyExchange+sizeof(gsSSLRecordHeaderMsg),
                    sizeof(gsSSLClientKeyExchangeMsg) - sizeof(gsSSLRecordHeaderMsg) +
                            sslInterface->serverpub.modulus.mLength*GS_LARGEINT_DIGIT_SIZE_BYTES);


                // 2) change cipher spec
                changeCipherSpec = (gsSSLRecordHeaderMsg*)&connection->sendBuffer.data[connection->sendBuffer.len];
                changeCipherSpec->contentType = GS_SSL_CONTENT_CHANGECIPHERSPEC;
                changeCipherSpec->versionMajor = GS_SSL_VERSION_MAJOR;
                changeCipherSpec->versionMinor = GS_SSL_VERSION_MINOR;
                changeCipherSpec->lengthNBO[0] = 0;
                changeCipherSpec->lengthNBO[1] = 1; // always one byte length 
                connection->sendBuffer.len += sizeof(gsSSLRecordHeaderMsg);
                connection->sendBuffer.data[connection->sendBuffer.len++] = 0x01; // always set to 0x01
                // DO NOT add it to the running handshake hash (its content is not GS_SSL_CONTENT_HANDSHAKE)

                // Calculate the encryption keys
                ghiEncryptorGenerateEncryptionKeys(sslInterface);

                // 3) final handshake message (encrypted)
                finalHandshake = (gsSSLRecordHeaderMsg*)&connection->sendBuffer.data[connection->sendBuffer.len];
                finalHandshake->contentType = GS_SSL_CONTENT_HANDSHAKE;
                finalHandshake->versionMajor = GS_SSL_VERSION_MAJOR;
                finalHandshake->versionMinor = GS_SSL_VERSION_MINOR;
                finalHandshake->lengthNBO[0] = 0;
                finalHandshake->lengthNBO[1] = 56; // handshake type(1)+handshake lenNBO(3)+SHA1(20)+MD5(16)+MAC(16)
                connection->sendBuffer.len += sizeof(gsSSLRecordHeaderMsg);
                connection->sendBuffer.data[connection->sendBuffer.len++] = GS_SSL_HANDSHAKE_FINISHED;
                ghiEncryptorWriteNBOLength((unsigned char*)&connection->sendBuffer.data[connection->sendBuffer.len], 36, 3);
                connection->sendBuffer.len += 3;


                // MD5(master_secret + pad2 + MD5(handshake_messages+"CLNT"+master_secret+pad1))
                // SHA1(master_secret + pad2 + SHA1(handshake_messages+"CLNT"+master_secret+pad1))
                // prepare the final hashes (inner hashes)
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)GS_SSL_CLIENT_FINISH_VALUE, 4);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)sslInterface->mastersecret, GS_SSL_MASTERSECRET_LEN);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)GS_SSL_PAD_ONE, GS_SSL_MD5_PAD_LEN);
                MD5Final(hashTempMD5, &sslInterface->finishHashMD5);

                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)GS_SSL_CLIENT_FINISH_VALUE, 4);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)sslInterface->mastersecret, GS_SSL_MASTERSECRET_LEN);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)GS_SSL_PAD_ONE, GS_SSL_SHA1_PAD_LEN);
                SHA1Result(&sslInterface->finishHashSHA1, hashTempSHA1);

                // prepare the final hashes (outer hashes)
                MD5Init(&sslInterface->finishHashMD5);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)sslInterface->mastersecret, GS_SSL_MASTERSECRET_LEN);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)GS_SSL_PAD_TWO, GS_SSL_MD5_PAD_LEN);
                MD5Update(&sslInterface->finishHashMD5, hashTempMD5, GS_CRYPT_MD5_HASHSIZE);
                MD5Final(hashTempMD5, &sslInterface->finishHashMD5);

                SHA1Reset(&sslInterface->finishHashSHA1);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)sslInterface->mastersecret, GS_SSL_MASTERSECRET_LEN);
                SHA1Input(&sslInterface->finishHashSHA1, (unsigned char*)GS_SSL_PAD_TWO, GS_SSL_SHA1_PAD_LEN);
                SHA1Input(&sslInterface->finishHashSHA1, hashTempSHA1, GS_CRYPT_SHA1_HASHSIZE);
                SHA1Result(&sslInterface->finishHashSHA1, hashTempSHA1);

                // copy results into the sendbuffer
                memcpy(&connection->sendBuffer.data[connection->sendBuffer.len], hashTempMD5, GS_CRYPT_MD5_HASHSIZE);
                connection->sendBuffer.len += GS_CRYPT_MD5_HASHSIZE;
                memcpy(&connection->sendBuffer.data[connection->sendBuffer.len], hashTempSHA1, GS_CRYPT_SHA1_HASHSIZE);
                connection->sendBuffer.len += GS_CRYPT_SHA1_HASHSIZE;

                // output the message MAC  (hash(MAC_write_secret+pad_2+ hash(MAC_write_secret+pad_1+seq_num+length+content)));
                // Re-using the finishHashMD5 since it has already been allocated
                MD5Init(&sslInterface->finishHashMD5);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)sslInterface->clientWriteMACSecret, GS_CRYPT_MD5_HASHSIZE);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)GS_SSL_PAD_ONE, GS_SSL_MD5_PAD_LEN);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)sslInterface->sendSeqNBO, 8);
                temp[0] = 0x16;
                temp[1] = (unsigned char)((GS_CRYPT_MD5_HASHSIZE+GS_CRYPT_SHA1_HASHSIZE+4)>>8);
                temp[2] = (unsigned char)((GS_CRYPT_MD5_HASHSIZE+GS_CRYPT_SHA1_HASHSIZE+4));
                //temp[1] = (unsigned char)(htons(GS_CRYPT_MD5_HASHSIZE+GS_CRYPT_SHA1_HASHSIZE));
                //temp[2] = (unsigned char)(htons(GS_CRYPT_MD5_HASHSIZE+GS_CRYPT_SHA1_HASHSIZE+4)>>8);
                temp[3] = 0x14; // 20-bytes of data (MD5+SHA1)
                temp[4] = 0x00; // 3-byte length NBO
                temp[5] = 0x00; // ..
                temp[6] = 0x24; // ..
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)&temp, 7);
                MD5Update(&sslInterface->finishHashMD5, hashTempMD5, GS_CRYPT_MD5_HASHSIZE);   // content part 1
                MD5Update(&sslInterface->finishHashMD5, hashTempSHA1, GS_CRYPT_SHA1_HASHSIZE); // content part 2
                MD5Final(hashTempMD5, &sslInterface->finishHashMD5);
                MD5Init(&sslInterface->finishHashMD5); // reset for outer hash
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)sslInterface->clientWriteMACSecret, GS_CRYPT_MD5_HASHSIZE);
                MD5Update(&sslInterface->finishHashMD5, (unsigned char*)GS_SSL_PAD_TWO, GS_SSL_MD5_PAD_LEN);
                MD5Update(&sslInterface->finishHashMD5, hashTempMD5, GS_CRYPT_MD5_HASHSIZE);
                MD5Final(hashTempMD5, &sslInterface->finishHashMD5);

                memcpy(&connection->sendBuffer.data[connection->sendBuffer.len], hashTempMD5, GS_CRYPT_MD5_HASHSIZE);
                connection->sendBuffer.len += GS_CRYPT_MD5_HASHSIZE;

                // increment sequence each time we send a message
                //   ...assume NBO is bigendian for simplicity
                memset(sslInterface->sendSeqNBO, 0, sizeof(sslInterface->sendSeqNBO));
                ghiEncryptorWriteNBOLength(&sslInterface->sendSeqNBO[4], 1, 4);

                // now encrypt the message (not including record header)
                RC4Encrypt(&sslInterface->sendRC4,
                        ((unsigned char*)finalHandshake)+sizeof(gsSSLRecordHeaderMsg),
                    ((unsigned char*)finalHandshake)+sizeof(gsSSLRecordHeaderMsg),
                    56);
            }
            else if (messageType == GS_SSL_HANDSHAKE_FINISHED)
            {
                // process server finished and verify hashes
                gsDebugFormat(GSIDebugCat_HTTP, GSIDebugType_Network, GSIDebugLevel_Notice,
                        "SSL: todo - verify server finished hash\r\n");
                pos = data->len;
            }
            else
            {
                gsDebugFormat(GSIDebugCat_HTTP, GSIDebugType_Network, GSIDebugLevel_WarmError,
                        "SSL received unexpected handshake message type: %d\r\n", messageType);
                return GHIEncryptionResult_Error; // abort connection
            }
        }

        GSI_UNUSED(connection);

        if (pos == data->len)
            return GHIEncryptionResult_Success;
        else
            return GHIEncryptionResult_Error; // too many or too few bytes, protocol error!
    }*/
}
