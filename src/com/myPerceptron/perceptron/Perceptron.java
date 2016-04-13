package com.myPerceptron.perceptron;

import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.Matrix;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Vika on 13.02.2016.
 */
public class Perceptron {

    private int layersCount;
    private Integer[] neuronsCountInfo;
    private Layer[] layers;
    private int inputSignalLength;

    public Perceptron(int inputSignalLength, ArrayList<Integer> neuronsCountInfo) throws IOException {

        //  1. write to file input com.myPerc.algorithms.data from images

       /* ArrayList<Double> inputSignalList = new ArrayList<Double>();
        inputSignalList.add(1.0);*/

        // 2. read from file input com.myPerc.algorithms.data and write it into arrayList
        // 3. normalize the inputs
        // 4. convert to array

      /*inputSignal = transformListToDoubleArray(inputSignalList);*/

        // 5. create layers by dint of user interaction?

        this.inputSignalLength = inputSignalLength;
        layersCount = neuronsCountInfo.size();
        layers = new Layer[layersCount];
        this.neuronsCountInfo = new Integer[layersCount];
        neuronsCountInfo.toArray(this.neuronsCountInfo);

        createLayers(inputSignalLength);

        AlertUtils.showAlert("Perceptron is successfully created!", Alert.AlertType.INFORMATION);

    }

    public void calculateOutputs(Matrix inputSignal) throws Exception {
        if (inputSignal.getColumnCount() != 1) {
            throw new RuntimeException("Illegal input signal dimensions. Input signal has " +
                    inputSignal.getColumnCount() + " columns.");
        }

        if (inputSignal.getRowCount() != inputSignalLength) {
            throw new RuntimeException("Illegal input signal dimensions. Input signal has " +
                    inputSignal.getRowCount() + " rows.");
        }

        for (int layer = 0; layer < layersCount; layer++) {
            if (layer == 0) {
                getLayers(layer).calculateNeuronOutputs(inputSignal);
            } else {
                getLayers(layer).calculateNeuronOutputs(getLayers(layer - 1).getNeuronOutputs());
            }
        }
/*
        System.out.print("Output = " );
        getLayers(getLayersCount() - 1).getNeuronOutputs().show();*/
    }

    private void createLayers(int inputSignalLength) throws IOException {
        int neuronsCount;

        for (int i = 0; i < layersCount; i++) {
            neuronsCount = neuronsCountInfo[i];
            final boolean LAST = true;

            if (i == 0) {
                layers[i] = new Layer(neuronsCount, inputSignalLength, !LAST);
            } else {
                if (i != layersCount - 1) {
                    layers[i] = new Layer(neuronsCount, layers[i - 1].getNeuronsCount() + 1, !LAST);
                } else {
                    layers[i] = new Layer(neuronsCount, layers[i - 1].getNeuronsCount() + 1, LAST);
                }
            }
        }
    }

    public int getLayersCount() {
        return layersCount;
    }

    public Layer getLayers(int layerNumber) {
        return layers[layerNumber];
    }

}
