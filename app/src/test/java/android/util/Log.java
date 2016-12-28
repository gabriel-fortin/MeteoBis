package android.util;

import java.util.Locale;

/**
 * Created by Gabriel Fortin
 */

public class Log {

    public static int v(String tag, String msg) {
        System.out.println(String.format(Locale.UK, "%s: %s", tag, msg));
        return 42;
    }

    public static int v(String tag, String msg, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static int d(String tag, String msg) {
        System.out.println(String.format(Locale.UK, "%s: %s", tag, msg));
        return 42;
    }

    public static int d(String tag, String msg, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static int i(String tag, String msg) {
        System.out.println(String.format(Locale.UK, "%s: %s", tag, msg));
        return 42;
    }

    public static int i(String tag, String msg, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static int w(String tag, String msg) {
        System.out.println(String.format(Locale.UK, "%s: %s", tag, msg));
        return 42;
    }

    public static int w(String tag, String msg, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isLoggable(String var0, int var1) {
        return true;
    }

    public static int w(String tag, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static int e(String tag, String msg) {
        System.out.println(String.format(Locale.UK, "%s: %s", tag, msg));
        return 42;
    }

    public static int e(String tag, String msg, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static int wtf(String tag, String msg) {
        System.out.println(String.format(Locale.UK, "%s: %s", tag, msg));
        return 42;
    }

    public static int wtf(String tag, Throwable tr) {
        throw new RuntimeException("Stub!");
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        throw new RuntimeException("Stub!");
    }
}
