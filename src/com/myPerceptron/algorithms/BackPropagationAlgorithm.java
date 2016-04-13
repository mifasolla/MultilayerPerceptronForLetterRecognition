package com.myPerceptron.algorithms;

import com.myPerceptron.perceptron.Layer;
import com.myPerceptron.perceptron.Perceptron;
import com.myPerceptron.utils.ActivationFunction;
import com.myPerceptron.utils.Matrix;

import java.util.Random;

 /* Created by Vika on 15.02.2016.*/

public class BackPropagationAlgorithm {

    private final double ALPHA = new Random().nextDouble() * (-1.0);
    private final double ETA = 0.001;

    private Perceptron perceptron;
    private TrainingSample ts;


    public BackPropagationAlgorithm(Perceptron perceptron, TrainingSample ts) throws Exception {
        this.perceptron = perceptron;
        int layersCount = perceptron.getLayersCount();
        this.ts = ts;
        int trainingSampleSize = ts.size();
        int trainingVectorCount = (int) (trainingSampleSize * 0.8);
        int generalizationVectorCount = trainingSampleSize - trainingVectorCount;
        Matrix desiredResponseVector;

        for (int epoch = 0; epoch < 500; epoch++) {

            ts.shuffle();
            desiredResponseVector = ts.getDesiredResponseVector();

            Matrix[] oldDeltaWeights = new Matrix[layersCount];

            for (int trainingVectorNumber = 0; trainingVectorNumber < trainingVectorCount; trainingVectorNumber++) {

                forwardComputation(trainingVectorNumber);

                double errorSignal = calculateErrorSignal(desiredResponseVector, trainingVectorNumber, layersCount);

                Matrix[] localGradients = backwardComputation(layersCount, errorSignal);

                Matrix[] newDeltaWeights = calculateDeltaWeights(localGradients, trainingVectorNumber);

                changeWeights(oldDeltaWeights, newDeltaWeights, trainingVectorNumber);

                oldDeltaWeights = newDeltaWeights;
            }
            int rightAnswer = 0;

            for (int generalVectorNum = trainingVectorCount; generalVectorNum < trainingSampleSize; generalVectorNum++) {
                forwardComputation(generalVectorNum);

                Layer lastLayer = perceptron.getLayers(layersCount - 1);
                double response = lastLayer.getNeuronOutputs().getElement(0, 0);

                if (getSign(response) == getSign(desiredResponseVector.getElement(generalVectorNum, 0))) {
                    rightAnswer++;
                }
            }
            System.out.println("Epoch " + epoch + " = " + (double)rightAnswer / (double)generalizationVectorCount);

            if ((double)rightAnswer / (double)generalizationVectorCount > 0.8) {
                System.out.println("The perceptron is learned!!!");
                epoch = 1000;
            }
        }
    }

    private void forwardComputation(int trainingVectorIndex) throws Exception {
        Matrix trainingVector = ts.getInputVector(trainingVectorIndex);
        perceptron.calculateOutputs(trainingVector);
    }

    private double calculateErrorSignal(Matrix desiredResponseVector, int vectorNumber, int layersCount) {
        Layer lastLayer = perceptron.getLayers(layersCount - 1);
        return desiredResponseVector.getElement(vectorNumber, 0) - lastLayer.getNeuronOutputs().getElement(0, 0);
    }

    private Matrix[] backwardComputation(int layersCount, double errorSignal) throws Exception {
        Matrix[] localGradients = new Matrix[layersCount];

        for (int layerNum = layersCount - 1; layerNum >= 0; layerNum--) {

            Matrix localGradientVector;
            Layer currentLayer = perceptron.getLayers(layerNum);

            if (layerNum == layersCount - 1) {
                localGradientVector = new Matrix(1, 1);
                localGradientVector.setElement(0, 0, errorSignal);
            } else {
                Layer nextLayer = perceptron.getLayers(layerNum + 1);
                Matrix nextLocalGradientsVector = localGradients[layerNum + 1].getMatrix();
                localGradientVector = nextLocalGradientsVector.multiple(nextLayer.getWeights().cutTheColumn(0));
            }
            localGradientVector.multipleColumnsOnScalarVector(getDerivativeOfActivationFunctionForLayer(currentLayer));

            localGradients[layerNum] = localGradientVector;
        }
        return localGradients;
    }

    private double[] getDerivativeOfActivationFunctionForLayer(Layer layer) {
        double[] activationFunctionDerivative = new double[layer.getNeuronsCount()];
        Matrix outputs = layer.getNeuronOutputs();
        int k = 1;
        if (layer.isLast()) {
            k = 0;
        }
        for (int i = 0; i < activationFunctionDerivative.length; i++) {
            activationFunctionDerivative[i] = ActivationFunction.getDerivativeResult(outputs.getElement(i + k, 0));
        }
        return activationFunctionDerivative;
    }

    private void changeWeights(Matrix[] oldDeltaWeights, Matrix[] newDeltaWeights, int trainingVectorNumber)
            throws Exception {

        if (trainingVectorNumber == 0) {
            for (int layerNum = 0; layerNum < oldDeltaWeights.length; layerNum++) {
                Layer layer = perceptron.getLayers(layerNum);
                layer.getWeights().addInPlace(newDeltaWeights[layerNum]);
            }
        } else {
            for (int layerNum = 0; layerNum < oldDeltaWeights.length; layerNum++) {
                oldDeltaWeights[layerNum].scalarMultiplicationInPlace(ALPHA);
                newDeltaWeights[layerNum].addInPlace(oldDeltaWeights[layerNum]);

                Layer layer = perceptron.getLayers(layerNum);
                layer.getWeights().addInPlace(newDeltaWeights[layerNum]);
            }
        }
    }

    private Matrix[] calculateDeltaWeights(Matrix[] localGradients, int trainingVectorNumber) throws Exception {
        Matrix[] newDeltaWeights = new Matrix[localGradients.length];

        for (int i = newDeltaWeights.length - 1; i >= 0; i--) {
            Matrix transposedLocalGradient;
            Matrix transposedInputs;

            if (i == 0) {
                transposedLocalGradient = localGradients[i].transpose();
                transposedInputs = ts.getInputVector(trainingVectorNumber).transpose();
            } else {
                Layer previousLayer = perceptron.getLayers(i - 1);
                transposedLocalGradient = localGradients[i].transpose();
                transposedInputs = previousLayer.getNeuronOutputs().transpose();
            }
            newDeltaWeights[i] = transposedLocalGradient.multiple(transposedInputs);
            newDeltaWeights[i].scalarMultiplicationInPlace(ETA);
        }
        return newDeltaWeights;
    }

    private int getSign(double value) {
        if (value >= 0) return 1;
        else return -1;
    }
}
