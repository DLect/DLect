/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lee
 */
public class StringUtilTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

        public StringUtilTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of parseInteger method, of class StringUtil.
     */
    @Test
    public void testParseInteger() {
        System.out.println("parseInteger");
        Map<String, Integer> tests = new HashMap<String, Integer>();
        tests.put("100", 100);
        tests.put("-100", -100);
        tests.put("NOT AN INT", 0);
        tests.put("0", 0);
        tests.put("9 But With Text", 0);
        tests.put(Integer.MAX_VALUE + "", Integer.MAX_VALUE);
        tests.put(Integer.MIN_VALUE + "", Integer.MIN_VALUE);
        for (Entry<String, Integer> entry : tests.entrySet()) {
            String string = entry.getKey();
            int exp = entry.getValue();
            int res = StringUtil.parseInteger(string);
            assertEquals("Input\"" + string + "\"", exp, res);
        }
    }

    /**
     * Test of parseBoolean method, of class StringUtil.
     */
    @Test
    public void testParseBoolean() {
        System.out.println("parseBoolean");
        Map<String, Boolean> tests = new HashMap<String, Boolean>();
        tests.put("false", false);
        tests.put("True", true);
        tests.put("1", true);
        tests.put("0", false);
        tests.put("-1", true);
        tests.put("100", true);
        tests.put("-100", true);
        tests.put("NOT AN INT", false);
        tests.put("9 But With Text", false);
        for (Entry<String, Boolean> entry : tests.entrySet()) {
            String string = entry.getKey();
            boolean exp = entry.getValue();
            boolean res = StringUtil.parseBoolean(string);
            assertEquals("Input\"" + string + "\"", exp, res);
        }
    }

    /**
     * Test of parseDate method, of class StringUtil.
     */
    @Test
    public void testParseDate() {
        System.out.println("parseDate");
        Map<String, Date> tests = new HashMap<String, Date>();
        
        for (Entry<String, Date> entry : tests.entrySet()) {
            String string = entry.getKey();
            Date exp = entry.getValue();
            Date res = StringUtil.parseDate(string);
            assertEquals("Input\"" + string + "\"", exp, res);
        }
    }
}
