package com.myPerceptron.perceptron;

import com.myPerceptron.utils.AlertUtils;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Vika on 13.02.2016.
 */
public class Perceptron {

    private int layersCount;
    Integer[] neuronsCountInfo;
    Layer[] layers;

    public Perceptron(int inputSignalLength, ArrayList<Integer> neuronsCountInfo) throws IOException {

        //  1. write to file input com.myPerc.algorithms.data from images

       /* ArrayList<Double> inputSignalList = new ArrayList<Double>();
        inputSignalList.add(1.0);*/

        // 2. read from file input com.myPerc.algorithms.data and write it into arrayList
        // 3. normalize the inputs
        // 4. convert to array

      /*inputSignal = transformListToDoubleArray(inputSignalList);*/

        // 5. create layers by dint of user interaction?

        layersCount = neuronsCountInfo.size();
        layers = new Layer[layersCount];
        this.neuronsCountInfo = new Integer[layersCount];
        neuronsCountInfo.toArray(this.neuronsCountInfo);

        createLayers(inputSignalLength);

        AlertUtils.showAlert("Perceptron is successfully created!", Alert.AlertType.INFORMATION);

    }/*

    public ArrayList<double[][]> getAllWeights() {

        ArrayList<double[][]> allWeights = new ArrayList<>();
        for (int i = 0; i < layersCount; i++) {
            allWeights.add(getLayers(i).getWeights());
        }
        return allWeights;
    }*/

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
/*
    private double[] transformListToDoubleArray(ArrayList<Double> arrayList) {
        double[] array = new double[arrayList.size()];

        for (int i = 0; i < arrayList.size(); i++)
            array[i] = arrayList.get(i).doubleValue();

        return array;
    }*/

    public int getLayersCount() {
        return layersCount;
    }

    public Layer getLayers(int layerNumber) {
        return layers[layerNumber];
    }

}
