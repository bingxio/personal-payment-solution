package cn.xyiio.pay.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String currentAt() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static int currentDateInt() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String toMonth = "" + month, toDay = "" + day, toHour = "" + hour, toMinute = "" + minute;

        for (int i = 0; i < 9; i ++) {
            if (month == i) toMonth = "0" + month;
            if (day == i) toDay = "0" + day;
            if (hour == i) toHour = "0" + hour;
            if (minute == i) toMinute = "0" + minute;
        }

        return Integer.parseInt("" + year + toMonth + toDay);
    }

    public static int currentDateTimeInt() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String toHour = "" + hour, toMinute = "" + minute;

        for (int i = 0; i < 9; i ++) {
            if (hour == i) toHour = "0" + hour;
            if (minute == i) toMinute = "0" + minute;
        }

        return Integer.parseInt(toHour + toMinute);
    }

}
