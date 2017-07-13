package com.ziyawang.ziyadaily.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 牛海丰 on 2017/7/10.
 */

public class TimeChange {
    public static String dateToWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "星期日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "星期六";
        }
        return Week;
    }
}
