/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import org.dlect.model.Stream;

/**
 *
 * @author lee
 */
public class ImmutableStream implements Comparable<ImmutableStream>, ImmutableModel<Stream> {

    private final String name;

    public ImmutableStream(String name) {
        this.name = name;
    }

    @Override
    public void copyTo(Stream s) {
        s.setName(getName());
    }

    @Override
    public Stream copyToNew() {
        Stream s = new Stream();
        copyTo(s);
        return s;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ImmutableStream o) {
        if (o == null) {
            return 1;
        }
        return Ordering.natural().nullsLast().compare(this.getName(), o.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImmutableStream other = (ImmutableStream) obj;
        return Objects.equal(this.getName(), other.getName());
    }

    @Override
    public String toString() {
        return "ImmutableStream{" + "name=" + name + '}';
    }

    public static ImmutableStream from(Stream stream) {
        return new ImmutableStream(stream.getName());
    }

}
