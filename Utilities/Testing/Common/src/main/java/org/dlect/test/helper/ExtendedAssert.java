/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper;

import org.junit.Assert;

/**
 *
 * @author lee
 */
public class ExtendedAssert extends Assert {

    /**
     * Verify that <code>smaller &lt; larger</code> and the reverse <code>larger &lt; smaller</code>
     *
     * @param <T>
     * @param larger
     * @param smaller
     */
    public static <T extends Comparable<T>> void assertComparason(T smaller, T larger) {
        int cmp = smaller.compareTo(larger);

        if (cmp >= 0) {
            fail("The (smaller)value [" + smaller + "] was " + (cmp == 0 ? "equal to" : "larger(" + cmp + ") than") + " the (larger)value [" + larger + "]");
        }
        
        cmp = larger.compareTo(smaller);
        if (cmp <= 0) {
            fail("The (larger)value [" + larger + "] was " + (cmp == 0 ? "equal to" : "smaller(" + cmp + ") than") + " the (smaller)value [" + smaller + "]");
        }
    }

    /**
     * Verify that <code>first == second</code> and the reverse <code>second == first</code>
     *
     * @param <T>
     * @param first
     * @param second
     */
    public static <T extends Comparable<T>> void assertEqualsComparason(T first, T second) {
        int cmp = first.compareTo(second);

        if (cmp != 0) {
            fail("The (first)value [" + first + "] was " + (cmp < 0 ? "smaller" : "larger") + "(" + cmp + ") than the (second)value [" + second + "]");
        }
        cmp = second.compareTo(first);
        if (cmp != 0) {
            fail("The (second)value [" + second + "] was " + (cmp < 0 ? "smaller" : "larger") + "(" + cmp + ") than the (first)value [" + first + "]");
        }
    }

}
