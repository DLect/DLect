/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.au.uniqld;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.lee.echo360.providers.implementers.au.testImpl.TestUQRotaParser;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.StringUtil;
import org.lee.echo360.util.XMLUtil;
import static org.lee.echo360.util.XMLUtil.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Lee Symes
 */
public class UQRotaParser {

    private static final TimeZone UQ_TIME_ZONE = TimeZone.getTimeZone("Australia/Brisbane");
    private static final URL coursePrefix = create("http://rota.eait.uq.edu.au/course/");
    private static final URL offeringPrefix = create("http://rota.eait.uq.edu.au/offering/");
    private static final URL semesterPrefix = create("http://rota.eait.uq.edu.au/semester/");
    private static final LoadingCache<Integer, Sem> semesterInformationCache =
            CacheBuilder.newBuilder().softValues().build(new CacheLoader<Integer, Sem>() {
        @Override
        public Sem load(Integer key) throws Exception {
            return getSemester(key);
        }
    });

    private static URL create(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            ExceptionReporter.reportException(ex);
            return null;
        }
    }

    protected static Map<Date, List<Integer>> getLectures(String courseName, int semesterId) {
        Map<Integer, List<Date>> lec = getLectures_(courseName, semesterId);
        Map<Date, List<Integer>> ret = new HashMap<Date, List<Integer>>();
        for (Entry<Integer, List<Date>> entry : lec.entrySet()) {
            Integer integer = entry.getKey();
            List<Date> list = entry.getValue();
            for (Date date : list) {
                if (ret.containsKey(date)) {
                    ret.get(date).add(integer);
                } else {
                    ret.put(date, new ArrayList<Integer>(Arrays.asList(integer)));
                }
            }
        }
        return ret;
    }

    private static Map<Integer, List<Date>> getLectures_(String courseName, int semesterId) {
        Map<Integer, List<Date>> lectureTimes = new HashMap<Integer, List<Date>>();
        try {
            URL u = new URL(coursePrefix, courseName + ".xml");
            Document doc = getDocumentFromStream(openStreamFromURL(u));

            List<Node> offerings = XMLUtil.getListByXPath(doc, "course/offerings/offering");
            for (Node node : offerings) {

                String offeringID = XMLUtil.getTextByXPath(node, "./semester/id[text()=\"" + semesterId + "\"]/../../id/text()");
                if (offeringID == null || offeringID.isEmpty()) {
                    continue;
                }
                getStuff(offeringID, lectureTimes);
            }
        } catch (SAXException ex) {
            ExceptionReporter.reportException(ex);
        } catch (ParserConfigurationException ex) {
            ExceptionReporter.reportException(ex);
        } catch (MalformedURLException ex) {
            ExceptionReporter.reportException(ex);
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
        }
        return lectureTimes;
    }

    private static InputStream openStreamFromURL(URL u) throws IOException {
        HttpURLConnection conn;
        for (int i = 0; i < 10; i++) {
            conn = (HttpURLConnection) u.openConnection();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                return conn.getInputStream();
            }
        }
        throw new IOException("(See Output for More Info) Open Stream Failed for URL: " + u);
    }

    private static void getStuff(String offeringID, Map<Integer, List<Date>> lectureTimes) {
        try {
            URL u = new URL(offeringPrefix, offeringID + ".xml");
            Document doc = getDocumentFromStream(openStreamFromURL(u));
            List<Node> series = getListByXPath(doc, "/offering/series/series");
            List<Node> groups = null;
            for (Node node : series) {
                if (getTextByXPath(node, "name/text()").equalsIgnoreCase("L")) {
                    groups = getListByXPath(node, "groups/group");
                    break;
                }
            }
            if (groups == null) {
                return;
            }
            Calendar c = Calendar.getInstance(UQ_TIME_ZONE);
            c.setLenient(true);
            for (Node group : groups) {
                List<Date> dateTimes = new ArrayList<Date>();
                int num = (int) getLongByXPath(group, "name/text()");
                List<Node> sessions = getListByXPath(group, "sessions/session");
                for (Node sess : sessions) {
                    c.clear();
                    c.add(Calendar.MINUTE, (int) getLongByXPath(sess, "startmins"));
                    List<Node> evts = getListByXPath(sess, "events/event");
                    for (Node evt : evts) {
                        if (getBooleanByXPath(evt, "@taught")) {
                            String s = getTextByXPath(evt, "@date");
                            c.set(Calendar.YEAR, StringUtil.parseInteger(s.substring(0, 4)));
                            c.set(Calendar.MONTH, StringUtil.parseInteger(s.substring(5, 7)) - 1);
                            c.set(Calendar.DAY_OF_MONTH, StringUtil.parseInteger(s.substring(8, 10)));
                            dateTimes.add(c.getTime());
                        }
                    }
                }
                Collections.sort(dateTimes);
                lectureTimes.put(num, dateTimes);
            }
        } catch (SAXException ex) {
            ExceptionReporter.reportException(ex);
        } catch (ParserConfigurationException ex) {
            ExceptionReporter.reportException(ex);
        } catch (MalformedURLException ex) {
            ExceptionReporter.reportException(ex);
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
        }
    }

    protected static String getSemesterCoursePostfixName(int semNum) {
        return semesterInformationCache.getUnchecked(semNum).cpn;
    }

    protected static String getSemesterLongName(int semNum) {
        return semesterInformationCache.getUnchecked(semNum).ln;
    }

    private static Sem getSemester(int semesterNum) {
        try {
            URL u = new URL(semesterPrefix, semesterNum + ".xml");
            Document doc = getDocumentFromStream(openStreamFromURL(u));

            String name = getTextByXPath(doc, "semester/name");
            int semNum = (int) getLongByXPath(doc, "semester/number");
            int semYear = (int) getLongByXPath(doc, "semester/year");

            return new Sem(name, "Sem " + semNum + ", " + semYear);
        } catch (IOException ex) {
            Logger.getLogger(TestUQRotaParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(TestUQRotaParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TestUQRotaParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; //Exception thrown case
    }

    private static class Sem {

        private String cpn;
        private String ln;

        public Sem(String cpn, String ln) {
            this.cpn = cpn;
            this.ln = ln;
        }
    }
}
