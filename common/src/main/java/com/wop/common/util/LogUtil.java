package com.wop.common.util;

import android.util.Log;


import com.wop.common.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author woniu
 * @title LogUtil
 * @description
 * @since 2018/1/31 上午11:23
 */
public final class LogUtil {

    private static final String TAG = "Boo_Log";
    private static final int JSON_INDENT = 4;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private LogUtil() {
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Log.d(tag, buildPrintStr(msg));
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void dFormat(String tag, String msg, Object... args) {
        if (BuildConfig.LOG_DEBUG) {
            Log.d(tag, buildPrintFormatStr(msg, args));
        }
    }

    public static void dFormat(String msg, Object... args) {
        dFormat(TAG, msg, args);
    }

    public static void i(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Log.i(tag, buildPrintStr(msg));
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void iFormat(String tag, String msg, Object... args) {
        if (BuildConfig.LOG_DEBUG) {
            Log.i(tag, buildPrintFormatStr(msg, args));
        }
    }

    public static void iFormat(String msg, Object... args) {
        iFormat(TAG, msg, args);
    }

    public static void w(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Log.w(tag, buildPrintStr(msg));
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void wFormat(String tag, String msg, Object... args) {
        if (BuildConfig.LOG_DEBUG) {
            Log.w(tag, buildPrintFormatStr(msg, args));
        }
    }

    public static void wFormat(String msg, Object... args) {
        wFormat(TAG, msg, args);
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(tag, buildPrintStr(msg));
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void eFormat(String tag, String msg, Object... args) {
        if (BuildConfig.LOG_DEBUG) {
            Log.e(tag, buildPrintFormatStr(msg, args));
        }
    }

    public static void eFormat(String msg, Object... args) {
        eFormat(TAG, msg, args);
    }

    public static void json(String tag, String json) {
        printJson(tag, json);
    }

    public static void json(String json) {
        printJson(TAG, json);
    }

    private static String buildPrintStr(String msg) {
        return new StringBuilder()
                .append("╔═══════════════════════════════════════════════════════════════════════════════════════")
                .append("\n║ thread name: ")
                .append(Thread.currentThread().getName())
                .append("\n║------------------------------------")
                .append("\n║ ")
                .append(msg)
                .append("\n╚═══════════════════════════════════════════════════════════════════════════════════════")
                .toString();

    }

    private static String buildPrintFormatStr(String format, Object... args) {
        return buildPrintStr(String.format(format, args));
    }

    private static void printJson(String tag, String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        message = LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }

    public static String formatJson(String jsonStr) {
        String message;
        try {
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                message = jsonObject.toString(JSON_INDENT);
            } else if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                message = jsonArray.toString(JSON_INDENT);
            } else {
                message = jsonStr;
            }
        } catch (JSONException e) {
            message = jsonStr;
        }
        message = LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line)
                    .append("\n");
        }
        return builder.toString();
    }

}
