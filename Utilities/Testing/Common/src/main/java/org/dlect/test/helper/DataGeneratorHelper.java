/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author lee
 */
public class DataGeneratorHelper {

    public static List<String> fillList(int min, int max) {
        List<String> list = Lists.newArrayList();
        for (int i = min; i < max; i++) {
            list.add("Object No. " + i);
        }
        return list;
    }

    public static Map<String, String> fillMap(int min, int max) {
        Map<String, String> map = Maps.newHashMap();
        for (int i = min; i < max; i++) {
            map.put("Key No. " + i, "Value No. " + i);
        }
        return map;
    }

    public static Set<Entry<String, String>> fillEntrySet(int min, int max) {
        return fillMap(min, max).entrySet();
    }
    public static Set<String> fillKeySet(int min, int max) {
        return fillMap(min, max).keySet();
    }

}
