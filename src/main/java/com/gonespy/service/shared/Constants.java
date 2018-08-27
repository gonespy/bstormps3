package com.gonespy.service.shared;

import com.google.common.base.Strings;

public abstract class Constants {
    public static final int STRING_AUTH_LENGTH = 32;

    public static final String DUMMY_AUTH_TOKEN = Strings.padEnd("", STRING_AUTH_LENGTH, '1');
    public static final String DUMMY_PARTNER_CHALLENGE = Strings.padEnd("", STRING_AUTH_LENGTH, '2');

    // taken from eaEmu
    public static final String PEER_KEY_PRIVATE =
            "8818DA2AC0E0956E0C67CA8D785CFAF3" +
                    "A11A9404D1ED9A6E580EA8569E087B75" +
                    "316B85D77B2208916BE2E0D37C7D7FD1" +
                    "8EFD6B2E77C11CDA6E1B689BF460A40B" +
                    "BAF861D800497822004880024B4E7F98" +
                    "A020B1896F536D7219E67AB24B17D60A" +
                    "7BDD7D42E3501BB2FA50BB071EF7A80F" +
                    "29870FFD7C409C0B7BB7A8F70489D04D";

}
