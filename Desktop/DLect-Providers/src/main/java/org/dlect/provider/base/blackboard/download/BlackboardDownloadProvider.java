/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.download;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.provider.DownloadProvider;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;


/**
 *
 * @author lee
 */
public class BlackboardDownloadProvider implements DownloadProvider {

    private static final Pattern DOWNLOAD_URL_PATTERN = Pattern.compile("<embed.*?src=\"([^\"]*?)\"", Pattern.DOTALL);

    private final BlackboardHttpClient httpClient;

    public BlackboardDownloadProvider(BlackboardHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public InputStream getDownloadStreamFor(ImmutableLectureDownload lectureDownload) throws DLectException {
        String responce;
        try (InputStream r = httpClient.doGet(lectureDownload.getDownloadURL())) {
            responce = CharStreams.toString(new InputStreamReader(r, Charsets.UTF_8));
        } catch (IOException ex) {
            throw new DLectException(DLectExceptionCause.NO_CONNECTION,
                                     "Failed to get lecture page.",
                                     ex);
        }

        Matcher m = DOWNLOAD_URL_PATTERN.matcher(responce);

        if (m.find()) {
            String uri = m.group(1);
            try {
                return httpClient.doGet(new URI(uri));
            } catch (IOException ex) {
                throw new DLectException(DLectExceptionCause.NO_CONNECTION,
                                         "Error Accessing " + uri,
                                         ex);
            } catch (URISyntaxException ex) {
                throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE,
                                         "Error Parsing URI for " + uri,
                                         ex);
            }
        } else {
            throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE,
                                     "Failed to parse page at " + lectureDownload.getDownloadExtension());
        }
    }

}
