/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl;

import com.google.common.base.Optional;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;

/**
 *
 * @author lee
 */
public interface BlackboardLectureCustomiser {

    public Optional<Date> getLectureTime(URI u, String title, String captureDate);

    public Collection<ImmutableStream> getLectureStream(URI u, String title, Date lectureTime, ImmutableSemester sem, ImmutableSubject s);
}
