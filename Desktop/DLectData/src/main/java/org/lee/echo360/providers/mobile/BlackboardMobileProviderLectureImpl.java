/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Stream;
import org.lee.echo360.model.Subject;
import org.lee.echo360.providers.LectureLocator;
import org.lee.echo360.util.ExceptionReporter;
import org.lee.echo360.util.StringUtil;
import org.lee.echo360.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class BlackboardMobileProviderLectureImpl implements LectureLocator {

    private final MobileXmlProvider xmlProvider;
    private final MobileProviderLocaliser localiser;

    public BlackboardMobileProviderLectureImpl(MobileXmlProvider get, MobileProviderLocaliser mpl) {
        this.xmlProvider = get;
        this.localiser = mpl;
    }

    @Override
    public ActionResult getLecturesIn(final Blackboard b, final Subject s) {
        try {
            final String code = URLEncoder.encode(s.getBlackboardId(), "UTF-8");
            Document doc = xmlProvider.getLectureXML(code);

            List<Node> possibleLectureNodes = XMLUtil.getListByXPath(doc, "mobileresponse//map-item[@isAvail='true' and @linktype='resource/x-apreso']");
            for (final Node node : possibleLectureNodes) {
                final Element e = (Element) node;
                final String cid = e.getAttribute("contentid");
                if (!subjectContainsLecture(s, code, cid)) {
                    try {
                        getLectureContent(code, cid, s);
                    } catch (IOException ex) {
                        ExceptionReporter.reportException(ex);
                    }
                }
            }
            return ActionResult.SUCCEDED;
        } catch (SAXException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (ParserConfigurationException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (IOException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        } catch (IllegalStateException ex) {
            ExceptionReporter.reportException(ex);
            return ActionResult.FAILED;
        }
    }

    private void getLectureContent(String subjCode, String contentID, Subject s) throws IOException {
        String rep = xmlProvider.getLectureContentString(subjCode, contentID);
        URL root = xmlProvider.getRootURL(StringUtil.regex(rep, "rooturl=\"(.*?)\"", 1, null));
        String title = StringUtil.regex(rep, "<content title=\"(.*?)\"", 1, null);
        String href = StringEscapeUtils.unescapeHtml4(StringUtil.regex(rep, "<!\\[CDATA\\[.*?<a id=\".*?\" href=\"(.*?)\"", 1, null));
        try {
            if (title != null && href != null) {
                URI u = new URL(root, href).toURI();
                Date recDate = localiser.getLectureTime(u, title, rep);
                if (recDate != null) {
                    List<Stream> stream = localiser.getLectureStream(u, title, rep, s);
                    if (stream == null || stream.isEmpty()) {
                        stream = new ArrayList<Stream>();// TODO
                        stream.add(s.getStream("Other", Integer.MIN_VALUE, false));
                    }
                    String videoName = localiser.getVideoFileName(u, title, recDate, s, stream);
                    String audioName = localiser.getAudioFileName(u, title, recDate, s, stream);
                    Lecture l = new Lecture(u, subjCode, contentID, recDate, stream, videoName, audioName);
                    s.addLecture(l);
                } else {
                    System.out.println("Rec Date not valid for: " + contentID);
                    System.out.println("\tDate : " + recDate);
                }
            } else {
                System.out.println("Title or Href not valid for: " + contentID);
                System.out.println("\tTitle: " + title);
                System.out.println("\tHref : " + href);
            }
        } catch (URISyntaxException ex) {
            ExceptionReporter.reportException(ex);
        }
    }

    private boolean subjectContainsLecture(Subject s, String code, String cid) {
        for (Lecture l : s.getLectures()) {
            if (l.getContentID().equals(cid) && l.getSubjectCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
