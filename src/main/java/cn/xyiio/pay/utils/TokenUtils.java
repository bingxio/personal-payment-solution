package cn.xyiio.pay.utils;

public class TokenUtils {

    public static String generateToken() {
        String time = "" + System.currentTimeMillis();

        char[] a = time.toCharArray();

        for (int i = 0; i < a.length; i ++) {
            a[i] = (char) (a[i] ^ 't');
        }

        return new String(a);
    }

}
