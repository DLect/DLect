/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dlect.model.helper.ImmutableDate;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlDateTypeAdapter;

/**
 *
 * @author lee
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardSubject {

    @XmlAttribute(name = "bbid")
    private String bbid;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "courseid")
    private String courseId;

    @XmlAttribute(name = "enrollmentdate")
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
