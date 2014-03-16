/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld;

import com.google.common.base.Optional;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.base.blackboard.BlackboardSubjectCustomiser;
import org.dlect.provider.impl.au.uniQld.rota.UQRotaHelper;

/**
 *
 * @author lee
 */
public class UQSubjectCustomiser implements BlackboardSubjectCustomiser {

    private static final Pattern SEMESTER_ID_FROM_COURSE_ID_REGEX = Pattern.compile("_(\\d*?)_");
    private static final Pattern COURSE_NAME_FROM_BLACKBOARD_NAME_REGEX = Pattern.compile("^\\[(.*?)\\]");

    private final UQRotaHelper helper;

    public UQSubjectCustomiser(UQRotaHelper helper) {
        this.helper = helper;
    }

    @Override
    public ImmutableSemester getSemesterFor(String name, String courseId, String bbid, Date enrollmentDate) {
        final Matcher matcher = SEMESTER_ID_FROM_COURSE_ID_REGEX.matcher(courseId);
        matcher.find();
        return helper.getSemester(Integer.parseInt(matcher.group(1)));
    }

    @Override
    public Optional<String> getSubjectName(String name, String courseId, String bbid) {
        final Matcher matcher = COURSE_NAME_FROM_BLACKBOARD_NAME_REGEX.matcher(name);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        } else {
            return Optional.absent();
        }
    }

}
