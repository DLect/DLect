/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import java.util.List;

/**
 *
 * @author lee
 */
public class TagFormatter extends Formatter {

    private boolean enabled = false;

    public TagFormatter() {
    }

    public TagFormatter(TagFormatter tf) {
        super(tf);
        this.enabled = tf.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String formatAlbum(DownloadType dt, Subject s, Lecture l) {
        String postfix = "";
        List<Stream> st = l.getStreams();
        if (s.getStreams().size() > 1 && !st.isEmpty()) {
            postfix = " - " + st.get(0).getName();
            for (int i = 1; i < st.size(); i++) {
                postfix += ", " + st.get(i).getName();
            }
        }
        return s.getName() + postfix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TagFormatter other = (TagFormatter) obj;
        if (this.enabled != other.enabled) {
            return false;
        }
        return this.enabled ? this.equalTo(other) : true;
    }

    @Override
    public int hashCode() {
        int hash = hashCode_();
        hash = 67 * hash + (this.enabled ? 1 : 0);
        return hash;
    }
}
