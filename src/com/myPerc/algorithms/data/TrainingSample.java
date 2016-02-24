package com.myPerc.algorithms.data;

/**
 * Created by Vika on 15.02.2016.
 */
public class TrainingSample {

    int vectorLength;

    double [] inputVector;
    double [] desiredResponseVector;

    public TrainingSample(int vectorLength, double[] inputVector, double[] desiredResponseVector) {
        this.vectorLength = vectorLength;
        this.inputVector = inputVector;
        this.desiredResponseVector = desiredResponseVector;
    }

    public double [] getInputVector() {
        return inputVector;
    }

    public double [] getDesiredResponseVector() {
        return desiredResponseVector;
    }
}
