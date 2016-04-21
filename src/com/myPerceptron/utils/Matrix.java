package com.myPerceptron.utils;

/**
 * Created by Vika on 24.03.2016.
 */
public class Matrix {

    private double[][] matrix;

    public Matrix(int rowNumber, int columnNumber) {
        matrix = new double[rowNumber][columnNumber];
    }

    public Matrix(double[][] matrix) {
        this.matrix = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, matrix[i].length);
        }
    }

    public void setElement(int rowIndex, int columnIndex, double value) { // tested
        matrix[rowIndex][columnIndex] = value;
    }

    public double getElement(int rowIndex, int columnIndex) {
        return matrix[rowIndex][columnIndex];
    } //tested

    public double[][] getMatrix() { //tested
        double[][] matrixCopy = new double[getRowCount()][getColumnCount()];

        for (int i = 0; i < matrixCopy.length; i++) {
            System.arraycopy(matrix[i], 0, matrixCopy[i], 0, matrix[i].length);
        }

        return matrixCopy;
    }

    public int getRowCount() {
        return matrix.length;
    } // tested

    public int getColumnCount() {
        return matrix[0].length;
    } // tested

    public Matrix copy() { // apple.getApple :)
        Matrix copyObject = new Matrix(this.getRowCount(), this.getColumnCount());

        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                copyObject.setElement(i, j, this.getElement(i, j));
            }
        }
        return copyObject;
    }

    public void setVerticalVector(double[] vector) {
        if (getColumnCount() != 1) {
            throw new RuntimeException("Matrix has more than 1 column.");
        }

        if (vector.length != getRowCount()) {
            throw new RuntimeException("Vertical vector has wrong elements count.");
        }

        for (int i = 0; i < getRowCount(); i++) {
            matrix[i][0] = vector[i];
        }
    } // tested

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
    } // tested

    public void setColumn(int columnNumber, double[] columnElements) {
        if (columnNumber > this.getColumnCount() - 1 || columnNumber < 0) {
            throw new RuntimeException("Column number is wrong.");
        }
        if (columnElements.length != this.getRowCount()) {
            throw new RuntimeException("The column has wrong length!");
        }

        for (int i = 0; i < columnElements.length; i++) {
            this.setElement(i, columnNumber, columnElements[i]);
        }
    } // tested

    public void setMatrix(Matrix other) {
        if (other.getRowCount() != getRowCount() || other.getColumnCount() != getColumnCount()) {
            throw new RuntimeException("Illegal dimensions");
        }

        for (int i = 0; i < getRowCount(); i++) {
            System.arraycopy(other.matrix[i], 0, matrix[i], 0, other.matrix[i].length);
        }
    } // tested

    public Matrix getVectorFromColumn(int columnNumber) {

        if (columnNumber > this.getColumnCount() - 1 || columnNumber < 0) {
            throw new RuntimeException("Column index is " + columnNumber + ", but column count is " + this.getColumnCount());
        }

        Matrix vector = new Matrix(getRowCount(), 1);

        for (int i = 0; i < getRowCount(); i++) {
            vector.setElement(i, 0, matrix[i][columnNumber]);
        }
        return vector;
    } // tested

    public void addInPlace(Matrix secondMatrix) throws Exception {
        throwDimensionsNotEqualException(secondMatrix);

        doSum(secondMatrix, this);
    } // tested

    public Matrix multiple(Matrix secondMatrix) throws Exception {

        throwExceptionForMultiplication(secondMatrix);

        Matrix resultMatrix = new Matrix(this.getRowCount(), secondMatrix.getColumnCount());
        doMultiplication(secondMatrix, resultMatrix);

        return resultMatrix;
    } // tested

    public void scalarMultiplicationInPlace(double scalar) {
        doScalarMultiplication(scalar, this);
    } // tested

    public void multipleColumnsOnScalarVector(double[] scalarVector) {
        if (scalarVector.length != this.getColumnCount()) {
            throw new RuntimeException("Scalar vector has wrong length! Scalar vector has " +
                    scalarVector.length + "elements. Column count is " + this.getColumnCount() + ".");
        }

        for (int i = 0; i < scalarVector.length; i++) {
            this.scalarMultiplicationTheColumnInPlace(scalarVector[i], i);
        }
    } // tested

    public Matrix transpose() {
        Matrix transposedMatrix = new Matrix(this.getColumnCount(), this.getRowCount());

        for (int i = 0; i < transposedMatrix.getRowCount(); i++) {
            for (int j = 0; j < transposedMatrix.getColumnCount(); j++) {
                transposedMatrix.setElement(i, j, this.getElement(j, i));
            }
        }
        return transposedMatrix;
    } // tested

    public Matrix cutColumn(int columnNumber) {
        if (columnNumber > this.getColumnCount() - 1 || columnNumber < 0) {
            throw new RuntimeException("Illegal column index");
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
    } // tested

    public Matrix cutRow(int rowNumber) {
        if (rowNumber > this.getRowCount() - 1 || rowNumber < 0) {
            throw new RuntimeException("Illegal row index");
        }

        Matrix reducedMatrix = new Matrix(this.getRowCount() - 1, this.getColumnCount());

        for (int i = 0; i < this.getRowCount(); i++) {
            if (i < rowNumber) {
                reducedMatrix.setRow(i, this.getRow(i));
            } else if (i > rowNumber) {
                int j = i - 1;
                reducedMatrix.setRow(j, this.getRow(i));
            }
        }
        return reducedMatrix;
    } // tested

    public void swapColumns(int i, int j) {
        // need to add checking for i and j
        double[] columnI = this.getColumn(i);
        this.setColumn(i, this.getColumn(j));
        this.setColumn(j, columnI);
    } // tested

    public void swapRows(int i, int j) {
        // need to add checking for i and j
        double[] rowI = this.getRow(i);
        this.setRow(i, this.getRow(j));
        this.setRow(j, rowI);
    } // tested

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
                        if (matrix[i][j] != other.matrix[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    public boolean isNull() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) return false;
            }
        }
        return true;
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

    @Override
    public String toString() {
        StringBuilder matrixString = new StringBuilder("");

        for (int i = 0; i < matrix.length; i++) {
            StringBuilder line = new StringBuilder("");
            for (int j = 0; j < matrix[i].length - 1; j++) {
                line.append(matrix[i][j] + " ");
            }
            line.append(matrix[i][matrix[i].length - 1]);
            if (i < matrix.length - 1) {
                matrixString.append(line + "\n");
            } else {
                matrixString.append(line);
            }
        }
        return matrixString.toString();
    }


    private void scalarMultiplicationTheColumnInPlace(double scalar, int columnNumber) {
        for (int i = 0; i < this.getRowCount(); i++) {
            matrix[i][columnNumber] *= scalar;
        }
    }

    private double[] getColumn(int columnNumber) {
        double[] column = new double[this.getRowCount()];

        for (int i = 0; i < this.getRowCount(); i++) {
            column[i] = matrix[i][columnNumber];
        }

        return column;
    }

    private double[] getRow(int rowNumber) {
        double[] row = new double[this.getColumnCount()];
        System.arraycopy(matrix[rowNumber], 0, row, 0, matrix[rowNumber].length);

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

        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
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

}