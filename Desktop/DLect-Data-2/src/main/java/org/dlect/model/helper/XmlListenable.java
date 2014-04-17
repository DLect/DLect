/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import org.dlect.events.listenable.Listenable;
import org.w3c.dom.Element;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlListenable<T extends Listenable<T>> extends Listenable<T> {

    @XmlAnyElement
    private List<Element> otherElements;

    @XmlAnyAttribute
    private Map<QName, String> otherAttributes;

    public XmlListenable() {
        otherElements = Lists.newArrayList();
        otherAttributes = Maps.newHashMap();
    }

    public Map<QName, String> getOtherAttributes() {
        return copyOf(otherAttributes);
    }

    public List<Element> getOtherElements() {
        return copyOf(otherElements);
    }

}
