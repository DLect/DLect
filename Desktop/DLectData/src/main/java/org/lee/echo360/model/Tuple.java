/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.base.Function;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author lee
 */
public class Tuple<A, B, C> {

    public static <X, Y, Z> Function<Tuple<X, Y, Z>, Pair<Y, Z>> tupleToPairBC() {
        return new Function<Tuple<X, Y, Z>, Pair<Y, Z>>() {
            @Override
            public Pair<Y, Z> apply(Tuple<X, Y, Z> input) {
                return Pair.of(input.getB(), input.getC());
            }
        };
    }
    private final A a;
    private final B b;
    private final C c;

    public Tuple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (this.a != null ? this.a.hashCode() : 0);
        hash = 41 * hash + (this.b != null ? this.b.hashCode() : 0);
        hash = 41 * hash + (this.c != null ? this.c.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuple<A, B, C> other = (Tuple<A, B, C>) obj;
        if (this.a != other.a && (this.a == null || !this.a.equals(other.a))) {
            return false;
        }
        if (this.b != other.b && (this.b == null || !this.b.equals(other.b))) {
            return false;
        }
        if (this.c != other.c && (this.c == null || !this.c.equals(other.c))) {
            return false;
        }
        return true;
    }

    public C getC() {
        return c;
    }

    public B getB() {
        return b;
    }

    public A getA() {
        return a;
    }

    public static <A, B, C> Tuple<A, B, C> of(A a, B b, C c) {
        return new Tuple<A, B, C>(a, b, c);
    }
}
