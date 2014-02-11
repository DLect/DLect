/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events;

/**
 *
 * @author lee
 */
public class TestObject {

    public TestObject() {
    }

    public static enum TestObjecEventID implements EventID {

        ID,
        NAME,
        SUPPORTED_NUMBERS;

        @Override
        public Class<?> getAppliedClass() {
            return TestObject.class;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public boolean isUniqueId() {
            return this == ID;
        }
    }

}
