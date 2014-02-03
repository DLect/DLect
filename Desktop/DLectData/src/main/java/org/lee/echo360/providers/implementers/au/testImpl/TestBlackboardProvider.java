/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.au.testImpl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.lee.echo360.providers.mobile.MobileProviderLocaliser;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.BlackboardProvider;
import org.lee.echo360.providers.LectureDownloader;
import org.lee.echo360.providers.LectureLocator;
import org.lee.echo360.providers.LoginExecuter;
import org.lee.echo360.providers.SubjectLocator;
import org.lee.echo360.providers.mobile.BlackboardMobileProvider;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.StringUtil;

public class TestBlackboardProvider implements BlackboardProvider,
        MobileProviderLocaliser,
        LectureDownloader {

    private static final Pattern semesterFromCourseIdRegex = Pattern.compile("[^_]*?_(\\d*)");
    private static final Pattern myCourseNameFromBbName = Pattern.compile("\\[(.*)\\]");
    private static final DateFormat lectureTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat unknowStreamDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int UQ_CLIENT_ID = 921;
    private static final LoadingCache<Subject, Map<Date, List<Integer>>> subjectLectures = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Subject, Map<Date, List<Integer>>>() {
        @Override
        public Map<Date, List<Integer>> load(Subject key) throws Exception {
            return TestUQRotaParser.getLectures(StringUtil.regex(key.getName(), "([A-Z]{4}[1-9][0-9]{3})", 1, ""), key.getSemesterCode());
        }
    });

    static {
        lectureTimeFormat.setTimeZone(TimeZone.getTimeZone("Australia/Brisbane"));
        unknowStreamDateFormat.setTimeZone(TimeZone.getTimeZone("Australia/Brisbane"));
    }
    private HttpClient httpClient = new SystemDefaultHttpClient();
    private BlackboardMobileProvider mobileProvider = null;

    public TestBlackboardProvider() {
    }

    @Override
    public String getProviderName() {
        return "UQ Test";
    }

    @Override
    public Blackboard createBlackboard() {
        return new Blackboard();
    }

    @Override
    public Image getProviderImage() {
        return null;
    }

    @Override
    public LoginExecuter getLoginExecuter() {
        if (createProvider()) {
            return mobileProvider.getLoginExecuter();
        }
        return null;
    }

    @Override
    public SubjectLocator getSubjectLocator() {
        if (createProvider()) {
            return mobileProvider.getSubjectLocator();
        }
        return null;
    }

    @Override
    public LectureLocator getLectureLocator() {
        if (createProvider()) {
            return mobileProvider.getLectureLocator();
        }
        return null;
    }

    private boolean createProvider() {
        if (mobileProvider != null) {
            return true;
        } else {
            if (BlackboardMobileProvider.checkClientIdSupported(UQ_CLIENT_ID)) {
                mobileProvider = BlackboardMobileProvider.createProvider(UQ_CLIENT_ID, this, httpClient);
                if (mobileProvider != null) {
                    httpClient = mobileProvider.getHttpClient();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String getCourseName(String name) {
        final Matcher m = myCourseNameFromBbName.matcher(name);
        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    @Override
    public int getSemesterNumber(String name, String courseId, String bbid, Date enrollmentdate) {
        final Matcher matcher = semesterFromCourseIdRegex.matcher(courseId);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            // Not a true course/not supported. discard this course.
            return Integer.MIN_VALUE;
        }
    }

    @Override
    public Date getLectureTime(URI u, String title, String xmlData) {
        String date = StringUtil.regex(xmlData, "Capture.*?: (.*?)<", 1, null);
        try {
            if (date != null) {
                synchronized (lectureTimeFormat) {
                    return lectureTimeFormat.parse(date);
                }
            } else {
                return null;
            }
        } catch (ParseException ex) {
            ExceptionReporter.reportException(ex, "The string \"" + date + "\" from \"" + title + "\" caused an error");
            return null;
        } catch (NumberFormatException ex) {
            ExceptionReporter.reportException(ex, "The string \"" + date + "\" from \"" + title + "\" caused an error");
            return null;
        }
    }

    @Override
    public List<Stream> getLectureStream(URI u, String title, String rawXml, Subject s) {
        try {
            final Map<Date, List<Integer>> get = subjectLectures.get(s);
            final Date lectureTime = getLectureTime(u, title, rawXml);
            final List<Integer> l = get.get(lectureTime);

            final List<Stream> ss = new ArrayList<Stream>();
            if (l != null) {
                for (Integer i : l) {
                    ss.add(s.getStream("L" + (i == 0 ? "" : i), i));
                }
            }
            if (ss.isEmpty()) {
                synchronized (unknowStreamDateFormat) {
                    ss.add(s.getStream(unknowStreamDateFormat.format(lectureTime), (int) lectureTime.getTime(), false));
                }
            }
            return ss;
        } catch (ExecutionException ex) {
            ExceptionReporter.reportException(ex);
            return Collections.emptyList();
        }
    }

    @Override
    public LectureDownloader getLectureDownloader() {
        return this;
    }

    @Override
    public String getVideoFileName(URI u, String title, Date d, Subject sub, List<Stream> streams) {
        return sub.getFolderName() + StringUtil.formatDate(" ~ yyyy-MM-dd HH.mm.'m4v'", d);
    }

    @Override
    public String getAudioFileName(URI u, String title, Date d, Subject sub, List<Stream> streams) {
        return sub.getFolderName() + StringUtil.formatDate(" ~ yyyy-MM-dd HH.mm.'mp3'", d);
    }

    @Override
    public String getCourseFileName(String name, String courseName) {
        return StringUtil.regex(courseName, "([A-Z]{4}[1-9][0-9]{3})", 1, courseName.replace("/", " ").replace("\\", " "));
    }

    @Override
    public ActionResult downloadLectureTo(Blackboard b, Subject s, Lecture l, DownloadType dt, File f) throws IOException {

        if (f.exists()) {
            return ActionResult.SUCCEDED;
        }

        long time = (long) (Math.random() * 2000 + Math.random() * 2000 + 100);
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
        }
        IOUtils.write("This is a test File, It has been downloaded", new FileOutputStream(f));
        return ActionResult.SUCCEDED;
    }

    @Override
    public String getSemesterCoursePostfixName(int semesterNum) {
        return TestUQRotaParser.getSemesterCoursePostfixName(semesterNum);
    }

    @Override
    public String getSemesterLongName(int semesterNum) {
        return TestUQRotaParser.getSemesterLongName(semesterNum);
    }

    private String getSuffix(DownloadType dt) {
        switch (dt) {
            case AUDIO:
                return "mp3";
            case VIDEO:
                return "m4v";
            default:
                return "???";
        }
    }
}
