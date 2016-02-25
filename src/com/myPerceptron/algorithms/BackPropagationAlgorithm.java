package com.myPerceptron.algorithms;

import com.myPerceptron.perceptron.Perceptron;

import java.util.Random;

/**
 * Created by Vika on 15.02.2016.
 */
public class BackPropagationAlgorithm {

    final double ALPHA = new Random().nextDouble()*(-1.0);

    Perceptron perceptron;

    public BackPropagationAlgorithm(Perceptron perceptron, double [] desiredOutput) {
        this.perceptron = perceptron;


    }



}
