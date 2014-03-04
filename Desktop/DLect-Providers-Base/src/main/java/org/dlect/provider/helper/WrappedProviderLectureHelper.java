/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.helper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Stream;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.provider.ImmutableSubjectData;
import org.dlect.provider.WrappedProvider;

/**
 *
 * @author lee
 */
public class WrappedProviderLectureHelper {

    private static Lecture copyToNew(ImmutableLecture il) {
        Lecture l = new Lecture();
        l.setContentID(il.getContentID());
        l.setTime(il.getTime());
        return l;
    }

    private static void copyTo(LectureDownload ld, ImmutableLectureDownload ild) {
        ld.setDownloadExtension(ild.getDownloadExtension());
        ld.setDownloadURL(ild.getDownloadURL());
    }

    public static void mergeSubjectData(Subject s, ImmutableSubjectData data) {
        Map<ImmutableStream, Stream> streams = convertStreams(s.getStreams(), data.getStreams());
        s.setStreams(streams.values());
        Set<ImmutableLecture> lectures = data.getLectures();

        ImmutableMultimap<ImmutableLecture, ImmutableStream> lsMapping = data.getLectureStreamMapping();

        Map<Lecture, Lecture> lectureMapping = creatingMapping(s.getLectures());

        for (ImmutableLecture il : lectures) {
            Lecture lecToConfig = copyToNew(il);

            Lecture original = lectureMapping.get(lecToConfig);

            if (original != null) {
                lecToConfig = original;
            }

            Map<DownloadType, LectureDownload> lds = getLectureDownloads(lecToConfig, il);
            lecToConfig.setLectureDownloads(lds);

            Set<Stream> lecStreams = getLectureStreams(il.getStreams(), streams);
            lecStreams.addAll(getLectureStreams(lsMapping.get(il), streams));

            lecToConfig.setStreams(lecStreams);
        }
    }

    private static Map<DownloadType, LectureDownload> getLectureDownloads(Lecture lecToConfig, ImmutableLecture il) {
        ImmutableMap<DownloadType, LectureDownload> existing = lecToConfig.getLectureDownloads();
        Map<DownloadType, LectureDownload> lds = Maps.newEnumMap(DownloadType.class);
        for (Entry<DownloadType, ImmutableLectureDownload> entry : il.getLectureDownloads().entrySet()) {
            DownloadType dt = entry.getKey();
            ImmutableLectureDownload ild = entry.getValue();
            LectureDownload existingLd = existing.get(dt);

            if (existingLd == null) {
                existingLd = new LectureDownload();
            }

            copyTo(existingLd, ild);
            lds.put(dt, existingLd);
        }
        return lds;
    }

    private static Set<Stream> getLectureStreams(Collection<ImmutableStream> stream, Map<ImmutableStream, Stream> streams) {
        Set<Stream> lecStreams = Sets.newHashSet();
        for (ImmutableStream is : stream) {
            Stream st = streams.get(is);
            if (st == null) {
                WrappedProvider.LOGGER.error("Stream not found " + is + ". Ignoring");
            } else {
                lecStreams.add(st);
            }
        }
        return lecStreams;
    }

    protected static Map<ImmutableStream, Stream> convertStreams(Set<Stream> existing, Set<ImmutableStream> adding) {
        Map<ImmutableStream, Stream> mapping = Maps.newHashMap();
        Map<Stream, Stream> originalMapping = creatingMapping(existing);

        for (ImmutableStream immutableStream : adding) {
            Stream s = immutableStream.copyToNew();

            Stream original = originalMapping.get(s);
            if (original == null) {
                original = s;
            } else {
                immutableStream.copyTo(original);
            }
            mapping.put(immutableStream, original);
        }
        return mapping;
    }

    public static <T> Map<T, T> creatingMapping(Collection<T> c) {
        // TODO move this to utils class.
        Builder<T, T> map = ImmutableMap.builder();
        for (T t : c) {
            map.put(t, t);
        }
        return map.build();
    }
}
