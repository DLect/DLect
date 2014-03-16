/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.xml;

import java.net.URI;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardLectureRecordingItem {

    private final String contentId;
    private final String title;
    private final String captureDate;
    private final URI url;

    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public URI getUrl() {
        return url;
    }

    public String getCaptureDate() {
        return captureDate;
    }

    public BlackboardLectureRecordingItem(String contentId, String title, String captureDate, URI url) {
        this.contentId = contentId;
        this.title = title;
        this.captureDate = captureDate;
        this.url = url;
    }

}
