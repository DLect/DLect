/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.migration.version;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.dlect.test.helper.ExtendedAssert.*;

/**
 *
 * @author lee
 */
@SuppressWarnings("unchecked")
public class VersionTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullVersionList() {
        List<Integer> n = null;
        new Version(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NullVersionArray() {
        int[] n = null;
        new Version(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyVersionList() {
        List<Integer> n = ImmutableList.of();
        new Version(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyVersionArray() {
        new Version();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeVersionArray() {
        new Version(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeVersionArray2() {
        new Version(1, 2, 2, 2, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeVersionList() {
        List<Integer> n = ImmutableList.of(-1);
        new Version(n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NegativeVersionList2() {
        List<Integer> n = ImmutableList.of(1, 2, 3, 4, 5, 3, 2, -1);
        new Version(n);
    }

    /**
     * Test of compareTo method, of class Version.
     */
    @Test
    public void testCompareTo_SameVersion() {
        Version v3 = new Version(3);
        assertEqualsComparason(v3, v3);
    }

    /**
     * Test of compareTo method, of class Version.
     */
    @Test
    public void testCompareTo_SameVersionWithZeros() {
        Version v3 = new Version(3);
        Version v30 = new Version(3, 0, 0, 0, 0, 0);
        assertEqualsComparason(v3, v30);
    }

    /**
     * Test of compareTo method, of class Version.
     */
    @Test
    public void testCompareTo_SlightlyLarger() {
        Version v3 = new Version(3);
        Version v301 = new Version(3, 0, 0, 0, 0, 0, 1);
        assertComparason(v3, v301);
    }

    /**
     * Test of compareTo method, of class Version.
     */
    @Test
    public void testCompareTo_Larger() {
        Version v3 = new Version(3);
        Version v31 = new Version(3, 1);
        assertComparason(v3, v31);
    }

    /**
     * Test of compareTo method, of class Version.
     */
    @Test
    public void testCompareTo_MuchLarger() {
        Version v3 = new Version(3);
        Version v31 = new Version(3, 100, 100, 1000);
        assertComparason(v3, v31);
    }

    /**
     * Test of equals & hashCode methods, of class Version.
     */
    @Test
    public void testEquals() {
        Version v3 = new Version(3);
        Version v30 = new Version(3, 0);
        Version v31 = new Version(3, 1);
        Version v32 = new Version(3, 2);
        Version v3456 = new Version(3, 4, 5, 6);
        Version v34560 = new Version(3, 4, 5, 6, 0);

        new EqualsTester().addEqualityGroup(v3, v30).addEqualityGroup(v31).addEqualityGroup(v32).addEqualityGroup(v3456, v34560).testEquals();
    }

    /**
     * Test of toString method, of class Version.
     */
    @Test
    public void testToString() {
        Version v34560 = new Version(3, 4, 5, 6, 0);
        String s = v34560.toString();
        String repl = s.replaceAll("[34560]", "");
        assertEquals("The toString responce `" + s + "` contained too many version numbers.", repl.length(), s.length() - 5);
    }

}
