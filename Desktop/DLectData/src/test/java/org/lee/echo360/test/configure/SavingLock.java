/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.test.configure;

import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lee.echo360.control.controllers.PropertiesSavingController;
import org.lee.echo360.test.TestUtilities;

/**
 *
 * @author lee
 */
public class SavingLock {

    private final static ReentrantLock oldLock = PropertiesSavingController.SAVE_LOAD_LOCK;

    public static ReentrantLock setUpLock(ReentrantLock r) {
        try {
            TestUtilities.setStaticField(PropertiesSavingController.class, "SAVE_LOAD_LOCK", r);
        } catch (NoSuchFieldException ex) {
        } catch (IllegalArgumentException ex) {
        } catch (IllegalAccessException ex) {
        }
        return r;
    }

    public static void tearDownLock() {
        try {
            TestUtilities.setStaticField(PropertiesSavingController.class, "SAVE_LOAD_LOCK", oldLock);
        } catch (NoSuchFieldException ex) {
        } catch (IllegalArgumentException ex) {
        } catch (IllegalAccessException ex) {
        }
    }
}