/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl;

import com.google.common.base.Optional
import com.google.common.collect.ImmutableList
import org.dlect.exception.DLectException
import org.dlect.exception.DLectExceptionCause
import org.dlect.immutable.model.ImmutableSemester
import org.dlect.immutable.model.ImmutableSubject
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient
import org.dlect.provider.base.blackboard.helper.httpclient.HttpResponceStream
import org.dlect.provider.base.blackboard.helper.xml.BlackboardLectureRecordingItem
import org.dlect.provider.base.blackboard.lecture.plugin.impl.BlackboardLecturePageParserImpl
import java.net.URI
import spock.lang.*
import static spock.lang.MockingApi.*
import static com.google.common.base.Charsets.UTF_8


class BlackboardLecturePageParserImplSpockTest extends Specification {
    
    BlackboardHttpClient httpClient = Mock(BlackboardHttpClient)
    BlackboardLecturePageParserImpl obj = new BlackboardLecturePageParserImpl(httpClient)
    
    def "getPageFor[...] Check that doGet is called on httpClient"() {
        setup:
        URI u = URI.create("http://google.com/")
        def ex = new IOException()
        when:
        obj.getPageFor(u)
        then:
        1 * httpClient.doGet(u) >> {throw ex}
        then:
        Exception e = thrown(DLectException)
        e.cause == ex
    }
    
    def "getPageFor[...] Returns empty list on invalid page."() {
        setup:
        URI u = URI.create("http://google.com/")
        when:
        def page = obj.getPageFor(u)
        then:
        1 * httpClient.doGet(u) >> toIs("Nope, no HTML here")
        [].equals(page.getItems())
    }
    
    def "getPageFor[...] Does not fail when given invalid list item"() {
        setup:
        URI u = URI.create("http://google.com/")
        when:
        def page = obj.getPageFor(u)
        then:
        1 * httpClient.doGet(u) >> toIs("<li id='contentListItem:000'>Content</li>")
        [].equals(page.getItems())
    }
    
    def "getPageFor[...] Creates a valid item."() {
        setup:
        URI u = URI.create("http://google.com/")
        def contentID = "000"
        def title = "Title"
        def date = "2000-01-01 00:00:00"
        def url = "test"
        def resulting_url = new URL(u.toURL(), url).toURI()
        when:
        def page = obj.getPageFor(u)
        then:
        1 * httpClient.doGet(u) >> toIs("<li id='contentListItem:$contentID'>\n\
                                         <span >$title</span>Captured: $date<br /><a href=\"$url\">html</a></li>")
        BlackboardLectureRecordingItem item = page.getItems().get(0)
        item.getContentId().equals(contentID)
        item.getTitle().equals(title)
        item.getCaptureDate().equals(date)
        item.getUrl().equals(resulting_url)
    }
    
    def "getPageFor[...] Errors for invalid url."() {
        setup:
        URI u = URI.create("http://google.com/")
        when:
        def page = obj.getPageFor(u)
        then:
        1 * httpClient.doGet(u) >> toIs("<li id='contentListItem:0233'>\n\
                                         <span >DDFFF</span>Captured: 00001<br /><a href=\"http:/dfjkhjdd98635%@#@###!@#\">html</a></li>")
        def ex = thrown(DLectException)
        ex.getCauseCode() == DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE
    }
    
    private HttpResponceStream toIs(String str) {
        return new HttpResponceStream(new ByteArrayInputStream(str.getBytes(UTF_8)), null, URI.create("http://www.google.com/"));
    }

}
