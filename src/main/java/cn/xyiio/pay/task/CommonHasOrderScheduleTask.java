package cn.xyiio.pay.task;

import cn.xyiio.pay.listener.CommonHasOrderScheduleListener;
import cn.xyiio.pay.utils.DateUtils;

public class CommonHasOrderScheduleTask {

    public static boolean hasRun = false;

    public static void configureTask(CommonHasOrderScheduleListener commonHasOrderScheduleListener) {
        int time = DateUtils.currentDateTimeInt();

        hasRun = true;

        while (hasRun) {
            if (DateUtils.currentDateTimeInt() - time >= 5) {
                commonHasOrderScheduleListener.OnTaskRunningEnd();

                hasRun = false;
            }
        }
    }
}
