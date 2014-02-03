/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.lee.echo360.control.ProviderListListener;
import org.lee.echo360.control.exceptions.InvalidImplemetationException;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.providers.BlackboardProviders;
import org.lee.echo360.util.I18N;

/**
 *
 * @author lee
 */
public class ProviderModel extends DefaultComboBoxModel implements
        ProviderListListener {

    private static final ProviderModel model = new ProviderModel();
    private static final String FIRST_OPTION = I18N.getString("Choose_University");

    public static ProviderModel getModel() {
        return model;
    }
    private final List<BlackboardProviderWrapper> providers = new ArrayList<BlackboardProviderWrapper>(BlackboardProviders.getLazyProviders());
    private static final Comparator<BlackboardProviderWrapper> COMPARATOR = new Comparator<BlackboardProviderWrapper>() {
        @Override
        public int compare(BlackboardProviderWrapper o1, BlackboardProviderWrapper o2) {
            try {
                return o1.getProviderName().compareToIgnoreCase(o2.getProviderName());
            } catch (InvalidImplemetationException ex) {
                return 0;
            }
        }
    };

    private ProviderModel() {
        BlackboardProviders.addProviderListListener(this);
    }

    @Override
    public int getSize() {
        update();
        return providers.size() + 1;
    }

    @Override
    public Object getElementAt(int index) {
        update();
        return index > 0 ? providers.get(index - 1) : FIRST_OPTION;
    }

    private void update() {
        providers.clear();
        providers.addAll(BlackboardProviders.getLazyProviders());
        Collections.sort(providers, COMPARATOR);
    }

    @Override
    public void notifyBadProviderRemoved(Class cls, BlackboardProviderWrapper provider) {
        update();
        int n = providers.indexOf(provider) + 1;
        fireIntervalRemoved(this, n, n);
    }

    @Override
    public void notifyListPopulated() {
        update();
        fireIntervalAdded(this, 1, getSize());
    }

    @Override
    public void removeAllElements() {
    }

    @Override
    public void addElement(Object item) {
    }

    @Override
    public void removeElement(Object obj) {
    }

    @Override
    public void insertElementAt(Object item, int index) {
    }

    @Override
    public void removeElementAt(int index) {
    }
}
