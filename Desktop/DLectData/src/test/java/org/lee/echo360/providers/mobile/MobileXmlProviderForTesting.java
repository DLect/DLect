/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.client.HttpClient;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.providers.implementers.test.xml.BlackboardMarshaller;
import org.lee.echo360.providers.implementers.test.xml.EnrollmentInfo;
import org.lee.echo360.providers.implementers.test.xml.LectureData;
import org.lee.echo360.providers.implementers.test.xml.SubjectData;
import org.lee.echo360.providers.implementers.test.xml.SubjectInfo;
import org.lee.echo360.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author lee
 */
public class MobileXmlProviderForTesting implements MobileXmlProvider {

    private final EnrollmentInfo ei;
    private final SortedSet<SubjectInfo> sis;
    private final SortedSet<SubjectData> sds;
    private final SortedSet<LectureData> lds;

    public MobileXmlProviderForTesting(EnrollmentInfo ei) {
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
        Iterables.addAll(lds, Iterables.concat(Iterables.transform(sis, new Function<SubjectInfo, Collection<LectureData>>() {
            @Override
            public Collection<LectureData> apply(SubjectInfo input) {
                return input.data.lectures;
            }
        })));
    }

    @Override
    public Document getLoginResponce(Blackboard b) throws IOException, SAXException, ParserConfigurationException {
        return XMLUtil.getDocumentFromStream(
                new ByteArrayInputStream(
                ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<mobileresponse status=\"OK\" userid=\"_345424_1\"/>").getBytes()));
    }

    @Override
    public Document getSubjects() throws IOException, SAXException, ParserConfigurationException {
        return XMLUtil.getDocumentFromStream(
                new ByteArrayInputStream(
                BlackboardMarshaller.saveEnrollmentInfo(ei).getBytes()));
    }

    @Override
    public String getLectureContentString(String subjCode, String contentID) throws IOException {
        for (SubjectInfo s : sis) {
            if (subjCode.equals(s.bbid)) {
                SortedSet<LectureData> ld = s.data.lectures;
                for (LectureData l : ld) {
                    if (l.contentid.equals(contentID)) {
                        return BlackboardMarshaller.saveLectureContent(l.lc);
                    }
                }
            }
        }
        throw new IOException("No lecture found for: \"" + subjCode + "\" \"" + contentID + "\"");
    }

    @Override
    public Document getLectureXML(String code) throws ParserConfigurationException, IllegalStateException, IOException, SAXException {
        for (SubjectInfo s : sis) {
            if (s.bbid.equals(code)) {
                return XMLUtil.getDocumentFromStream(
                        new ByteArrayInputStream(
                        BlackboardMarshaller.saveSubjectData(s.data).getBytes()));
            }
        }
        throw new IOException("No lecture found for: \"" + code + "\"");
    }

    @Override
    public URL getRootURL(String thisRootUrl) throws MalformedURLException {
        try {
            return new URL(thisRootUrl);
        } catch (MalformedURLException e) {
            return new URL("http://www.google.com/");
        }
    }

    @Override
    public HttpClient getClient() {
        return null;
    }
}
