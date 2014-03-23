/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.collect.ImmutableSet;
import java.util.Date;
import java.util.Set;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.logging.ProviderLogger;

public class UQRotaHelperImpl implements UQRotaHelper {

    private static final ImmutableStream STREAM = new ImmutableStream("Unknown");
    private final UQRotaSemesterParser semesterParser;

    public UQRotaHelperImpl() {
        semesterParser = null; // TODO
    }

    @Override
    public ImmutableSemester getSemester(int semCode) {
        try {
            return semesterParser.getSemester(semCode);
        } catch (DLectException ex) {
            ProviderLogger.LOGGER.error("Failed to find semester information for code: " + semCode, ex);
            // Make a best guess about the semester information.
            return new ImmutableSemester(semCode, "Sem Code: " + semCode, Integer.toString(semCode));
        }
    }

    @Override
    public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode) {
        return ImmutableSet.of(STREAM);
    }

    @Override
    public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode, Date lectureTime, String lectureRoom) {
        return ImmutableSet.of(STREAM);
    }

}
