/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import java.util.HashSet;
import java.util.Set;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableStream;

/**
 *
 * @author lee
 */
public class BlackboardLectureMapping {

    private static final BlackboardLectureMapping EMPTY = new BlackboardLectureMapping(
            new HashSet<ImmutableLecture>(),
            ImmutableSetMultimap.<ImmutableLecture, ImmutableStream>of());

    private final ImmutableSet<ImmutableLecture> lectures;
    private final ImmutableSetMultimap<ImmutableLecture, ImmutableStream> lectureStreamMapping;

    public BlackboardLectureMapping(Set<ImmutableLecture> lectures, Multimap<ImmutableLecture, ImmutableStream> lectureStreamMapping) {
        this.lectures = ImmutableSet.copyOf(lectures);
        this.lectureStreamMapping = ImmutableSetMultimap.copyOf(lectureStreamMapping);
    }

    public ImmutableSetMultimap<ImmutableLecture, ImmutableStream> getLectureStreamMapping() {
        return ImmutableSetMultimap.copyOf(lectureStreamMapping);
    }

    public ImmutableSet<ImmutableLecture> getLectures() {
        return ImmutableSet.copyOf(lectures);
    }

    public static BlackboardLectureMapping empty() {
        return EMPTY;
    }

}
