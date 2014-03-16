/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.media;

import org.dlect.media.tagging.MediaMetaData;
import com.google.common.collect.Maps;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

/**
 *
 * @author lee
 */
@Deprecated
public class PlaylistGenerator {
// TODO(Later) FIX THIS CLASS COMPLETELY

    public static boolean buildPlaylist(File playlistFile, DownloadType type, Map<File, MediaMetaData> lectureMetaDaya, Subject s) {
        BufferedWriter w = null;
        try {
            if (!playlistFile.getParentFile().exists()) {
                playlistFile.getParentFile().mkdirs();
            }
            w = new BufferedWriter(new FileWriter(playlistFile));
            w.write("#EXTM3U");
            w.newLine();
            w.newLine();
            w.newLine();
            for (Entry<File, MediaMetaData> metaDataEntry : lectureMetaDaya.entrySet()) {
                File file = metaDataEntry.getKey();

                w.write("#EXTINF:-1," + metaDataEntry.getValue().getTitle());
                w.newLine();
                w.write(FilenameUtils.getName(file.toString()));
                w.newLine();
                w.newLine();
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(PlaylistGenerator.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(PlaylistGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean buildAllPlaylistsFor(Subject s) {
        boolean res = true;
        for (DownloadType dt : DownloadType.values()) {
            res &= buildAllPlaylistsFor(s, dt);
        }
        return res;
    }

    public static boolean buildAllPlaylistsFor(Subject s, DownloadType dt) {
//        PlaylistFormatter pf = s.getPlaylistFormatter();
//        switch (pf.getStyle()) {
//            case ALL:
//                return buildAllInOnePlaylistFor(s, dt, pc);
//            case BY_STREAM:
//                return buildPlaylistsByStreamFor(s, dt, pc);
//            case DISABLED:
//            default:
        return true;
//        }
    }

    private static boolean buildAllInOnePlaylistFor(final Subject s, final DownloadType downloadType) {
        File playlistFile = getFileNameForSinglePlaylist(s, downloadType);
        return false; //buildPlaylist(playlistFile, downloadType, getLectureFileNames(s.getLectures(), s, downloadType), s);
    }

    private static boolean buildPlaylistsByStreamFor(Subject s, DownloadType dt) {
        boolean res = true;
        Map<Stream, File> sf = getFileNamesByStream(s, dt);
        for (Map.Entry<Stream, File> entry : sf.entrySet()) {
            Stream stream = entry.getKey();
            File playlistFile = entry.getValue();
            // res &= buildPlaylist(playlistFile, dt, getLectureFileNames(stream.getLectures(), s, dt, pc), s);
        }
        return res;
    }

    public static Map<Stream, File> getFileNamesByStream(Subject s, DownloadType dt) {
        Map<Stream, File> f = Maps.newTreeMap();

        for (Stream stream : s.getStreams()) {
//            String playlistName = s.getFolderName() + " " + stream.getName() + " - " + dt.toString() + ".m3u";
            //File playlistFile = new File(subFolder, playlistName);
            //f.put(stream, playlistFile);
        }
        return f;
    }

    public static File getFileNameForSinglePlaylist(final Subject s, final DownloadType downloadType) {
//        String playlistName = s.getFolderName() + " " + downloadType.toString() + " Playlist.m3u";
        return null;//FileUtils.getFile(pc.getFolderFor(s), playlistName);
    }

}
