package com.integrasolusi.pusda.intranet.utils;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 9:55:04 PM
 */
public class ArrayHelperTest {
    @Test
    public void listToArray2D() {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 21; i++)
            list.add(i);

        Object[][] array = ArrayHelper.listToArray2D(list, 4);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + "  ");
            }
            System.out.println();
        }
    }
}
