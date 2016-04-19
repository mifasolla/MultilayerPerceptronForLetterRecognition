package com.myPerceptron.utils;

/**
 * Created by Vika on 08.04.2016.
 */
public class ActivationFunction {

    //private static final double A = 1.7159;
    private static final double A = 1;
    //private static final double B = 0.66;
    private static final double B = 1;


    private ActivationFunction() {
    }

    public static double getResult(double value) {
        return A * Math.tanh(B * value);
    }

    /**
     * Неявная производная.
     * Производная берётся не по значению переменной, а по результатам функции в значении переменной.
     * Т.е. сначала считаем функцию от значения, а потом подставляем результат в метод и он посчитает производную от
     * значения через результат функции: phi'(y) = (B/A)*(A - y)*(A + y), where y(v) = phi(v).
     */

    public static double getDerivativeResult(double functionValue) {
        return (B / A )* ((A - functionValue) * (A + functionValue));
    }

    public static double getA() {
        return A;
    }

    public static double getB() {
        return B;
    }
}
