/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl.echo;

import java.util.Collection;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;

/**
 *
 * @author lee
 */
public interface EchoCenterLectureCustomiser {

    public Collection<ImmutableStream> getStreamsFor(EchoCenterLecture lecture, ImmutableSemester sem, ImmutableSubject s);

}
