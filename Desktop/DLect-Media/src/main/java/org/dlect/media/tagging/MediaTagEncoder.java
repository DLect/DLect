/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.media.tagging;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.dlect.logging.MediaLogging;
import org.dlect.model.Lecture;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.model.formatter.LectureTitleFormat;
import org.jaudiotagger.audio.AudioFile;

import static org.dlect.model.formatter.DownloadType.AUDIO;
import static org.dlect.model.formatter.DownloadType.VIDEO;

/**
 *
 * @author lee
 */
public class MediaTagEncoder {

    private static final Map<DownloadType, MediaTagger> MEDIA_TAGGERS = ImmutableMap.of(AUDIO, new MP3MediaTagger(),
                                                                                        VIDEO, new M4VMediaTagger());

    static {
        AudioFile.logger.setLevel(Level.OFF);
    }

    public static final String COMMENT = "Downloaded By DLect. To find out more visit http://facebook.com/DLect";

    public static boolean tag(final File inFile, final File outFile, final Subject s, final Lecture l, final DownloadType dt) {
        boolean success = false;
        LectureTitleFormat format = s.getTagFormat().getFormat(dt);
        MediaTagger tagger = MEDIA_TAGGERS.get(dt);
        if (s.getTagFormat().isEnabled() && format != null && tagger != null) {
            MediaMetaData a = new MediaMetaData(getName(s, l, dt), getAlbum(s, l, dt));
            
            
            // TODO(LAteR) implement tagging in MediaTagEncoder.
            
            
        }
        if (!success) {
            try {
                FileUtils.copyFile(inFile, outFile);
                return true;
            } catch (IOException ex) {
                MediaLogging.LOG.error("Error applying M4V tags. ", ex);
            }
        }
        return success;
    }

    private static String getName(Subject s, Lecture l, DownloadType dt) {
        LectureTitleFormat format = s.getTagFormat().getFormat(dt);

        if (format == null) {
            throw new UnsupportedOperationException("Can't get format for null format. DownloadType: " + dt);
        }

        return format.format(s, l);
    }

    private static String getAlbum(Subject s, Lecture l, DownloadType dt) {
        return formatAlbum(dt, s, l);
    }

    public static String formatAlbum(DownloadType dt, Subject s, Lecture l) {
        List<Stream> st = l.getStreams().asList();
        if (s.getStreams().size() <= 1 || st.isEmpty()) {
            // No streams or only 1 
            return s.getName();
        } else if (s.getStreams().equals(st)) {
            return s.getName() + " - All";
        } else {
            String postfix = " - " + st.get(0).getName();
            for (int i = 1; i < st.size(); i++) {
                postfix += ", " + st.get(i).getName();
            }
            return s.getName() + postfix;
        }
    }

}
