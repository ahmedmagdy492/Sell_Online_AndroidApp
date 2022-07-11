package com.magdyradwan.sellonline.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeConverter {
    public static int convertToHours(long timeStamp) {
        Date timeD = new Date(timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.ENGLISH);
        return Integer.parseInt(sdf.format(timeD));
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR);
    }
}
