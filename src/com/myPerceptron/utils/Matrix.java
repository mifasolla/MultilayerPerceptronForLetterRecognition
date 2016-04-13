package com.myPerceptron.utils;

/**
 * Created by Vika on 24.03.2016.
 */
public class Matrix {

    private double[][] matrix;
    private int N; // row number
    private int M; // column number

    public Matrix(int rowNumber, int columnNumber) {
        N = rowNumber;
        M = columnNumber;
        matrix = new double[N][M];
    }

    public Matrix getMatrix() { // apple.getApple :)
        Matrix copy = new Matrix(this.getRowCount(), this.getColumnCount());

        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                copy.setElement(i, j, this.getElement(i, j));
            }
        }
        return copy;
    }

    public Matrix getVectorFromColumn(int columnNumber) {

        if (columnNumber > this.getColumnCount() - 1) {
            throw new RuntimeException("Column index is " + columnNumber + ", but column count is " + this.getColumnCount());
        }

        Matrix vector = new Matrix(N, 1);

        for (int i = 0; i < N; i++) {
            vector.setElement(i, 0, matrix[i][columnNumber]);
        }
        return vector;
    }

    public void setVerticalVector(double[] vector) {

        if (vector.length != N || M != 1) {
            System.out.println("!!! Vertical vector is impossible.");
            this.N = vector.length;
            this.M = 1;
            matrix = new double[N][M];
        }

        for (int i = 0; i < N; i++) {
            matrix[i][0] = vector[i];
        }
    }

    public void setElement(int rowIndex, int columnIndex, double value) {
        matrix[rowIndex][columnIndex] = value;
    }

    public double getElement(int rowIndex, int columnIndex) {
        return matrix[rowIndex][columnIndex];
    }

    public void addInPlace(Matrix secondMatrix) throws Exception {
        throwDimensionsNotEqualException(secondMatrix);

        doSum(secondMatrix, this);
    }

    public Matrix multiple(Matrix secondMatrix) throws Exception {

        throwExceptionForMultiplication(secondMatrix);

        Matrix resultMatrix = new Matrix(this.getRowCount(), secondMatrix.getColumnCount());
        doMultiplication(secondMatrix, resultMatrix);

        return resultMatrix;
    }

    public void scalarMultiplicationInPlace(double scalar) {
        doScalarMultiplication(scalar, this);
    }

    public void show() {
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                System.out.print(this.getElement(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void multipleColumnsOnScalarVector(double[] scalarVector) {
        if (scalarVector.length != this.getColumnCount()) {
            throw new RuntimeException("Scalar vector has wrong length! Scalar vector has " +
                    scalarVector.length + "elements. Column count is " + this.getColumnCount() + ".");
        }

        for (int i = 0; i < scalarVector.length; i++) {
            this.scalarMultiplicationTheColumnInPlace(scalarVector[i], i);
        }
    }

    public int getRowCount() {
        return matrix.length;
    }

    public int getColumnCount() {
        return matrix[0].length;
    }

    public Matrix transpose() {
        Matrix transposedMatrix = new Matrix(this.getColumnCount(), this.getRowCount());

        for (int i = 0; i < transposedMatrix.getRowCount(); i++) {
            for (int j = 0; j < transposedMatrix.getColumnCount(); j++) {
                transposedMatrix.setElement(i, j, this.getElement(j, i));
            }
        }
        return transposedMatrix;
    }

    public Matrix cutTheColumn(int columnNumber) {
        if (columnNumber > this.getRowCount() - 1) {
            throw new RuntimeException("Row number is larger than row counts of the matrix.");
        }

        Matrix reducedMatrix = new Matrix(this.getRowCount(), this.getColumnCount() - 1);

        for (int i = 0; i < this.getColumnCount(); i++) {
            if (i < columnNumber) {
                reducedMatrix.setColumn(i, this.getColumn(i));
            } else if (i > columnNumber) {
                int j = i - 1;
                reducedMatrix.setColumn(j, this.getColumn(i));
            }
        }
        return reducedMatrix;
    }

    private void scalarMultiplicationTheColumnInPlace(double scalar, int columnNumber) {
        for (int i = 0; i < this.getRowCount(); i++) {
            matrix[i][columnNumber] *= scalar;
        }
    }

    public void setColumn(int columnNumber, double[] columnElements) {
        if (columnNumber > this.getColumnCount() - 1) {
            throw new RuntimeException("Column number is big.");
        }
        if (columnElements.length != this.getRowCount()) {
            throw new RuntimeException("The column has wrong length!");
        }

        for (int i = 0; i < columnElements.length; i++) {
            this.setElement(i, columnNumber, columnElements[i]);
        }
    }

    public void swapColumns(int i, int j) {
        double[] columnI = this.getColumn(i);
        this.setColumn(i, this.getColumn(j));
        this.setColumn(j, columnI);
    }

    public void swapRows(int i, int j) {
        double[] rowI = this.getRow(i);
        this.setRow(i, this.getRow(j));
        this.setRow(j, rowI);
    }

    private double[] getColumn(int columnNumber) {
        double[] column = new double[this.getRowCount()];

        for (int i = 0; i < this.getRowCount(); i++) {
            column[i] = matrix[i][columnNumber];
        }

        return column;
    }

    public void setRow(int rowNumber, double[] rowElements) {
        if (rowNumber > this.getRowCount() - 1) {
            throw new RuntimeException("Row number is big.");
        }
        if (rowElements.length != this.getColumnCount()) {
            throw new RuntimeException("The row has wrong length!");
        }

        for (int j = 0; j < rowElements.length; j++) {
            this.setElement(rowNumber, j, rowElements[j]);
        }
    }

    private double[] getRow(int rowNumber) {
        double[] row = new double[this.getColumnCount()];

        for (int j = 0; j < this.getColumnCount(); j++) {
            row[j] = matrix[rowNumber][j];
        }

        return row;
    }

    private void throwDimensionsNotEqualException(Matrix secondMatrix) throws Exception {
        if (secondMatrix.getRowCount() != this.getRowCount() || secondMatrix.getColumnCount() != this.getColumnCount()) {
            throw new RuntimeException("Illegal dimensions!");
        }
    }

    private void throwExceptionForMultiplication(Matrix secondMatrix) throws Exception {
        if (this.getColumnCount() != secondMatrix.getRowCount()) {
            throw new RuntimeException("Second matrix has wrong row count.");
        }
    }

    private void doSum(Matrix secondMatrix, Matrix resultMatrix) {

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                resultMatrix.setElement(i, j, matrix[i][j] + secondMatrix.getElement(i, j));
            }
        }
    }

    private void doMultiplication(Matrix secondMatrix, Matrix resultMatrix) {

        for (int i = 0; i < resultMatrix.getRowCount(); i++)
            for (int j = 0; j < resultMatrix.getColumnCount(); j++)
                for (int k = 0; k < this.getColumnCount(); k++)

                    resultMatrix.setElement(i, j, resultMatrix.getElement(i, j) +
                            this.getElement(i, k) * secondMatrix.getElement(k, j));
    }

    private void doScalarMultiplication(double scalar, Matrix resultMatrix) {
        for (int i = 0; i < resultMatrix.getRowCount(); i++) {
            for (int j = 0; j < resultMatrix.getColumnCount(); j++) {
                resultMatrix.setElement(i, j, this.getElement(i, j) * scalar);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        } else {
            Matrix other = (Matrix) obj;

            if (getColumnCount() != other.getColumnCount() || getRowCount() != other.getRowCount()) {
                return false;
            } else {
                for (int i = 0; i < this.getRowCount(); i++) {
                    for (int j = 0; j < this.getColumnCount(); j++) {
                        if (matrix[i][j] != other.matrix[i][j]) { // that's better понятно )
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

}