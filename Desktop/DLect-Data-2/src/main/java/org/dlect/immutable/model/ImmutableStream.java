/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.immutable.model;

import com.google.common.base.Objects;
import org.dlect.model.Stream;

/**
 *
 * @author lee
 */
public class ImmutableStream implements Comparable<ImmutableStream>, ImmutableModel<Stream> {

    private final String name;
    private final long number;

    public ImmutableStream(String name, long number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public void copyTo(Stream s) {
        s.setName(getName());
        s.setNumber(getNumber());
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

    public long getNumber() {
        return number;
    }

    @Override
    public int compareTo(ImmutableStream o) {
        if (o == null) {
            return 1;
        }
        return Long.compare(this.getNumber(), o.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getNumber());
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
        return this.getNumber() == other.getNumber();
    }

    @Override
    public String toString() {
        return "ImmutableStream{" + "name=" + name + ", number=" + number + '}';
    }

    public static ImmutableStream from(Stream stream) {
        return new ImmutableStream(stream.getName(), stream.getNumber());
    }

}
