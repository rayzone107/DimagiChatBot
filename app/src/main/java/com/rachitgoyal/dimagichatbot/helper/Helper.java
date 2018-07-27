package com.rachitgoyal.dimagichatbot.helper;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Helper {


    public static String convertLongToFormattedTime(long time) {

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";

        Calendar messageTime = Calendar.getInstance();
        messageTime.setTimeInMillis(time);

        Calendar now = Calendar.getInstance();

        if (now.get(Calendar.DATE) == messageTime.get(Calendar.DATE)) {
            return DateFormat.format(timeFormatString, messageTime).toString();
        } else {
            return DateFormat.format(dateTimeFormatString, messageTime).toString();
        }
    }
}
