package com.myPerceptron.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Vika on 12.04.2016.
 */
public class ActivationFunctionTest {

    @Test
    public void testGetResult() throws Exception {
        double A = ActivationFunction.getA();
        double B = ActivationFunction.getB();
        double value = 0.5;

        double result = A * (Math.tanh(B * value)); // 0.462117, when A = B = 1

        assertEquals(ActivationFunction.getResult(value), result, 0.0001);
    }

    @Test
    public void testGetDerivativeResult() throws Exception {
        double A = ActivationFunction.getA();
        double B = ActivationFunction.getB();
        double value = 0.5;
        double functionResult = A * (Math.tanh(B * value));
        double result = A * B * (Math.pow(1/Math.cosh(B *  value), 2));

        assertEquals(result, ActivationFunction.getDerivativeResult(functionResult), 0.0001);
    }
}