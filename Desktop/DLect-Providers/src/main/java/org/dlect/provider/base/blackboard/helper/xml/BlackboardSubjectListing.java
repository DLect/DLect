/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.xml;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "mobileresponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class BlackboardSubjectListing {

    @XmlElementWrapper(name = "courses")
    @XmlElement(name = "course")
    private final Set<BlackboardSubject> courses = Sets.newHashSet();

    @XmlElementWrapper(name = "orgs")
    @XmlElement(name = "org")
    private final Set<BlackboardSubject> orgs = Sets.newHashSet();

    public Set<BlackboardSubject> getCourses() {
        return ImmutableSet.copyOf(courses);
    }

    public Set<BlackboardSubject> getOrgs() {
        return ImmutableSet.copyOf(orgs);
    }

    public Set<BlackboardSubject> getAllSubjects() {
        return ImmutableSet.<BlackboardSubject>builder().addAll(courses).addAll(orgs).build();
    }

}
