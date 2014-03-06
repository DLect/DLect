/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.objects;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import java.util.Collection;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableStream;

/**
 *
 * @author lee
 */
public class ImmutableSubjectData {

    private final ImmutableMultimap<ImmutableLecture, ImmutableStream> lectureStreamMapping;
    private final ImmutableSet<ImmutableLecture> lectures;
    private final ImmutableSet<ImmutableStream> streams;

    public ImmutableSubjectData(Multimap<ImmutableLecture, ImmutableStream> lectureStreamMapping, Collection<ImmutableLecture> lectures, Collection<ImmutableStream> streams) {
        this.lectureStreamMapping = ImmutableMultimap.copyOf(lectureStreamMapping);
        this.lectures = ImmutableSet.
                <ImmutableLecture>builder()
                .addAll(lectures)
                .addAll(lectureStreamMapping.keySet())
                .build();
        this.streams = ImmutableSet.
                <ImmutableStream>builder()
                .addAll(streams)
                .addAll(lectureStreamMapping.values())
                .build();
    }

    public ImmutableMultimap<ImmutableLecture, ImmutableStream> getLectureStreamMapping() {
        return ImmutableMultimap.copyOf(lectureStreamMapping);
    }

    public ImmutableSet<ImmutableLecture> getLectures() {
        return ImmutableSet.copyOf(lectures);
    }

    public ImmutableSet<ImmutableStream> getStreams() {
        return ImmutableSet.copyOf(streams);
    }

}
