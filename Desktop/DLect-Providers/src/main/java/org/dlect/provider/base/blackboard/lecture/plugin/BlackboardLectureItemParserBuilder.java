/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;

/**
 *
 * @author lee
 */
public abstract class BlackboardLectureItemParserBuilder {

    public final Set<BlackboardLectureItemParser> of(BlackboardLectureItemParser... parsers) {
        return ImmutableSet.copyOf(parsers);
    }

    public final Set<BlackboardLectureItemParser> build(BlackboardHttpClient c) {
        Set<BlackboardLectureItemParser> unsafe = buildParsers(c);
        if (unsafe == null) {
            return ImmutableSet.of();
        }
        Set<BlackboardLectureItemParser> copy = Sets.newHashSet(unsafe);
        copy.remove(null);
        return ImmutableSet.copyOf(copy);
    }

    protected abstract Set<BlackboardLectureItemParser> buildParsers(BlackboardHttpClient c);
}
