/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "mobileresponse")
public class LectureContent {
    /*
     * <mobileresponse status="OK" rooturl="https://learn.uq.edu.au"><content 
     *  title="2013-05-28 16:00:00 - Calculus &amp; Linear Algebra II - Tue 16:00 Rm: STLUCIA 50-S201" 
     *  contenthandler="resource/x-apreso" mobileFriendlyUrl=""><body><![CDATA[<div class="vtbegenerated"><div version="1.0">Capture Date/Time: 2013-05-28 16:00:00<br/><a id="http%3A%2F%2Flecture.recordings.uq.edu.au%3A8080%2Fess%2Fecho%2Fpresentation%2F097e168f-78d9-4c77-a67c-0d13f58e54a8" href="/webapps/any-acrbb-BBLEARN//seamless?id=_287039_1&url=http%3A%2F%2Flecture.recordings.uq.edu.au%3A8080%2Fess%2Fecho%2Fpresentation%2F097e168f-78d9-4c77-a67c-0d13f58e54a8" target="_blank">Calculus & Linear Algebra II - Tue 16:00 Rm: STLUCIA 50-S201</a><br/><a id="http%3A%2F%2Flecture.recordings.uq.edu.au%3A8080%2Fess%2Fecho%2Fpresentation%2F097e168f-78d9-4c77-a67c-0d13f58e54a8%2Fmedia.mp3" href="/webapps/any-acrbb-BBLEARN//seamless?id=_287039_1&url=http%3A%2F%2Flecture.recordings.uq.edu.au%3A8080%2Fess%2Fecho%2Fpresentation%2F097e168f-78d9-4c77-a67c-0d13f58e54a8%2Fmedia.mp3" target="_blank">Download Lecture Audio</a><br/><a id="http%3A%2F%2Flecture.recordings.uq.edu.au%3A8080%2Fess%2Fecho%2Fpresentation%2F097e168f-78d9-4c77-a67c-0d13f58e54a8%2Fmedia.m4v" href="/webapps/any-acrbb-BBLEARN//seamless?id=_287039_1&url=http%3A%2F%2Flecture.recordings.uq.edu.au%3A8080%2Fess%2Fecho%2Fpresentation%2F097e168f-78d9-4c77-a67c-0d13f58e54a8%2Fmedia.m4v" target="_blank">Download Lecture Video</a></div></div>]]></body></content></mobileresponse>
     */

    @XmlAttribute(name = "status")
    public String status;
    @XmlAttribute(name = "rooturl")
    public String rooturl;
    @XmlElement(name = "content")
    public LectureBodyContent content;
}
