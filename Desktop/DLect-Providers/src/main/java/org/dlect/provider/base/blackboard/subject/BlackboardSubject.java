/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.subject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dlect.model.helper.ImmutableDate;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlDateTypeAdapter;

/**
 *
 * @author lee
 */
public class BlackboardSubject {

    @XmlElement(name = "bbid")
    private String bbid;

    @XmlElement(name = "name")
    private String name;

    @XmlElement(name = "courseid")
    private String courseId;

    @XmlElement(name = "enrollmentdate")
    @XmlJavaTypeAdapter(BlackboardXmlDateTypeAdapter.class)
    private ImmutableDate enrollmentDate;

    public String getBbid() {
        return bbid;
    }

    public String getName() {
        return name;
    }

    public String getCourseId() {
        return courseId;
    }

    public ImmutableDate getEnrollmentDate() {
        return ImmutableDate.of(enrollmentDate);
    }

}
