package com.myPerceptron.algorithms;

import com.myPerceptron.perceptron.Layer;
import com.myPerceptron.perceptron.Perceptron;
import com.myPerceptron.utils.ActivationFunction;
import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.Matrix;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Random;

 /* Created by Vika on 15.02.2016.*/

public class BackPropagationAlgorithm {

    private final double ALPHA = new Random().nextDouble() * (-0.001);
    private final double ETA = 0.001;

    private Perceptron perceptron;
    private TrainingSample ts;
    private ArrayList<Double> generalizationError;
    private ArrayList<Double> trainingError;


    public BackPropagationAlgorithm(Perceptron perceptron, TrainingSample ts) throws Exception {
        this.perceptron = perceptron;
        int layersCount = perceptron.getLayersCount();
        this.ts = ts;
        generalizationError = new ArrayList<>();
        trainingError = new ArrayList<>();
        int trainingSampleSize = ts.size();
        int trainingVectorCount = (int) (trainingSampleSize * 0.8);
        int generalizationVectorCount = trainingSampleSize - trainingVectorCount;
        Matrix desiredResponseVector;
        int target = 0;

        for (int epoch = 0; epoch < 500; epoch++) {

            ts.shuffle();
            desiredResponseVector = ts.getDesiredResponseVector();
            int trainingRightAnswersCount = 0;

            for (int trainingVectorNumber = 0; trainingVectorNumber < trainingVectorCount; trainingVectorNumber++) {

                forwardComputation(trainingVectorNumber);

                Layer lastLayer = perceptron.getLayers(layersCount - 1);
                double response = lastLayer.getNeuronOutputs().getElement(0, 0);
                if (getSign(response) == getSign(desiredResponseVector.getElement(trainingVectorNumber, 0))) {
                    trainingRightAnswersCount++;
                }

                double errorSignal = desiredResponseVector.getElement(trainingVectorNumber, 0) - response;

                Matrix[] localGradients = getLocalGradients(layersCount, errorSignal);

                Matrix[] newDeltaWeights = calculateDeltaWeights(localGradients, trainingVectorNumber);

                changeWeights(newDeltaWeights);
            }
            int generalRightAnswersCount = 0;

            for (int generalVectorNum = trainingVectorCount; generalVectorNum < trainingSampleSize; generalVectorNum++) {
                forwardComputation(generalVectorNum);

                Layer lastLayer = perceptron.getLayers(layersCount - 1);
                double response = lastLayer.getNeuronOutputs().getElement(0, 0);

                if (getSign(response) == getSign(desiredResponseVector.getElement(generalVectorNum, 0))) {
                    generalRightAnswersCount++;
                }
            }

            generalizationError.add(100.0 - ((double) generalRightAnswersCount / (double) generalizationVectorCount)*100.0);
            trainingError.add(100.0 - ((double) trainingRightAnswersCount / (double) trainingVectorCount)*100.0);

            if ((double) generalRightAnswersCount / (double) generalizationVectorCount > 0.8) {
                target++;
                if (target > 50) {
                    break;
                }
            }
        }
    }

    public double[] getGeneralizationError() {
        return convertToArray(generalizationError);
    }

    public double[] getTrainingError() {
        return convertToArray(trainingError);
    }

    private void forwardComputation(int trainingVectorIndex) throws Exception {
        Matrix trainingVector = ts.getInputVector(trainingVectorIndex);
        perceptron.calculateOutputs(trainingVector);
    }

    private Matrix[] getLocalGradients(int layersCount, double errorSignal) throws Exception {
        Matrix[] localGradients = new Matrix[layersCount];

        for (int layerNum = layersCount - 1; layerNum >= 0; layerNum--) {

            Matrix localGradientVector;
            Layer currentLayer = perceptron.getLayers(layerNum);

            if (layerNum == layersCount - 1) {
                localGradientVector = new Matrix(1, 1);
                localGradientVector.setElement(0, 0, errorSignal);
            } else {
                Layer nextLayer = perceptron.getLayers(layerNum + 1);
                Matrix nextLocalGradientsVector = localGradients[layerNum + 1].copy();
                localGradientVector = nextLocalGradientsVector.multiple(nextLayer.getWeights().cutColumn(0));
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

    private void changeWeights(Matrix[] newDeltaWeights)
            throws Exception {

            for (int layerNum = 0; layerNum < newDeltaWeights.length; layerNum++) {
                Layer layer = perceptron.getLayers(layerNum);
                Matrix newWeights = layer.getWeights();
                newWeights.addInPlace(newDeltaWeights[layerNum]);
                layer.setWeights(newWeights);
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

    private double[] convertToArray(ArrayList<Double> arrayList) {
        double[] array = new double[arrayList.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }
}
