package me.croshaw.ess.util;

public class NumberHelper {
    public static boolean hasNumber(int number, int condition) {
        if(condition > 9 || condition < 0) {
            return false;
        }
        while(number > 0)
        {
            if(number % 10 == condition)
                return true;
            number=number/10;
        }
        return false;
    }
    public static double round(double number, int c) {
        int divider = (int) Math.pow(10, c);
        return (double)(int) (number * divider) / divider;
    }
    public static float round(float number, int c) {
        int divider = (int) Math.pow(10, c);
        return (float)(int) (number * divider) / divider;
    }

    public static double[][] merge(double[][] ... matrices) {
        int rows = matrices[0].length;
        int cols = matrices[0][0].length;
        double[][] result = new double[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                for (double[][] matrix : matrices) {
                    result[i][j] += matrix[i][j];
                }
                result[i][j] = NumberHelper.round(result[i][j], 2);
            }
        }
        return result;
    }
    public static double[][] matrixMinus(double[][] ... matrices) {
        int rows = matrices[0].length;
        int cols = matrices[0][0].length;
        double[][] result = new double[rows][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                for (double[][] matrix : matrices) {
                    result[i][j] -= matrix[i][j];
                }
                result[i][j] = NumberHelper.round(result[i][j], 2);
            }
        }
        return result;
    }
    public static double[][] matrixReplaceMax(double[][] matrices, double max) {
        for(int i = 0; i < matrices.length; i++) {
            for(int j = 0; j < matrices[i].length; j++) {
                matrices[i][j] = Math.max(matrices[i][j], max);
            }
        }
        return matrices;
    }
}
