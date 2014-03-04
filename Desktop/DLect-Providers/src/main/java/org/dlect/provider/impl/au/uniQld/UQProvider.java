/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld;

import java.util.Date;
import java.util.Set;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.provider.base.blackboard.BlackboardBaseProvider;
import org.dlect.provider.impl.au.uniQld.rota.UQRotaHelper;

/**
 *
 * @author lee
 */
public class UQProvider extends BlackboardBaseProvider {

    public UQProvider() {
        this(new UQRotaHelper() {

            @Override
            public ImmutableSemester getSemester(int semCode) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode, Date lectureTime, String lectureRoom) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public UQProvider(UQRotaHelper helper) {
        super(921, new UQSubjectCustomiser(helper), new UQLectureCustomiser(helper));
    }

}
