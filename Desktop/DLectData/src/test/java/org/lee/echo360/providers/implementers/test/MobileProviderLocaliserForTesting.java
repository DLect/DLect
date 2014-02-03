/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.net.URI;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Semester;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.implementers.test.xml.EnrollmentInfo;
import org.lee.echo360.providers.implementers.test.xml.LectureData;
import org.lee.echo360.providers.implementers.test.xml.SubjectData;
import org.lee.echo360.providers.implementers.test.xml.SubjectInfo;
import org.lee.echo360.providers.mobile.MobileProviderLocaliser;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class MobileProviderLocaliserForTesting implements MobileProviderLocaliser {

    private final EnrollmentInfo ei;
    private final SortedSet<SubjectInfo> sis;
    private final TreeSet<SubjectData> sds;
    private final SortedSet<LectureData> lds;

    public MobileProviderLocaliserForTesting(EnrollmentInfo ei) {
        this.ei = ei;
        this.sis = ei.courses;
        this.sds = new TreeSet<SubjectData>();
        Iterables.addAll(sds, Iterables.transform(sis, new Function<SubjectInfo, SubjectData>() {
            @Override
            public SubjectData apply(SubjectInfo input) {
                return input.data;
            }
        }));
        this.lds = new TreeSet<LectureData>();
        Iterables.addAll(lds,
                Iterables.concat(Iterables.transform(sis, new Function<SubjectInfo, Collection<LectureData>>() {
            @Override
            public Collection<LectureData> apply(SubjectInfo input) {
                return input.data.lectures;
            }
        })));
    }

    @Override
    public String getCourseName(String name) {
        return name;
    }

    @Override
    public int getSemesterNumber(String name, String courseId, String bbid, Date enrollmentdate) {
        Calendar c = Calendar.getInstance();
        c.setTime(enrollmentdate);
        return c.get(Calendar.YEAR) + 1;
    }

    @Override
    public String getSemesterCoursePostfixName(int semesterNum) {
        return "_" + semesterNum;
    }

    @Override
    public String getSemesterLongName(int semesterNum) {
        return "Sem. " + semesterNum;
    }

    @Override
    public Date getLectureTime(URI u, String title, String xmlData) {
        for (LectureData ld : lds) {
            if (ld.name.equals(title)) {
                return ld.lc.content.body.captureTime;
            }
            if (URI.create(ld.lc.content.body.href).equals(u)) {
                return ld.lc.content.body.captureTime;
            }
        }
        System.out.println("ERROR: Failed to find lecture with:"
                + "\n\tTitle: " + title
                + "\n\tURI  : " + u
                + "\n\tXML  : " + xmlData);
        return null;
    }

    @Override
    public List<Stream> getLectureStream(URI u, String title, String rawXml, Subject s) {
        for (LectureData ld : lds) {
            if (ld.name.equals(title)) {
                int st = ld.lc.content.body.captureTime.getYear();
                return Lists.newArrayList(s.getStream("L" + st, st, true));
            }
        }
        return ImmutableList.of();
    }

    @Override
    public String getVideoFileName(URI u, String title, Date d, Subject sub, List<Stream> streams) {
        return d.getDay() + "-" + d.getMonth() + "-" + d.getYear() + ".m4v";
    }

    @Override
    public String getAudioFileName(URI u, String title, Date d, Subject sub, List<Stream> streams) {
        return d.getDay() + "-" + d.getMonth() + "-" + d.getYear() + ".mp3";
    }

    @Override
    public String getCourseFileName(String name, String courseName) {
        return name.replaceAll("[^a-zA-Z0-9]+", "");
    }

    public String getCourseFileName(SubjectInfo si) {
        return getCourseFileName(si.name, si.name);
    }

    public int getSemesterNumber(SubjectInfo si) {
        return getSemesterNumber(si.name, si.courseid, si.bbid, si.enrollmentdate);
    }

    public Semester getSemester(SubjectInfo get, Blackboard b) {
        return b.getSemester(getSemesterNumber(get), getSemesterLongName(getSemesterNumber(get)), getSemesterCoursePostfixName(getSemesterNumber(get)));
    }

    public Blackboard addAllSubjectsToBlackboard(Blackboard b, EnrollmentInfo ei) {
        for (SubjectInfo get : ei.courses) {
            Subject s = new Subject(get.name, get.bbid,
                    getCourseFileName(get), get.courseid,
                    getSemesterNumber(get), getSemester(get, b));
            b.addSubject(s);
        }
        return b;
    }
}
