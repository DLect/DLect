/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.events.listenable;

import java.util.Map.Entry;
import org.dlect.events.EventAdapter;
import org.dlect.events.EventID;
import org.dlect.events.collections.CollectionEventHelper;

public class MapValueListenableCollectionEventHelper<K, V extends Listenable<V>> extends CollectionEventHelper<Entry<K, V>> {

    public MapValueListenableCollectionEventHelper(Object source, EventID eventID, EventAdapter adapter) {
        super(source, eventID, adapter);
    }

    @Override
    public void fireAdd(Entry<K, V> addedElement) {
        ListenableCollectionEventHelper.addListenable(addedElement.getValue(), this.getAdapter());
        super.fireAdd(addedElement);
    }

    @Override
    public void fireRemove(Entry<K, V> removedElement) {
        ListenableCollectionEventHelper.removeListenable(removedElement.getValue(), this.getAdapter());
        super.fireRemove(removedElement); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fireReplace(Entry<K, V> original, Entry<K, V> replacement) {
        ListenableCollectionEventHelper.addListenable(replacement.getValue(), this.getAdapter());
        ListenableCollectionEventHelper.removeListenable(original.getValue(), this.getAdapter());
        super.fireReplace(original, replacement); //To change body of generated methods, choose Tools | Templates.
    }

}
