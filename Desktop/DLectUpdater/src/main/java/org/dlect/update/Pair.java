/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import java.util.Map;

/**
 *
 * @author lee
 */
public class Pair<A, B> implements Map.Entry<A, B> {

    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public A getKey() {
        return a;
    }

    @Override
    public B getValue() {
        return b;
    }

    @Override
    public B setValue(B value) {
        // No Op.
        return b;
    }

    public A getA() {
        return a;
    }

    public B getB() {
        return b;
    }

    /**
     * <p>Compares this pair to another based on the two elements.</p>
     *
     * @param obj the object to compare to, null returns false
     * @return true if the elements of the pair are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry<?, ?>) {
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
            if (this.getKey() != other.getKey() && (this.getKey() == null || !this.getKey().equals(other.getKey()))) {
                return false;
            }
            if (this.getValue() != other.getValue() && (this.getValue() == null || !this.getValue().equals(other.getValue()))) {
                return false;
            }
        }
        return false;
    }

    /**
     * <p>Returns a suitable hash code. The hash code follows the definition in
     * {@code Map.Entry}.</p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // see Map.Entry API specification
        return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
    }

    /**
     * <p>Returns a String representation of this pair using the format
     * {@code ($a,$b)}.</p>
     *
     * @return a string describing this object, not null
     */
    @Override
    public String toString() {
        return new StringBuilder().append('(').append(getA()).append(',').append(getB()).append(')').toString();
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<A, B>(a, b);
    }
}
