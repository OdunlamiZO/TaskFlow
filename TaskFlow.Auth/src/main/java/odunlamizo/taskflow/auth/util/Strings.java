package odunlamizo.taskflow.auth.util;

import java.security.SecureRandom;
import java.util.Random;

public final class Strings {

    private Strings() {}

    public static String random() {
        return random(12);        
    }

    public static String random(int length) {
        char[] chars = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        };
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new SecureRandom();
        for(int i = 0; i < length; i++) {
            char _char = chars[random.nextInt(chars.length)];
            stringBuilder.append(_char);
        }
        return stringBuilder.toString();
    }
    
}
