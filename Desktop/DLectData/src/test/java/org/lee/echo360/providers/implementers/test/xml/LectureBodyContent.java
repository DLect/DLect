/* * To change this template, choose Tools | Templates * and open the template in the editor. */
package org.lee.echo360.providers.implementers.test.xml;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;

/**
 * * * @author lee
 */
public class LectureBodyContent {

    private static final DateFormat lectureTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        LectureBody.fromHtml("");
    }
    @XmlAttribute(name = "title")
    public String title;
    @XmlAttribute(name = "contenthandler")
    public String contenthandler;
    @XmlAttribute(name = "mobileFriendlyUrl")
    public String mobileFriendlyUrl;
    @XmlElement(name = "body")
    public LectureBody body;

    public static class LectureBody {

        @XmlTransient
        public Date captureTime;
        @XmlTransient
        public String name;
        @XmlTransient
        public String id;
        @XmlTransient
        public String href;

        public static LectureBody fromHtml(String html) {
            String regex = "<div class=\"vtbegenerated\">\\s*" + "<div version=\"1.0\">\\s*" + "Capture Date/Time:\\s*" + "(.*?)\\s*" + "<br\\s*/>\\s*" + "<a\\s+" + "id=\"(.*?)\"\\s+" + "href=\"(.*?)\"\\s+" + "target=\"_blank\">\\s*" + "(.*?)\\s*" + "</a>";
            final Matcher matcher = Pattern.compile(regex).matcher(html);
            try {
                if (matcher.find()) {
                    LectureBody lb = new LectureBody();
                    lb.captureTime = lectureTimeFormat.parse(matcher.group(1));
                    lb.id = matcher.group(2);
                    lb.href = matcher.group(3);
                    lb.name = matcher.group(4);
                    return lb;
                }
            } catch (ParseException ex) {
                Logger.getLogger(LectureBodyContent.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        public String toHtml() {
            return "<div class=\"vtbegenerated\"><div version=\"1.0\">" + "Capture Date/Time: " + lectureTimeFormat.format(captureTime) + "<br/><a id=\"" + id + "\" href=\"" + href + "\" target=\"_blank\"> " + name + " </a> <br/> <a id=\"" + id + "%2Fmedia.mp3\" href=\"" + href + "%2Fmedia.mp3\" target=\"_blank\"> Download Lecture Audio </a> <br/> <a id=\"" + id + "%2Fmedia.m4v\" href=\"" + href + "%2Fmedia.m4v\" target=\"_blank\"> Download Lecture Video </a> </div> </div>";
        }

        @XmlCDATA
        public String getHtml() {
            return toHtml();
        }

        public void setHtml(String html) {
            LectureBody l = fromHtml(html);
            this.captureTime = l.captureTime;
            this.href = l.href;
            this.id = l.id;
            this.name = l.name;
        }
    }
}
