package com.devtau.popularmovies.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.devtau.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public abstract class Util {
    private static final String LOG_TAG = Util.class.getSimpleName();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
    public static final SimpleDateFormat theMovieDBDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

    public static void loadImageToView(Context context, String imagePath, ImageView target,
                                 int resizeImageWidthTo, int resizeImageHeightTo) {
        if (TextUtils.isEmpty(imagePath) || "".equals(imagePath)) {
            Logger.e(LOG_TAG, "No posterUrlString found in movie. Replacing with Kitty");
            imagePath = "http://kogteto4ka.ru/wp-content/uploads/2012/04/%D0%9A%D0%BE%D1%82%D0%B5%D0%BD%D0%BE%D0%BA.jpg";
        } else {
            imagePath = Constants.IMAGE_STORAGE_BASE_URL + Constants.POSTER_SIZE + imagePath;
        }
        Picasso.with(context)
                .load(imagePath)
                .error(R.drawable.load_failed)
                .centerCrop()
                .resize(resizeImageWidthTo, resizeImageHeightTo)
                .into(target);
    }

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
