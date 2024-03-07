package me.croshaw.ess.util;

public class NumberHelper {
    public static double round(double number, int c) {
        int divider = (int) Math.pow(10, c);
        return (double)(int) (number * divider) / divider;
    }
    public static float round(float number, int c) {
        int divider = (int) Math.pow(10, c);
        return (float)(int) (number * divider) / divider;
    }
}
