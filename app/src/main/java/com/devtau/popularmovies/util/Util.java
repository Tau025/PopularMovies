package com.devtau.popularmovies.util;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class Util {
    private static final String LOG_TAG = Util.class.getSimpleName();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
    public static final SimpleDateFormat theMovieDBDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    public static void logDate(String dateName, Calendar dateToLog, Context context){
        Locale locale = context.getResources().getConfiguration().locale;
        String log = String.format(locale, "%02d.%02d.%04d %02d:%02d:%02d", dateToLog.get(Calendar.DAY_OF_MONTH),
                dateToLog.get(Calendar.MONTH) + 1, dateToLog.get(Calendar.YEAR), dateToLog.get(Calendar.HOUR_OF_DAY),
                dateToLog.get(Calendar.MINUTE), dateToLog.get(Calendar.SECOND));
        if (dateName.length() >= 20) {
            Logger.d(LOG_TAG, dateName + log);
        } else {
            while (dateName.length() < 20) dateName += '.';
            Logger.d(LOG_TAG, dateName + log);
        }
    }

    //работает только с числом десятичных знаков 0-5
    public static double roundResult(double value, int decimalSigns) {
        if (decimalSigns < 0 || decimalSigns > 5) {
            Logger.d(LOG_TAG, "decimalSigns meant to be bw 0-5. Request is: " + String.valueOf(decimalSigns));
            if (decimalSigns < 0) decimalSigns = 0;
            if (decimalSigns > 5) decimalSigns = 5;
        }
        double multiplier = Math.pow(10.0, (double) decimalSigns);//всегда .0
        long numerator  = Math.round(value * multiplier);
        return numerator / multiplier;
    }

    public static int generateInt(int from, int to) {
        to -= from;
        return from + (int) (Math.random() * ++to);
    }
}
