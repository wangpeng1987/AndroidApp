package com.wop.common.util;

/**
 * Created by tibbytang on 31/10/2017 12:27 PM.
 * QQ:562980080
 * Wechat:ITnan562980080
 */

public class EmojiUtils {
//    /**
//     * 将emoji转换为十六进制字符串
//     *
//     * @param emojiText
//     * @return
//     */
//    public static String emojiToHexString(String emojiText) {
//        String str = EmojiParser.parseToHtmlHexadecimal(emojiText);
//        return str.replace("&#", "0");
//    }

    /**
     * 将十六进制字符串转换为emoji
     *
     * @param emoji
     * @return
     */
    public static String emojiFromHexString(int emoji) {
        int[] a = new int[]{emoji};
        return new String(a, 0, a.length);
    }
}
