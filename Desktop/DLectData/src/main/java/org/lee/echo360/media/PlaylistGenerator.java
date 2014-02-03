/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.media;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.lee.echo360.control.controllers.PropertiesController;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.PlaylistFormatter;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public class PlaylistGenerator {

    private static final LoadingCache<DownloadType, Predicate<Lecture>> LECTURE_PRESENT_PREDICATE_CACHE = CacheBuilder.newBuilder().softValues().build(new CacheLoader<DownloadType, Predicate<Lecture>>() {
        @Override
        public Predicate<Lecture> load(final DownloadType key) throws Exception {
            return new Predicate<Lecture>() {
                @Override
                public boolean apply(Lecture input) {
                    return input.isFilePresent(key);
                }
            };
        }
    });

    public static boolean buildPlaylist(File playlistFile, DownloadType type, Map<Lecture, File> lectures, Subject s) {
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
            for (Lecture lecture : lectures.keySet()) {
                File file = lectures.get(lecture);

                w.write("#EXTINF:-1," + s.getPlaylistFormatter().formatName(type, s, lecture));
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

    public static boolean buildAllPlaylistsFor(Subject s, PropertiesController pc) {
        boolean res = true;
        for (DownloadType dt : DownloadType.values()) {
            res &= buildAllPlaylistsFor(s, dt, pc);
        }
        return res;
    }

    public static boolean buildAllPlaylistsFor(Subject s, DownloadType dt, PropertiesController pc) {
        PlaylistFormatter pf = s.getPlaylistFormatter();
        switch (pf.getStyle()) {
            case ALL:
                return buildAllInOnePlaylistFor(s, dt, pc);
            case BY_STREAM:
                return buildPlaylistsByStreamFor(s, dt, pc);
            case DISABLED:
            default:
                return true;
        }
    }

    private static boolean buildAllInOnePlaylistFor(final Subject s, final DownloadType downloadType, final PropertiesController pc) {
        File playlistFile = getFileNameForSinglePlaylist(s, downloadType, pc);
        return buildPlaylist(playlistFile, downloadType, getLectureFileNames(s.getLectures(), s, downloadType, pc), s);
    }

    private static boolean buildPlaylistsByStreamFor(Subject s, DownloadType dt, PropertiesController pc) {
        boolean res = true;
        Map<Stream, File> sf = getFileNamesByStream(s, dt, pc);
        for (Map.Entry<Stream, File> entry : sf.entrySet()) {
            Stream stream = entry.getKey();
            File playlistFile = entry.getValue();
            res &= buildPlaylist(playlistFile, dt, getLectureFileNames(stream.getLectures(), s, dt, pc), s);
        }
        return res;
    }

    public static Map<Stream, File> getFileNamesByStream(Subject s, DownloadType dt, PropertiesController pc) {
        File subFolder = pc.getFolderFor(s);
        Map<Stream, File> f = Maps.newTreeMap();

        for (Stream stream : s.getStreams()) {
            String playlistName = s.getFolderName() + " " + stream.getName() + " - " + dt.toString() + ".m3u";
            File playlistFile = new File(subFolder, playlistName);
            f.put(stream, playlistFile);
        }
        return f;
    }

    public static File getFileNameForSinglePlaylist(final Subject s, final DownloadType downloadType, final PropertiesController pc) {
        String playlistName = s.getFolderName() + " " + downloadType.toString() + " Playlist.m3u";
        return FileUtils.getFile(pc.getFolderFor(s), playlistName);
    }

    private static Map<Lecture, File> getLectureFileNames(final SortedSet<Lecture> lecs, final Subject s, final DownloadType downloadType, final PropertiesController pc) {
        SortedSet<Lecture> filtered = Sets.filter(lecs, LECTURE_PRESENT_PREDICATE_CACHE.getUnchecked(downloadType));
        return Maps.asMap(filtered, new Function<Lecture, File>() {
            @Override
            public File apply(Lecture input) {
                return pc.getFileFor(s, input, downloadType);
            }
        });
    }
}