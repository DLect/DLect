/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author lee
 */
public class Formatter implements Serializable {

    public Formatter() {
    }

    public Formatter(Formatter f) {
        this.formats.putAll(f.formats);
    }
    private final EnumMap<DownloadType, FormatType> formats = Maps.newEnumMap(DownloadType.class);

    public void setFormat(DownloadType dt, FormatType ft) {
        if (dt != null) {
            formats.put(dt, ft);
        }
    }

    public boolean isFormatting(DownloadType dt) {
        return formats.get(dt) != null;
    }

    /**
     *
     * @param dt
     * @return The {@linkplain FormatType} associated with the given
     * {@linkplain DownloadType} or {@linkplain FormatType#getDefault()} if
     * there is none associated.
     */
    public FormatType getFormat(DownloadType dt) {
        if (dt == null || formats.get(dt) == null) {
            return FormatType.getDefault();
        } else {
            return formats.get(dt);
        }
    }

    /**
     *
     * @param dt
     * @return The {@linkplain FormatType} associated with the given
     * {@linkplain DownloadType} or {@code null} if there is none associated.
     */
    public FormatType getNullableFormat(DownloadType dt) {
        return formats.get(dt);
    }

    public String formatName(DownloadType dt, Subject s, Lecture l) {
        return getFormat(dt).format(s, l);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (Map.Entry<DownloadType, FormatType> entry : formats.entrySet()) {
            DownloadType d = entry.getKey();
            FormatType f = entry.getValue();
            if (b.length() == 0) {
                b.append('\t').append(d).append(":\t\t").append(f);
            } else {
                b.append('\n').append('\t').append(d).append(":\t\t").append(f);
            }
        }
        return b.toString();
    }

    @Override
    public int hashCode() {
        return hashCode_();
    }

    protected int hashCode_() {
        int hash = 3;
        hash = 19 * hash + (this.formats != null ? this.formats.hashCode() : 0);
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
        final Formatter other = (Formatter) obj;
        if (this.formats != other.formats && (this.formats == null || !this.formats.equals(other.formats))) {
            return false;
        }
        return true;
    }

    protected final boolean equalTo(Formatter f) {
        return this.formats.equals(f.formats);
    }
}
