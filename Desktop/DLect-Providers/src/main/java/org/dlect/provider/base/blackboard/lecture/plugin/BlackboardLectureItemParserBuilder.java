/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;

/**
 *
 * @author lee
 */
public abstract class BlackboardLectureItemParserBuilder {

    public final List<BlackboardLectureItemParser> of(BlackboardLectureItemParser... parsers) {
        return ImmutableList.copyOf(parsers);
    }

    public final List<BlackboardLectureItemParser> build(BlackboardHttpClient c) {
        Collection<BlackboardLectureItemParser> unsafe = buildParsers(c);
        if (unsafe == null) {
            return ImmutableList.of();
        }
        try {
            return ImmutableList.copyOf(unsafe);
        } catch (NullPointerException e) {
            return ImmutableList.of();
        }
    }

    protected abstract Collection<BlackboardLectureItemParser> buildParsers(BlackboardHttpClient c);
}
