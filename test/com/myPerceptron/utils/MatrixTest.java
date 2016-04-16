package com.myPerceptron.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vika on 12.04.2016.
 */
public class MatrixTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testSetElement() throws Exception {
        Matrix test = new Matrix(3, 5);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                test.setElement(i, j, ((i * 5) + j + 1));
            }
        }
        double[][] testMatrix = test.getMatrix();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(testMatrix[i][j], ((i * 5) + j + 1), 0.001);
            }
        }
    } // marked

    @Test
    public void testGetElement() throws Exception {
        double[][] twoDimensionalArray = createTwoDimensionalArray(3, 5);
        Matrix test = new Matrix(twoDimensionalArray);

        for (int i = 0; i < twoDimensionalArray.length; i++) {
            for (int j = 0; j < twoDimensionalArray[i].length; j++) {
                assertEquals(test.getElement(i, j), twoDimensionalArray[i][j], 0.001d);
            }
        }
    } // marked

    @Test
    public void testGetMatrix() {
        Matrix testObject = new Matrix(createTwoDimensionalArray(3, 5));
        double[][] testMatrix = createTwoDimensionalArray(3, 5);
        double[][] testMatrixGetter = testObject.getMatrix();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(testMatrix[i][j], testMatrixGetter[i][j], 0.001);
            }
        }
    } // marked

    @Test
    public void testGetRowCount() throws Exception {
        Matrix test = new Matrix(2, 100);

        assertEquals(test.getRowCount(), 2);
    } // marked

    @Test
    public void testGetColumnCount() throws Exception {
        Matrix test = new Matrix(100, 101);

        assertEquals(test.getColumnCount(), 101);
    } // marked

    @Test
    public void testSetVerticalVectorThrowColumnException() {
        Matrix matrix = new Matrix(5, 2);
        double[] vectorArray = {1, 2, 3, 4, 5};

        exception.expect(RuntimeException.class);
        matrix.setVerticalVector(vectorArray);
    } // marked

    @Test
    public void testSetVerticalVectorThrowVectorLengthException() {
        Matrix matrix = new Matrix(5, 2);
        double[] vectorArray = {1, 2, 3, 4};

        exception.expect(RuntimeException.class);
        matrix.setVerticalVector(vectorArray);
    } // marked

    @Test
    public void testSetVerticalVector() {
        Matrix matrix = new Matrix(5, 1);
        double[] vectorArray = {1, 2, 3, 4, 5};

        matrix.setVerticalVector(vectorArray);
    } // marked

    @Test
    public void testSetRowThrowsWrongRowIndexException() {
        Matrix test = new Matrix(3, 2);
        double[] testRow = {1, 2};

        exception.expect(RuntimeException.class);
        test.setRow(4, testRow);
    } // marked

    @Test
    public void testSetRowThrowsWrongRowLengthException() {
        Matrix test = new Matrix(3, 2);
        double[] testRow = {1, 2, 4, 5};

        exception.expect(RuntimeException.class);
        test.setRow(1, testRow);
    } // marked

    @Test
    public void testSetRow() {
        Matrix test = new Matrix(3, 2);
        double[] testRow = {1, 2};

        test.setRow(1, testRow);
    } // marked

    @Test
    public void testSetColumnThrowsWrongColumnIndexException() {
        Matrix test = new Matrix(4, 10);
        double[] testColumn = {1, 2, 3, 4};

        exception.expect(RuntimeException.class);
        test.setColumn(-1, testColumn);
    } // marked

    @Test
    public void testSetColumnThrowsWrongColumnLengthException() {
        Matrix test = new Matrix(4, 10);
        double[] testColumn = {1, 2, 3, 4, 5};

        exception.expect(RuntimeException.class);
        test.setColumn(2, testColumn);
    } // marked

    @Test
    public void testSetColumn() {
        Matrix test = new Matrix(4, 10);
        double[] testColumn = {1, 2, 3, 4};

        test.setColumn(1, testColumn);
    } // marked

    @Test
    public void testSetMatrixException() {
        Matrix test = new Matrix(3, 4);
        Matrix other = new Matrix(2, 3);

        exception.expect(RuntimeException.class);
        test.setMatrix(other);
    } // marked

    @Test
    public void testSetMatrix() {
        Matrix test = new Matrix(3, 4);
        Matrix other = new Matrix(createTwoDimensionalArray(3, 4));

        test.setMatrix(other);
        double[][] testMatrix = test.getMatrix();
        double[][] otherMatrix = other.getMatrix();

        for (int i = 0; i < testMatrix.length; i++) {
            for (int j = 0; j < testMatrix[i].length; j++) {
                assertEquals(testMatrix[i][j], otherMatrix[i][j], 0.0001);
            }
        }
    } // marked

    @Test
    public void testGetVectorFromColumnThrowsException() {
        Matrix test = new Matrix(createTwoDimensionalArray(5, 3));

        exception.expect(RuntimeException.class);
        test.getVectorFromColumn(3);
    } // marked

    @Test
    public void testGetVectorFromColumn() {
        Matrix test = new Matrix(createTwoDimensionalArray(5, 3));

        Matrix testVector = new Matrix(5, 1);
        testVector.setElement(0, 0, 1);
        testVector.setElement(1, 0, 4);
        testVector.setElement(2, 0, 7);
        testVector.setElement(3, 0, 10);
        testVector.setElement(4, 0, 13);

        assertEquals(test.getVectorFromColumn(0), testVector);
    } // marked

    @Test
    public void testAddInPlace() throws Exception { // и тут самое интересное )
        Matrix test = new Matrix(2, 2);
        test.setElement(0, 0, 1.0d);
        test.setElement(0, 1, 2.0d);
        test.setElement(1, 0, 3.0d);
        test.setElement(1, 1, 4.0d);

        Matrix other = new Matrix(2, 2); // better to have distinct names anyway, otherwise mistakes like that happen
        other.setElement(0, 0, 2.0d);
        other.setElement(0, 1, 3.0d);
        other.setElement(1, 0, 4.0d);
        other.setElement(1, 1, 5.0d);

        Matrix correct = new Matrix(2, 2);
        correct.setElement(0, 0, 3.0d);
        correct.setElement(0, 1, 5.0d);
        correct.setElement(1, 0, 7.0d);
        correct.setElement(1, 1, 9.0d);

        test.addInPlace(other);
        assertEquals(test, correct);
    } // marked

    @Test
    public void testMultiple() throws Exception {
        Matrix test = new Matrix(createTwoDimensionalArray(2, 3));
        Matrix other = new Matrix(createTwoDimensionalArray(3, 1));
        Matrix result = test.multiple(other);
        Matrix control = new Matrix(2, 1);
        control.setElement(0, 0, 14);
        control.setElement(1, 0, 32);

        assertEquals(result, control);
        assertEquals(test, new Matrix(createTwoDimensionalArray(2, 3)));
        assertEquals(other, new Matrix(createTwoDimensionalArray(3, 1)));

    } //  marked

    @Test
    public void testScalarMultiplicationInPlace() {
        double[][] matrix = createTwoDimensionalArray(2, 3);
        Matrix test = new Matrix(matrix);
        double scalar = 0.5;

        test.scalarMultiplicationInPlace(scalar);
        double[][] testMatrix = test.getMatrix();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(testMatrix[i][j], matrix[i][j] * scalar, 0.0001);
            }
        }
    } // marked

    @Test
    public void testMultipleColumnsOnScalarVectorException() {
        Matrix test = new Matrix(createTwoDimensionalArray(2, 3));
        double[] scalarVector = {1, 2};

        exception.expect(RuntimeException.class);
        test.multipleColumnsOnScalarVector(scalarVector);
    } // marked

    @Test
    public void testMultipleColumnsOnScalarVector() {
        Matrix test = new Matrix(createTwoDimensionalArray(2, 3));
        double[] scalarVector = {3, 2, -1};

        double[][] matrix = test.getMatrix();
        test.multipleColumnsOnScalarVector(scalarVector);
        double[][] multipliedMatrix = test.getMatrix();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                assertEquals(multipliedMatrix[i][j], matrix[i][j] * scalarVector[j], 0.0001);
            }
        }
    } // marked

    @Test
    public void testTranspose() throws Exception {
        Matrix test = new Matrix(createTwoDimensionalArray(3, 4));
        Matrix transposedTest = test.transpose();
        double[][] matrix = test.getMatrix();
        double[][] transposedMatrix = transposedTest.getMatrix();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(transposedMatrix[j][i], matrix[i][j], 0.0001);
            }
        }

    } // marked

    @Test
    public void testCutColumnException() {
        Matrix test = new Matrix(createTwoDimensionalArray(3, 2));

        exception.expect(RuntimeException.class);
        Matrix cutTest = test.cutColumn(5);
    } // marked

    @Test
    public void testCutColumn() {
        Matrix test = new Matrix(createTwoDimensionalArray(3, 5));
        Matrix cutTest = test.cutColumn(2);
        double[][] testMatrix = test.getMatrix();
        double[][] cutTestMatrix = cutTest.getMatrix();

        double[][] controlMatrix = new double[3][4];
        for (int j = 0; j < 5; j++) {
            if (j < 2) {
                for (int i = 0; i < 3; i++)
                    controlMatrix[i][j] = testMatrix[i][j];
            } else if (j > 2) {
                for (int i = 0; i < 3; i++) {
                    controlMatrix[i][j - 1] = testMatrix[i][j];
                }
            }
        }

        for (int i = 0; i < cutTestMatrix.length; i++) {
            for (int j = 0; j < cutTestMatrix[0].length; j++) {
                assertEquals(controlMatrix[i][j], cutTestMatrix[i][j], 0.0001);
            }
        }


    } // marked

    @Test
    public void testSwapColumns() {
        Matrix test = new Matrix(createTwoDimensionalArray(2, 4));
        double[][] testMatrix = test.getMatrix();
        test.swapColumns(1, 3);
        double[][] result = test.getMatrix();

        double[][] control = createTwoDimensionalArray(2, 4);
        control[0][1] = testMatrix[0][3];
        control[1][1] = testMatrix[1][3];
        control[0][3] = testMatrix[0][1];
        control[1][3] = testMatrix[1][1];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(result[i][j], control[i][j], 0.0001);
            }
        }
    } // marked

    @Test
    public void testSwapRows() {
        Matrix test = new Matrix(createTwoDimensionalArray(5, 1));
        test.swapRows(2, 4);
        double[][] result = test.getMatrix();
        double[][] control = createTwoDimensionalArray(5, 1);
        control[2][0] = 5;
        control[4][0] = 3;

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(result[i][j], control[i][j], 0.0001);
            }
        }
    } // marked

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