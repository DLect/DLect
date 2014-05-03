/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import com.google.common.collect.ImmutableList;
import java.net.URI;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectContentListing;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectMapItem;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParser;
import org.dlect.provider.base.blackboard.lecture.plugin.BlackboardLectureItemParserBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author lee
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class BlackboardLectureProviderTest {

    private URI baseUrl = URI.create("Http://www.google.com/");
    @Mock
    private BlackboardHttpClient httpClient;
    @Mock
    private BlackboardXmlParser xmlParser;
    @Mock
    private BlackboardLectureItemParserBuilder builder;
    @Mock
    private BlackboardStreamProvider streamProvider;

    @InjectMocks
    private BlackboardLectureProvider testObject;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Ignore
    @Test
    public void testExamplar() throws Exception {
        Object o = mock(Object.class);
        assertNotNull(o);
        fail();
    }

    /**
     * Test of fillMultimap method, of class BlackboardLectureProvider.
     */
    @Ignore
    @Test
    public void testFillMultimap() {
    }

    /**
     * Test of getLecturesIn method, of class BlackboardLectureProvider.
     */
    @Test
    @Ignore
    public void testGetLecturesIn() throws Exception {

    }

    /**
     * Test of parseLectureItems method, of class BlackboardLectureProvider.
     */
    @Test
    public void testParseLectureItems() throws Exception {
        ImmutableSemester sem = new ImmutableSemester(1, "1", "11");
        ImmutableSubject sub = new ImmutableSubject("1", "2", empty(), empty());
        BlackboardSubjectContentListing bscl = mock(BlackboardSubjectContentListing.class);

        ImmutableList<BlackboardSubjectMapItem> items = ImmutableList.of(new BlackboardSubjectMapItem(),
                                                                         new BlackboardSubjectMapItem(),
                                                                         new BlackboardSubjectMapItem());

        ImmutableList<BlackboardLectureItemParser> parsers = ImmutableList.of(
                mock(BlackboardLectureItemParser.class),
                mock(BlackboardLectureItemParser.class),
                mock(BlackboardLectureItemParser.class));

        when(bscl.getAllItems()).thenReturn(items);
        when(streamProvider.getLectureStreamsFor(sem, sub)).thenReturn(empty());
        when(builder.buildParsers(httpClient)).thenReturn(parsers);

        testObject.parseLectureItems(bscl, baseUrl, sem, sub);

        InOrder i = inOrder(parsers.toArray());

        for (BlackboardLectureItemParser p : parsers) {
            System.out.println("Parser: " + p);
            for (BlackboardSubjectMapItem it : items) {
                System.out.println("Item: " + it);
                i.verify(p).getLecturesIn(baseUrl, it, sem, sub);
            }
            verifyNoMoreInteractions(p);
        }
    }

    @SuppressWarnings("rawtypes")
    private ImmutableList empty() {
        return ImmutableList.of();
    }

}
