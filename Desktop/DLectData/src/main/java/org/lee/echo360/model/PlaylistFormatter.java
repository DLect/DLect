/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

/**
 *
 * @author lee
 */
public class PlaylistFormatter extends Formatter {

    private PlaylistStyle style = PlaylistStyle.DISABLED;

    public PlaylistFormatter() {
    }

    public PlaylistFormatter(PlaylistFormatter pf) {
        super(pf);
        this.style = pf.style;
    }

    public void setStyle(PlaylistStyle style) {
        this.style = style;
    }

    public PlaylistStyle getStyle() {
        return style;
    }

    @Override
    public int hashCode() {
        int hash = hashCode_();
        hash = 83 * hash + (this.style != null ? this.style.hashCode() : 0);
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
        final PlaylistFormatter other = (PlaylistFormatter) obj;
        if (this.style != other.style) {
            return false;
        }
        return this.equalTo(other);
    }

    public static enum PlaylistStyle {

        DISABLED("Disabled"), ALL("All in 1 Playlist"), BY_STREAM("By Stream");
        private final String toString;

        private PlaylistStyle(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString; //To change body of generated methods, choose Tools | Templates.
        }
    }

    @Override
    public String toString() {
        return "Playlist formatter: " + style + "\n" + super.toString();

    }
}
