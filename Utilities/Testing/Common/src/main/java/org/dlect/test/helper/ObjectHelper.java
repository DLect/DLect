/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.test.helper;

import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import org.dlect.test.Resettable;
import org.dlect.test.Resettables;

/**
 *
 * @author lee
 */
public class ObjectHelper {
    // TODO test these methods.

    public static Resettable storeStaticStateOf(Class<?>... classes) throws Exception {
        List<Resettable> fieldResetters = Lists.newArrayList();

        for (Class<?> clz : classes) {
            Field[] declaredFields = clz.getDeclaredFields();
            for (Field f : declaredFields) {
                // only restore static non-final fields.
                if (Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
                    fieldResetters.add(new ObjectConfig(f, null, getField(f, null)));
                }
            }
        }
        return Resettables.join(fieldResetters);
    }

    public static ObjectConfig setStaticField(Class<?> c, String name, Object val) throws Exception {
        Field pcsField = getClassField(c, name);
        Object oldVal = setField(pcsField, null, val);

        return new ObjectConfig(pcsField, null, oldVal);
    }

    public static ObjectConfig getStaticFieldResetter(Class<?> c, String name) throws Exception {
        Field pcsField = getClassField(c, name);
        Object oldVal = getField(pcsField, null);

        return new ObjectConfig(pcsField, null, oldVal);
    }

    public static Object getStaticField(Class<?> c, String name) throws Exception {
        Field pcsField = getClassField(c, name);
        return getField(pcsField, null);
    }

    public static Field getClassField(Class<?> c, String name) throws NoSuchFieldException, SecurityException {
        assert c != null;
        Field pcsField = c.getDeclaredField(name);
        return pcsField;
    }

    public static Object setField(Field pcsField, Object instance, Object value) throws Exception {
        boolean access = pcsField.isAccessible();
        try {
            pcsField.setAccessible(true);
            Object original = pcsField.get(instance);
            pcsField.set(instance, value);
            return original;
        } finally {
            pcsField.setAccessible(access);
        }
    }

    public static Object getField(Field pcsField, Object instance) throws Exception {

        boolean access = pcsField.isAccessible();
        try {
            pcsField.setAccessible(true);
            return pcsField.get(instance);
        } finally {
            pcsField.setAccessible(access);
        }
    }

    public static class ObjectConfig implements Resettable {

        private final Field f;
        private final Object instance;
        private final Object oldVal;

        public ObjectConfig(Field f, Object instance, Object oldVal) {
            this.f = f;
            this.instance = instance;
            this.oldVal = oldVal;
        }

        @Override
        public void reset() throws Exception {
            setField(f, instance, oldVal);
        }
    }

    private ObjectHelper() {
    }
}
