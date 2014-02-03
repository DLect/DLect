/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import com.google.common.collect.Lists;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 *
 * @author lee
 */
public class DebuggingListener implements PropertyChangeListener {

    public static PropertyChangeListener getInstance() {
        return new DebuggingListener();
    }
    public final List<PropertyChangeEvent> events = Lists.newArrayList();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        events.add(evt);
    }
}
