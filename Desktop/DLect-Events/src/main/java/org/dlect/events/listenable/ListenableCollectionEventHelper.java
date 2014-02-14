/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;
import org.dlect.events.collections.CollectionEventHelper;

public class ListenableCollectionEventHelper<T extends Listenable<T>> extends CollectionEventHelper<T> {

    public ListenableCollectionEventHelper(Object source, EventID listID, EventAdapter adapter) {
        super(source, listID, adapter);
    }

    /**
     * Sets the given object's parent adapter to
     *
     * @param elm
     */
    protected void addListenable(@Nullable T elm) {
        if (elm == null) {
            return;
        }
        EventAdapter elmAdapt = elm.getAdapter();
        if (elmAdapt == null) {
            throw new IllegalStateException("Element " + elm + " has a null event adapter.");
        }
        EventAdapter elmParentAdapt = elmAdapt.getParentAdapter();

        if (elmParentAdapt == null) {
            elmAdapt.setParentAdapter(this.getAdapter());
        } else {
            throw new IllegalStateException(debugElmAndParent(elm, elmParentAdapt) + " is not null. This means that "
                                            + "another object has configured the element.");
        }
    }

    private String debugElmAndParent(T elm, EventAdapter elmParentAdapt) {
        return "Trying to add an element(" + elm + ") whose parent adapter(" + elmParentAdapt + ")";
    }

    protected void removeListenable(@Nullable T elm) {
        if (elm == null) {
            return;
        }
        EventAdapter elmAdapt = elm.getAdapter();
        if (elmAdapt == null) {
            throw new IllegalStateException("Element " + elm + " has a null event adapter.");
        }
        EventAdapter elmParentAdapt = elmAdapt.getParentAdapter();

        if (same(elmParentAdapt, this.getAdapter())) {
            elmAdapt.setParentAdapter(null);
        } else {
            throw new IllegalStateException(debugElmAndParent(elm, elmParentAdapt) + " is different from the adapter(" + this.getAdapter()
                                            + ") it should have been.");
        }
    }

    @Override
    public void fireAdd(T addedElement) {
        addListenable(addedElement);
        super.fireAdd(addedElement);
    }

    @Override
    public void fireRemove(T removedElement) {
        removeListenable(removedElement);
        super.fireRemove(removedElement);
    }

    @Override
    public void fireReplace(T original, T replacement) {
        addListenable(replacement);
        removeListenable(original);
        super.fireReplace(original, replacement);
    }

    private boolean same(Object a, Object b) {
        return a == b;
    }

}
