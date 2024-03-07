package me.croshaw.ess.util;

import java.util.Random;

public class Range<T extends Number> {
    private final T min;
    private final T max;

    public Range(T number1, T number2) {
        double val1 = number1.doubleValue();
        double val2 = number2.doubleValue();
        this.min = val1 < val2 ? number1 : number2;
        this.max = val1 > val2 ? number1 : number2;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
    public T getRandom(Random random) {
        double result = random.nextDouble(min.doubleValue(), max.doubleValue()+1);
        return switch (min) {
            case Double v -> (T) Double.valueOf(result);
            case Integer i -> (T) Integer.valueOf((int) result);
            case Float v -> (T) Float.valueOf((float) result);
            default -> throw new UnsupportedOperationException();
        };
    }
}
