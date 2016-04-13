package com.myPerceptron.perceptron;

import com.myPerceptron.utils.ActivationFunction;
import com.myPerceptron.utils.Matrix;

import java.util.Random;

/**
 * Created by Vika on 13.02.2016.
 */
public class Layer {

    private int neuronsCount;
    private boolean last;
    private Matrix weights;
    private Matrix outputVector;

    public Layer(int neuronsCount, int previousLayerNeuronsCount, boolean last) {

        this.neuronsCount = neuronsCount;
        this.last = last;

        weights = new Matrix(neuronsCount, previousLayerNeuronsCount);
        setRandomWeights();
    }

    public int getNeuronsCount() {
        return neuronsCount;
    }

    public void calculateNeuronOutputs(Matrix inputVector) throws Exception {

        if (inputVector.getColumnCount() > 1) {
            throw new RuntimeException("Incorrect input vector! It has " + inputVector.getColumnCount() +
                    " column count instead 1 column.");
        }

        outputVector = initializeOutputVector();
        Matrix inducedLocalFieldVector = weights.multiple(inputVector);

        double[] neuronOutputs = getActivationFunctionResult(inducedLocalFieldVector);


        if (!last) {
            for (int i = 1; i < outputVector.getRowCount(); i++) {
                outputVector.setElement(i, 0, neuronOutputs[i - 1]);
            }
        } else {
            outputVector.setVerticalVector(neuronOutputs);
        }
    }

    public Matrix getNeuronOutputs() {
        return outputVector;
    }

    public Matrix getWeights() {
        return weights;
    }

    private void setRandomWeights() {
        Random rand = new Random();
        for (int i = 0; i < weights.getRowCount(); i++) {
            for (int j = 0; j < weights.getColumnCount(); j++) {
                weights.setElement(i, j, rand.nextDouble());
            }
        }
    }

    public boolean isLast() {
        return last;
    }

    private Matrix initializeOutputVector() {
        if (!last) {
            outputVector = new Matrix(neuronsCount + 1, 1);
            outputVector.setElement(0, 0, 1);
        } else {
            outputVector = new Matrix(neuronsCount, 1);
        }
        return outputVector;
    }

    private double[] getActivationFunctionResult(Matrix inducedLocalField) {

        double[] neuronOutputs = new double[inducedLocalField.getRowCount()];

        for (int i = 0; i < inducedLocalField.getRowCount(); i++) {
            neuronOutputs[i] = ActivationFunction.getResult(inducedLocalField.getElement(i, 0));
        }

        return neuronOutputs;
    }

}
