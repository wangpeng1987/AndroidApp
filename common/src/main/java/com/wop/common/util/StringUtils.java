package com.wop.common.util;

/**
 * Created by tibbytang on 02/03/2018 8:29 PM.
 * QQ:562980080
 * Wechat:ITnan562980080
 */

public class StringUtils {
    public static String stringToEmoji(String string) {
        StringBuilder builder = new StringBuilder();
        while (string.contains("|")) {
            int position = string.indexOf("|");
            String str1 = string.substring(0, position);
            string = string.substring(position + 1, string.length());
            String regex = "^[A-Fa-f0-9]+$";
            if (str1.matches(regex)) {
                String emoji = EmojiUtils.emojiFromHexString(Integer.parseInt(str1, 16));
                builder.append(emoji);
            } else {
                builder.append(str1);
            }
        }
        builder.append(string);
        return builder.toString();
    }
}
