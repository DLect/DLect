/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui.helper;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author lee
 */
public class EnumComboBoxModel<E extends Enum<E>> implements ComboBoxModel {

    private E selected = null;
    private final List<E> data;
    private final String none;
    private final Map<String, E> valueMap;
    private final Class<E> enumClass;
    private final int size;
    private final boolean isNoneEnabled;

    public EnumComboBoxModel(Class<E> cls) {
        this(cls, null);
    }

    public EnumComboBoxModel(Class<E> cls, String noneString) {
        data = new ArrayList<E>(EnumSet.allOf(cls));
        if (data.isEmpty()) {
            throw new IllegalArgumentException("The enum contains no constants. Enum Class: " + cls);
        }
        //we could size these, probably not worth it; enums are usually small 
        valueMap = new HashMap<String, E>();
        enumClass = cls;
        none = noneString;
        isNoneEnabled = noneString != null;
        size = data.size() + (isNoneEnabled ? 1 : 0);
        for (E e : data) {
            String s = e.toString();

            if (valueMap.containsKey(s)) {
                throw new IllegalArgumentException(
                        "multiple constants map to one string value");
            }

            valueMap.put(s, e);
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (enumClass.isInstance(anItem)) {
            selected = (E) anItem;
        } else if (anItem instanceof String) {
            selected = valueMap.get((String) anItem);
        } else {
            selected = null;
        }
        if (selected == null && !isNoneEnabled) {
            selected = data.get(0);
        }
    }

    @Override
    public Object getSelectedItem() {
        if (selected == null && isNoneEnabled) {
            return none;
        } else if (selected == null && !isNoneEnabled) {
            selected = data.get(0);
            return selected;
        } else {
            return selected;
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Object getElementAt(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else if (isNoneEnabled) {
            if (index == 0) {
                return none;
            } else {
                return data.get(index - 1);
            }
        } else {
            return data.get(index);
        }
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        //No Op
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        // No Op
    }
}
