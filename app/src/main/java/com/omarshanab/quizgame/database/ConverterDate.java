package com.omarshanab.quizgame.database;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConverterDate {
    @TypeConverter
    public static Date longToData(long date) {
        return new Date(date);
    }

    @TypeConverter
    public static long dataToLong(Date date) {
        return date == null ? Calendar.getInstance().getTimeInMillis() : date.getTime();
    }

    public static String toStringSimpleDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(date);
    }
}
