package com.myPerceptron.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vika on 12.04.2016.
 */
public class MatrixTest {

    @Test
    public void testSetElement() throws Exception {
        Matrix test = new Matrix(2, 2);
        test.setElement(0, 0, 1.0d);
        test.setElement(1, 1, 2.0d);

        assertEquals(test.getElement(0, 0), 1.0d, 0.001d); // и вот тут нужен доступ к массиву, потому что мы надеемся, что getElement тоже работает правильно
        assertEquals(test.getElement(1, 1), 2.0d, 0.001d);
    }

    @Test
    public void testGetElement() throws Exception {
        Matrix test = new Matrix(2, 2); // а тут нужно создание из массива, потому что мы тестируем setElement опять тоже
        test.setElement(0, 0, 1.0d); // короче эти оба теста получились неправильные из-за этого, но идея должна быть понятна, я думаю
        test.setElement(1, 1, 2.0d); // они зависят друг от друга, а нужно-то только один метод тестировать

        assertEquals(test.getElement(0, 0), 1.0d, 0.001d);
        assertEquals(test.getElement(1, 1), 2.0d, 0.001d);
    }

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
    }

    @Test
    public void testGetRowCount() throws Exception {
        Matrix test = new Matrix(2, 100);

        assertEquals(test.getRowCount(), 2);
    }

    @Test
    public void testGetColumnCount() throws Exception {
        Matrix test = new Matrix(100, 101);

        assertEquals(test.getColumnCount(), 101);
    }

    @Test
    public void testTranspose() throws Exception {
        // don't want to implement right now // empty tests will pass by default
    }
}