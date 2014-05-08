package com.integrasolusi.pusda.intranet.utils;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 9:50:25 PM
 */
public class ArrayHelper {

    public static Object[][] listToArray2D(List source, int column) {
        int row = (int) Math.ceil((double) source.size() / (double) column);

        Object[][] array = new Object[row][column];
        for (int i = 0; i < source.size(); i++)
            array[i / column][i % column] = source.get(i);
        return array;
    }
}
