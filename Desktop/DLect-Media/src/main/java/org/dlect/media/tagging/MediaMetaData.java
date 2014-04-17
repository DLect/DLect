/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.media.tagging;

/**
 *
 * @author lee
 */
public class MediaMetaData {

    public static final String DEFAULT_COMMENT = "Downloaded By DLect. To find out more visit http://facebook.com/DLect";

    private final String title;
    private final String album;
    private final String comment;

    public MediaMetaData(String title, String album) {
        this.title = title;
        this.album = album;
        this.comment = DEFAULT_COMMENT;
    }

    public MediaMetaData(String title, String album, String comment) {
        this.title = title;
        this.album = album;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getComment() {
        return comment;
    }

}
