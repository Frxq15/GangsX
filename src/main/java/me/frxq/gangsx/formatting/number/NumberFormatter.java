package me.frxq.gangsx.formatting.number;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.ENGLISH);

    public static String format(byte number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(short number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(int number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(long number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(float number) {
        return NUMBER_FORMAT.format(number);
    }

    public static String format(double number) {
        return NUMBER_FORMAT.format(number);
    }

}