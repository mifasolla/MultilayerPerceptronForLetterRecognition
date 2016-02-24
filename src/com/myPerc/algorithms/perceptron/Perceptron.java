package com.myPerc.algorithms.perceptron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Vika on 13.02.2016.
 */
public class Perceptron {

    private int layersCount;
    Layer[] layers;


    public Perceptron(double [] inputSignal) throws IOException {

        //  1. write to file input com.myPerc.algorithms.data from images

       /* ArrayList<Double> inputSignalList = new ArrayList<Double>();
        inputSignalList.add(1.0);*/

        // 2. read from file input com.myPerc.algorithms.data and write it into arrayList
        // 3. normalize the inputs
        // 4. convert to array

      /*inputSignal = transformListToDoubleArray(inputSignalList);*/

        // 5. create layers by dint of user interaction?

        System.out.println("Layers count = ");
        layersCount = readNumber();
        layers = new Layer[layersCount];
        createLayers(inputSignal);


    }

    private int readNumber() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int number;
        try {
            number = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return number;
    }

    private void createLayers(double [] inputSignal) throws IOException {
        int neuronsCount;

        for (int i = 0; i < layersCount; i++) {
            System.out.println("Enter neurons count in the " + i + 1 + " layer: ");
            neuronsCount = readNumber();
            if (i == 0) {
               layers[i] = new Layer(i + 1, neuronsCount, inputSignal);
            } else {
                layers[i] = new Layer(i + 1, neuronsCount, layers[i - 1].getNeuronOutputs());
            }

        }
    }

    private double[] transformListToDoubleArray(ArrayList<Double> arrayList) {
        double [] array = new double[arrayList.size()];

        for(int i = 0; i < arrayList.size(); i++)
            array[i] = arrayList.get(i).doubleValue();

        return array;
    }

    public int getLayersCount() {
        return  layersCount;
    }

    public Layer getLayers(int layerNumber) {
        return layers[layerNumber];
    }

}
