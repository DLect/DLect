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

    public List<BlackboardLectureItemParser> of(BlackboardLectureItemParser... parsers) {
        return ImmutableList.copyOf(parsers);
    }

    public abstract Collection<BlackboardLectureItemParser> buildParsers(BlackboardHttpClient c);
}
