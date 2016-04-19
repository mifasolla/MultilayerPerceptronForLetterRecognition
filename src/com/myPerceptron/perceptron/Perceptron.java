package com.myPerceptron.perceptron;

import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.Matrix;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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

    public Perceptron(File weightsInfo) throws FileNotFoundException {
        if (!weightsInfo.exists()) {
            throw new FileNotFoundException("Info file does not exist. Check file path.");
        }

        Matrix[] perceptronWeights = parseFile(weightsInfo);
        this.layersCount = perceptronWeights.length;
        this.layers = new Layer[layersCount];

        createLayers(perceptronWeights);
        AlertUtils.showAlert("Perceptron is download", Alert.AlertType.INFORMATION);
        this.inputSignalLength = layers[0].getWeights().getColumnCount();

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

    private void createLayers(Matrix[] weightsInfo) {
        boolean last = true;
        for (int i = 0; i < weightsInfo.length; i++) {
            if (i < weightsInfo.length - 1) {
                layers[i] = new Layer(weightsInfo[i], !last);
            } else {
                layers[i] = new Layer(weightsInfo[i], last);
            }
        }
    }

    public int getLayersCount() {
        return layersCount;
    }

    public Layer getLayers(int layerNumber) {
        return layers[layerNumber];
    }

    /**
     *
     * @param weightsInfo Txt file, where consists information about weights.
     *                    Matrices of weights in file are written in descending layer order, from last layer to first.
     * @return Array with matrices of layers weights in ascending layer order.
     * @throws FileNotFoundException
     */

    private Matrix[] parseFile(File weightsInfo) throws FileNotFoundException {
        ArrayList<Matrix> perceptronWeights = new ArrayList<>();
        Scanner fileScanner = new Scanner(weightsInfo);
        fileScanner.useDelimiter("[/n]");
        String line = "";
        ArrayList<String> infoLines = null;

        while (fileScanner.hasNextLine()) {
            line = fileScanner.nextLine();
            if (line.contains("Layer")) {
                if (infoLines != null) {
                    Matrix weights = parseLines(infoLines);
                    perceptronWeights.add(weights);
                }

                infoLines = new ArrayList<>();
                line = fileScanner.nextLine();
            }
            infoLines.add(line);
        }
        perceptronWeights.add(parseLines(infoLines));

        Matrix[] allWeights = new Matrix[perceptronWeights.size()];
        int rightOrder = allWeights.length - 1;
        for (int i = 0; i < allWeights.length; i++) {
            allWeights[i] = perceptronWeights.get(rightOrder);
            rightOrder--;
        }
        return allWeights;
    }

    private Matrix parseLines(ArrayList<String> infoLines) {
        int rowCount = infoLines.size();
        String[] lineSplitArray = infoLines.get(0).split(" ");
        int columnCount = lineSplitArray.length;
        double[][] weights = new double[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            String[] lineSplit = infoLines.get(i).split(" ");
            for (int j = 0; j < lineSplit.length; j++) {
                lineSplit[j] = lineSplit[j].replace(',', '.');
                weights[i][j] = Double.valueOf(lineSplit[j]);
            }
        }

        return new Matrix(weights);
    }

}
