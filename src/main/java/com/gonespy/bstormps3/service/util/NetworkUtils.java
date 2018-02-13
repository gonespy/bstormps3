package com.gonespy.bstormps3.service.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class NetworkUtils {

    private static String MESSAGE_DELIMITER = "\\";
    private static String REGEX_ESCAPED_MESSAGE_DELIMITER = "\\\\";
    private static String MESSAGE_TERMINATOR = MESSAGE_DELIMITER + "final" + MESSAGE_DELIMITER;

    private static Pattern DIRECTIVE_PATTERN = Pattern.compile("^" + REGEX_ESCAPED_MESSAGE_DELIMITER + "([^" +
            REGEX_ESCAPED_MESSAGE_DELIMITER + "]*)" + REGEX_ESCAPED_MESSAGE_DELIMITER);

    public static String readGPMessage(InputStream reader) throws IOException {
        StringBuilder msg = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read;
        while((read = reader.read(buffer))!=-1) {
            String bytesRead = new String(buffer, 0, read);
            //System.out.println("READ: " + bytesRead);
            msg.append(bytesRead);
            if(bytesRead.endsWith(MESSAGE_TERMINATOR)) {
                //System.out.println("FINAL - stopping");
                break;
            }
        }
        return msg.toString();
    }

    public static String getGPDirective(String s) {
        Matcher m = DIRECTIVE_PATTERN.matcher(s);
        if(m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static String createGPMessage(Map<String, String> data) {
        StringBuilder b = new StringBuilder();
        for(String key : data.keySet()) {
            b.append(MESSAGE_DELIMITER).append(key).append(MESSAGE_DELIMITER).append(data.get(key));
        }
        b.append(MESSAGE_TERMINATOR);
        return b.toString();
    }

    public static String createGPEmptyListMessage(String key) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(key, "0");
        map.put("list", "");
        return createGPMessage(map);
    }

    public static Map<String, String> parseClientLogin (String clientLoginString) {
        Map<String, String> map = new HashMap<>();
        // remove directive (leading '\xxx\\')
        String str = clientLoginString.replaceFirst(
                REGEX_ESCAPED_MESSAGE_DELIMITER + "[^" + REGEX_ESCAPED_MESSAGE_DELIMITER + "]*" +
                        REGEX_ESCAPED_MESSAGE_DELIMITER + REGEX_ESCAPED_MESSAGE_DELIMITER,
                ""
        );
        String[] parts = str.split(REGEX_ESCAPED_MESSAGE_DELIMITER);
        for(int i = 0; i < parts.length-1; i += 2) {
            map.put(parts[i], parts[i + 1]);
        }
        return map;
    }
}
