/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ui;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import javax.swing.DefaultComboBoxModel;
import org.dlect.provider.loader.ProviderDetail;

/**
 *
 * @author lee
 */
public class ProviderModel extends DefaultComboBoxModel<String> {

    private static final long serialVersionUID = 1L;
    private static final Comparator<ProviderDetail> COMPARATOR = new Comparator<ProviderDetail>() {
        @Override
        public int compare(ProviderDetail o1, ProviderDetail o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    };

    private final ImmutableList<ProviderDetail> providers;

    public ProviderModel(Collection<ProviderDetail> loader) {
        TreeSet<ProviderDetail> ps = Sets.newTreeSet(COMPARATOR);
        ps.addAll(loader);
        providers = ImmutableList.copyOf(ps);
    }

    /**
     * The index in the list of this provider; or 0 if it does not exist.
     * 
     * @param provider
     * @return 
     */
    public int getProviderIndex(ProviderDetail provider) {
        int listIdx = providers.indexOf(provider);
        if (listIdx < 0) {
            return 0;
        } else {
            return listIdx + 1;
        }
    }

    @Override
    public int getSize() {
        return providers.size() + 1;
    }

    @Nonnull
    public Optional<ProviderDetail> getProviderAt(int idx) {
        if (idx >= 1 && idx < getSize()) {
            return Optional.of(providers.get(idx - 1));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public String getElementAt(int index) {
        Optional<ProviderDetail> p = getProviderAt(index);
        if (p.isPresent()) {
            return p.get().getName();
        } else {
            return firstOption();
        }
    }

    private String firstOption() {
        return "Choose your university";
        //TODO(Later) return I18N.getString("Choose_University");
    }

    @Override
    public void removeAllElements() {
    }

    @Override
    public void addElement(String item) {
    }

    @Override
    public void removeElement(Object obj) {
    }

    @Override
    public void insertElementAt(String item, int index) {
    }

    @Override
    public void removeElementAt(int index) {
    }
}
