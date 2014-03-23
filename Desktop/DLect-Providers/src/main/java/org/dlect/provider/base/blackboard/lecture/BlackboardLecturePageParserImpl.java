/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture;

import org.dlect.provider.base.blackboard.helper.xml.BlackboardLectureRecordingPage;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardLectureRecordingItem;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;

public class BlackboardLecturePageParserImpl extends BlackboardLecturePageParser {

    private static final Pattern LECTURE_RECORDING_ITEM_REGEXP = Pattern.compile("<li\\s+id=[\"']contentListItem:(.*?)[\"'].*?>(.*?)</li>", Pattern.DOTALL);
    private static final Pattern LECTURE_RECORDING_INFORMATION = Pattern.compile("<span\\s+[^>]*?>([^<]*?)</span>.*?(\\d{4}.*?)<br.*?<a\\s+.*?href=\"([^\"]*?)\"", Pattern.DOTALL);

    public BlackboardLecturePageParserImpl(BlackboardHttpClient client) {
        super(client);
    }

    @Override
    public BlackboardLectureRecordingPage getPageFor(URI normal) throws DLectException {
        try (InputStream i = getClient().doGet(normal)) {
            String html = CharStreams.toString(new InputStreamReader(i, Charsets.UTF_8));

            Matcher m = LECTURE_RECORDING_ITEM_REGEXP.matcher(html);
            List<BlackboardLectureRecordingItem> items = Lists.newArrayList();
            while (m.find()) {
                String contentId = m.group(1);
                String itemHtml = m.group(2);

                items.add(parseItem(normal.toURL(), contentId, itemHtml));
            }

            return new BlackboardLectureRecordingPage(items);
        } catch (MalformedURLException ex) {
            ProviderLogger.LOGGER.error("Failed to get URL: " + normal, ex);
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex);
        } catch (IOException ex) {
            ProviderLogger.LOGGER.error("Failed to load page from uri: " + normal, ex);
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        }
    }

    public BlackboardLectureRecordingItem parseItem(URL base, String contentId, String itemHtml) throws DLectException {
        Matcher m = LECTURE_RECORDING_INFORMATION.matcher(itemHtml);
        if (!m.find()) {
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, "Recording information regular expression failed. Input: " + itemHtml);
        } else {
            try {
                String title = m.group(1);
                String captureDate = m.group(2);
                String urlBase = m.group(3);

                URI url = new URL(base, urlBase).toURI();
                return new BlackboardLectureRecordingItem(contentId, title, captureDate, url);
            } catch (MalformedURLException | URISyntaxException ex) {
                ProviderLogger.LOGGER.error("Failed to create url from matcher. " + contentId.replace('\n', ' '), ex);
                throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex);
            }
        }
    }
}
