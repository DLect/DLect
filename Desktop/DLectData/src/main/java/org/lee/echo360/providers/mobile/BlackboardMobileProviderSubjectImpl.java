/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.ClientProtocolException;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Semester;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.SubjectLocator;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.StringUtil;
import org.lee.echo360.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class BlackboardMobileProviderSubjectImpl implements SubjectLocator {

    private final MobileProviderLocaliser localiser;
    private final MobileXmlProvider xmlProvider;

    public BlackboardMobileProviderSubjectImpl(MobileXmlProvider get, MobileProviderLocaliser mpl) {
        this.xmlProvider = get;
        this.localiser = mpl;
    }

    @Override
    public ActionResult getSubjects(Blackboard b) {
        try {
            Document doc = xmlProvider.getSubjects();
            if (!doc.getDocumentElement().getAttribute("status").equalsIgnoreCase("OK")) {
                return ActionResult.NOT_LOGGED_IN;
            }
            List<Node> childNodes = XMLUtil.getListByXPath(doc, "//course");
            List<SubjectInfo> subjects = new ArrayList<SubjectInfo>();
            readSubjectsFromNodes(childNodes, subjects, b);
            for (SubjectInfo si : subjects) {
                si.addToBlackboard(b);
            }
            return ActionResult.SUCCEDED;
        } catch (SAXException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (ParserConfigurationException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (ClientProtocolException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (MalformedURLException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (URISyntaxException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        }
    }

    private void readSubjectsFromNodes(List<Node> childNodes, List<SubjectInfo> subjects, Blackboard b) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        for (Node node : childNodes) {
            String bbid = XMLUtil.getTextByXPath(node, ".//@bbid");
            String bbName = XMLUtil.getTextByXPath(node, ".//@name");
            Date enrollmentDate = StringUtil.parseDate(
                    XMLUtil.getTextByXPath(node, ".//@enrollmentdate"));
            String courseid = XMLUtil.getTextByXPath(node, ".//@courseid");

            if (!isIn(subjects, bbid, courseid) &&
                    valid(bbid, bbName, courseid, enrollmentDate)) {

                String name = localiser.getCourseName(bbName);
                int semesterNum = localiser.getSemesterNumber(bbName, courseid, bbid, enrollmentDate);

                if (valid(name) && semesterNum != Integer.MIN_VALUE) {
                    String fileName = localiser.getCourseFileName(bbName, name);
                    if (valid(fileName)) {
                        SubjectInfo c = new SubjectInfo(name, bbid,
                                fileName, courseid, semesterNum);
                        subjects.add(c);
                    }
                }
            }
        }
    }

    private boolean isIn(List<SubjectInfo> subjects, String bbid, String courseid) {
        for (SubjectInfo subject : subjects) {
            if (subject.courseid.equals(courseid) && subject.bbid.equals(bbid)) {
                return true;
            }
        }
        return false;
    }

    private boolean valid(Object... vals) {
        for (Object v : vals) {
            if (v == null) {
                return false;
            }
            if (v instanceof String && ((String) v).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    class SubjectInfo {

        private final String bbid;
        private final String name;
        private final int semesterCode;
        private final String courseid;
        private final String folderName;

        public SubjectInfo(String name, String bbid, String fileName, String courseid, int semesterCode) {
            this.bbid = bbid;
            this.name = name;
            this.semesterCode = semesterCode;
            this.courseid = courseid;
            this.folderName = fileName;
        }

        private void addToBlackboard(Blackboard b) {
            for (Subject subject : b.getSubjects()) {
                if (subject.getCourseID().equals(courseid) && subject.getBlackboardId().equals(bbid)) {
                    return;// Subject already in BB.
                }
            }
            //Subject not in BB
            Semester semester = getSemester(semesterCode, b);
            if (semester != null) {
                Subject s = new Subject(name, bbid, folderName, courseid, semesterCode, semester);
                b.addSubject(s);
            }
        }

        private Semester getSemester(int semesterNum, Blackboard b) {
            String lName = localiser.getSemesterLongName(semesterNum);
            String cpn = localiser.getSemesterCoursePostfixName(semesterNum);
            return b.getSemester(semesterNum, lName, cpn);
        }
    }
}
