/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.test.builders;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;

/**
 *
 * @author lee
 */
public class LectureBuilder {

    public static LectureBuilder getBuilder() {
        return new LectureBuilder();
    }
    private static final Random rand = new Random();
    private URI url;
    private boolean enabled;
    private final Map<DownloadType, Boolean> dtEnabled = new EnumMap<DownloadType, Boolean>(DownloadType.class);
    private final Map<DownloadType, Boolean> dtFile = new EnumMap<DownloadType, Boolean>(DownloadType.class);
    private final Map<DownloadType, String> dtFiles = new EnumMap<DownloadType, String>(DownloadType.class);
    private Date date;
    private String id;
    private String code;
    private final List<Stream> streams = new ArrayList<Stream>();

    public LectureBuilder enabled(boolean en) {
        this.enabled = en;
        return this;
    }

    public LectureBuilder enabled(DownloadType dt, boolean en) {
        dtEnabled.put(dt, en);
        return this;
    }

    public LectureBuilder file(DownloadType dt, boolean present) {
        dtFile.put(dt, present);
        return this;
    }

    public LectureBuilder randomURL() {
        url = URI.create("http://www.google.com/" + Math.random());
        return this;
    }

    public LectureBuilder randomFiles() {
        for (DownloadType dt : DownloadType.values()) {
            randomFile(dt);
        }
        return this;
    }

    public LectureBuilder randomFile(DownloadType dt) {
        dtFiles.put(dt, RandomStringUtils.randomAscii(10) + ".test.dlect.tmp");
        return this;
    }

    public LectureBuilder randomCode() {
        this.code = RandomStringUtils.randomAscii(10);
        return this;
    }

    public LectureBuilder randomID() {
        this.id = RandomStringUtils.randomAscii(10);
        return this;
    }

    public LectureBuilder randomDate() {
        this.date = new Date(rand.nextInt());
        return this;
    }

    public LectureBuilder randomStream() {// TODO 
        return this;
    }

    public LectureBuilder randomStreams(int i) {
        for (int j = 0; j < i; j++) {
            randomStream();
        }
        return this;
    }

    public Lecture build() {
        if (dtFiles.get(DownloadType.AUDIO) == null) {
            randomFile(DownloadType.AUDIO);
        }
        String af = dtFiles.get(DownloadType.AUDIO);
        if (dtFiles.get(DownloadType.VIDEO) == null) {
            randomFile(DownloadType.VIDEO);
        }
        String vf = dtFiles.get(DownloadType.VIDEO);
        if (url == null) {
            randomURL();
        }
        if (code == null) {
            randomCode();
        }
        if (id == null) {
            randomID();
        }
        if (date == null) {
            randomDate();
        }
        if (streams.isEmpty()) {
            randomStreams(2);
        }
        Lecture l = new Lecture(url, code, id, date, streams, af, vf);
        l.setEnabled(enabled);
        for (DownloadType dt : DownloadType.values()) {
            if (dtEnabled.get(dt) != null) {
                l.setDownloadEnabled(dt, dtEnabled.get(dt));
            }
            if (dtFile.get(dt) != null) {
                l.setFilePresent(dt, dtFile.get(dt));
            }
        }
        return l;
    }
}
