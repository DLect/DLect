/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.helper;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.Database;
import org.dlect.model.Semester;
import org.dlect.model.Subject;
import org.dlect.provider.WrappedProvider;
import org.dlect.provider.objects.ImmutableSubjectData;

import static org.dlect.provider.helper.WrappedProviderLectureHelper.creatingMapping;

/**
 *
 * @author lee
 */
public class WrappedProviderSubjectHelper {

    private static final Multimap<ImmutableLecture, ImmutableStream> EMPTY_LECTURE_STREAM_MAP = ImmutableMultimap.of();

    public static void insertSemestersIntoDatabase(Database d, Multimap<ImmutableSemester, ImmutableSubject> subjects) {
        Map<Semester, Semester> semMap = creatingMapping(d.getSemesters());
        Set<Semester> semesters = Sets.newHashSet();
        for (Entry<ImmutableSemester, Collection<ImmutableSubject>> entry : subjects.asMap().entrySet()) {
            ImmutableSemester sem = entry.getKey();
            Collection<ImmutableSubject> collection = entry.getValue();
            Semester s = sem.copyToNew();
            Semester existing = semMap.get(s);
            if (existing == null) {
                existing = s;
            } else {
                sem.copyTo(existing);
            }
            semesters.add(existing);
            insertSubjectsIntoSemester(existing, collection);
        }
        d.setSemesters(semesters);
    }

    private static void insertSubjectsIntoSemester(Semester sem, Collection<ImmutableSubject> imSub) {
        Map<Subject, Subject> subMap = creatingMapping(sem.getSubjects());
        Set<Subject> subjects = Sets.newHashSet();
        for (ImmutableSubject is : imSub) {
            Subject s = is.copyToNew();
            Subject existing = subMap.get(s);
            if (existing == null) {
                existing = s;
            } else {
                is.copyTo(existing);
            }
            subjects.add(existing);
            if (!is.getLectures().isEmpty() || !is.getStreams().isEmpty()) {
                WrappedProviderLectureHelper.mergeSubjectData(existing, new ImmutableSubjectData(EMPTY_LECTURE_STREAM_MAP, is.getLectures(), is.getStreams()));
            }
        }
        sem.setSubjects(subjects);
    }

}
