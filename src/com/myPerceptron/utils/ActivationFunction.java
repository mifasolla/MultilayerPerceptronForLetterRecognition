package com.myPerceptron.utils;

/**
 * Created by Vika on 08.04.2016.
 */
public class ActivationFunction {

    private static final double A = 1.7159;
    private static final double B = 0.66;


    private ActivationFunction() {
    }

    public static double getResult(double value) {
        return calculate(value);
    }

    public static double getDerivativeResult(double value) {
        return calculateDerivative(value);
    }

    private static double calculate(double value) {
        return A * Math.tanh(B * value);
    }

    private static double calculateDerivative (double value) {
       return B / A * (A - value) * (A + value);
    }
}
