package com.myPerc.algorithms.perceptron;

import java.util.Random;

/**
 * Created by Vika on 13.02.2016.
 */
public class Layer {

    private int neuronsCount;
    private int layerNum;
    double [][] weights;
    private double [] neuronOutputs;
    final double A = 1.7159;
    final double B = 0.66;



    public Layer(int layerNum, int neuronsCount, double [] previousNeuronsOutputs) {

        this.layerNum = layerNum;
        this.neuronsCount = neuronsCount;

        int previousLayerNeuronsCount = previousNeuronsOutputs.length;

        weights = new double[neuronsCount][previousLayerNeuronsCount ];
        setWeights();

        neuronOutputs = calculateNeuronOutputs(previousNeuronsOutputs);

    }

    public int getNeuronsCount(){
        return neuronsCount;
    }

    public int getLayerNum(){
        return layerNum;
    }

    private void setWeights() {
        Random rand = new Random();
        for (int i = 0; i < weights.length; i++){
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = rand.nextDouble();
            }
        }
    }

    private double [] calculateNeuronOutputs(double [] inputVector) {
        double [] outputVector = new double[neuronsCount + 1];
        outputVector[0] = 1.0;
        for (int i = 0; i < weights.length; i++) {
            double v = 0;
            for (int j = 0; j < weights[i].length; j++) {
                v += inputVector[j] * weights[i][j];
            }
            // output = activation function of v
            outputVector[i+1] = A * Math.tanh(B*v);
        }

        return outputVector;
    }

    public double [] getNeuronOutputs() {
        return neuronOutputs;
    }



}
