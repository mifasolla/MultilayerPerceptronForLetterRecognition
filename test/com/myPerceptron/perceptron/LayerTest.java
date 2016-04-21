package com.myPerceptron.perceptron;

import com.myPerceptron.utils.ActivationFunction;
import com.myPerceptron.utils.Matrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vika on 14.04.2016.
 */
public class LayerTest {

    @Test
    public void testGetNeuronsCount() throws Exception {
        Layer testLayer = new Layer(5, 10, false);

        assertEquals(testLayer.getNeuronsCount(), 5);
    }

    @Test
    public void testCalculateNeuronOutputsOfNotLastLayer() throws Exception {
        Layer testLayer = new Layer(2, 4, false);
        Matrix inputVector = new Matrix(createTwoDimensionalArray(4, 1));
        testLayer.calculateNeuronOutputs(inputVector);
        Matrix output = testLayer.getNeuronOutputs();

        double[][] weights = testLayer.getWeights().getMatrix();
        double[][] input = createTwoDimensionalArray(4, 1);
        double[][] result = new double[3][1];
        result[0][0] = 1;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i + 1][j] += weights[i][k] * input[k][j];
                }
            }
        }

        for (int i = 1; i < result.length; i++) {
            result[i][0] = ActivationFunction.getResult(result[i][0]);
        }

        assertEquals(output, new Matrix(result));
    }

    @Test
    public void testCalculateNeuronOutputsOfLastLayer() throws Exception {
        Layer testLayer = new Layer(2, 4, true);
        Matrix inputVector = new Matrix(createTwoDimensionalArray(4, 1));
        testLayer.calculateNeuronOutputs(inputVector);
        Matrix output = testLayer.getNeuronOutputs();

        double[][] weights = testLayer.getWeights().getMatrix();
        double[][] input = createTwoDimensionalArray(4, 1);
        double[][] result = new double[2][1];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += weights[i][k] * input[k][j];
                }
            }
        }

        for (int i = 0; i < result.length; i++) {
            result[i][0] = ActivationFunction.getResult(result[i][0]);
        }

        assertEquals(output, new Matrix(result));
    }

    @Test
    public void testIsLast() throws Exception {
        Layer layer = new Layer(5, 4, true);
        Layer other = new Layer(4, 5, false);

        assertTrue(layer.isLast());
        assertTrue(!other.isLast());
    }

    private double[][] createTwoDimensionalArray(int rowCount, int columnCount) {
        double[][] twoDimensionalArray = new double[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                twoDimensionalArray[i][j] = (i * columnCount) + j + 1;
            }
        }

        return twoDimensionalArray;
    }

}