package org.frequency.frequencyapi.util;

public class EmailHelper {

    public static String extractNameFromEmail(String emailString) {
        int index = emailString.indexOf('@');
        if (index == -1) {
            return emailString;
        }
        return emailString.substring(0, index);
    }
}
