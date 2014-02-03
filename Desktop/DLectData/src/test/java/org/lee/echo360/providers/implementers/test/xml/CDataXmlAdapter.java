/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;

/**
 *
 * @author lee
 */
public class CDataXmlAdapter extends XmlAdapter<String, LectureBodyContent.LectureBody> {

    public CDataXmlAdapter() {
    }

    @Override
    public String marshal(LectureBodyContent.LectureBody arg0) throws Exception {
        return "<![CDATA[" + arg0.toHtml() + "]]>";
    }

    @Override
    public LectureBodyContent.LectureBody unmarshal(String arg0) throws Exception {
        String ns;
        if (arg0.startsWith("<![CDATA[")) {
            ns = arg0.replace("<![CDATA[", "").replace("]]>", "");
        } else {
            ns = StringEscapeUtils.UNESCAPE_XML.with(new UnicodeUnescaper()).translate(arg0);
        }
        return LectureBodyContent.LectureBody.fromHtml(ns);
    }
}
