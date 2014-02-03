/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test.xml;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

/**
 *
 * @author lee
 */
public class BlackboardMarshaller {

    private static final String ROOT_URL = "https://learn.uq.edu.au";

    public static EnrollmentInfo loadEnrollmentInfo(InputStream input) {
        try {
            JAXBContext j = JAXBContext.newInstance(EnrollmentInfo.class);
            Object load = j.createUnmarshaller().unmarshal(input);
            if (load != null && load instanceof EnrollmentInfo) {
                return (EnrollmentInfo) load;
            } else {
                return null;
            }
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static SubjectData loadSubjectData(InputStream input) {
        try {
            JAXBContext j = JAXBContext.newInstance(SubjectData.class);
            Object load = j.createUnmarshaller().unmarshal(input);
            if (load != null && load instanceof SubjectData) {
                return (SubjectData) load;
            } else {
                return null;
            }
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String saveEnrollmentInfo(EnrollmentInfo ei) {
        try {
            OutputStream s = new ByteArrayOutputStream(1000);
            JAXBContext j = JAXBContext.newInstance(EnrollmentInfo.class);
            j.createMarshaller().marshal(ei, s);
            return s.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String saveSubjectData(SubjectData sd) {
        try {
            OutputStream s = new ByteArrayOutputStream(1000);
            JAXBContext j = JAXBContext.newInstance(SubjectData.class);
            j.createMarshaller().marshal(sd, s);
            return s.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String saveLectureContent(LectureContent lc) {
        try {

            OutputStream s = new ByteArrayOutputStream(1000);
            JAXBContext j = JAXBContextFactory.createContext(new Class[]{LectureContent.class}, Collections.<String, Object>emptyMap());
            j.createMarshaller().marshal(lc, s);
            return s.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static EnrollmentInfo getRandomEnrollmentInfo(int numSubj, int numLect) {
        EnrollmentInfo e = new EnrollmentInfo();
        e.status = "OK";
        e.coursesDisplayName = "Courses";
        e.orgsDisplayName = "Organisations";
        e.defaultLocale = "en_GB";
        e.assessments3_0 = false;
        e.courses = new TreeSet<SubjectInfo>();
        while (e.courses.size() < numSubj) {
            SubjectInfo s = getRandomSubjectInfo(numLect);
            e.courses.add(s);
        }
        return e;
    }

    public static SubjectInfo getRandomSubjectInfo(int numLect) {
        SubjectInfo s = new SubjectInfo();
        s.bbid = "_" + rs(6) + "_1";
        s.name = "[" + rs(4) + ri(4) + "]" + rs(10) + " " + rs(10) + " " + rs(10);
        s.courseid = rs(4) + ri(4) + "S_" + ri(4) + "STx";
        s.role = "Courses in which you are enrolled:";
        s.isAvail = true;
        s.locale = "en_GB";
        s.enrollmentdate = rd(2013);
        s.roleIdentifier = "S";
        s.data = getRandomSubjectData(numLect);
        return s;
    }

    public static SubjectData getRandomSubjectData(int numLect) {
        SubjectData sd = new SubjectData();
        sd.status = "OK";
        sd.rooturl = ROOT_URL;
        sd.lectures = new TreeSet<LectureData>();
        while (sd.lectures.size() < numLect) {
            LectureData ld = getRandomLectureData();

            sd.lectures.add(ld);
        }
        return sd;
    }

    public static LectureData getRandomLectureData() {
        LectureData ld = new LectureData();
        ld.contentid = "_" + rs(7) + "_1";
        ld.datemodified = rd(2013);
        ld.name = ld.datemodified + " - " + "[" + rs(4) + ri(4) + "]" + rs(10) + " " + rs(10) + " " + rs(10);
        ld.viewurl = "/" + rs(20) + "/" + rs(10) + "/" + rs(5) + "/" + rs(7) + ".jsp?" + rs(5) + "=" + rs(5);
        ld.isAvail = true;
        ld.linktype = "resource/x-apreso";
        ld.isfolder = false;
        ld.lc = new LectureContent();
        ld.lc.rooturl = ROOT_URL;
        ld.lc.status = "OK";
        ld.lc.content = new LectureBodyContent();
        ld.lc.content.title = ld.name;
        ld.lc.content.contenthandler = "resource/x-apreso";
        ld.lc.content.mobileFriendlyUrl = "";
        ld.lc.content.body = new LectureBodyContent.LectureBody();
        ld.lc.content.body.captureTime = ld.datemodified;
        ld.lc.content.body.href = "http://google.com/not/a/url/that/exists/" + rs(20) + "/" + rs(10) + "/" + rs(5) + "/" + rs(7);
        ld.lc.content.body.id = StringEscapeUtils.escapeHtml4(ld.lc.content.body.href);
        ld.lc.content.body.name = ld.lc.content.title;
        return ld;
    }

    private static int rn(int max) {
        return (int) (Math.random() * max);
    }

    private static String ri(int chars) {
        return RandomStringUtils.random(chars, false, true);
    }

    private static String rs(int num) {
        return RandomStringUtils.random(num, true, false);
    }

    private static Date rd(int year) {
        Calendar c = Calendar.getInstance();
        int day = rn(28);
        int mon = rn(12);
        c.set(year, mon, day, rn(24), rn(60));
        return c.getTime();
    }
}
