/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.model.formatter.DownloadType;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectMapItem;

/**
 *
 * @author lee
 */
public abstract class BlackboardLectureItemParser {

    private static final ImmutableMap<DownloadType, String> DOWNLOAD_TYPE_EXTENSIONS = ImmutableMap
            .<DownloadType, String>builder()
            .put(DownloadType.AUDIO, "mp3")
            .put(DownloadType.VIDEO, "m4v")
            .build();

    public abstract BlackboardLectureMapping getLecturesIn(URI baseUri,
                                                           BlackboardSubjectMapItem bsmi,
                                                           ImmutableSemester semester,
                                                           ImmutableSubject subject) throws DLectException;

    protected static Map<DownloadType, ImmutableLectureDownload> getDownloadType(URI url) throws DLectException {
        return getDownloadType(url, true);
    }

    /**
     *
     * @param url
     * @param encoded {@code true} if the function should append URL encoded strings({@code /} will be appended as it's
     *                encoded format).
     *
     * @return
     *
     * @throws DLectException
     */
    protected static Map<DownloadType, ImmutableLectureDownload> getDownloadType(URI url, boolean encoded) throws DLectException {
        String urlPostfix = (encoded ? "%2Fmedia." : "/media."); // <= URL Encoded "/media." if needed

        Map<DownloadType, ImmutableLectureDownload> map = Maps.newHashMap();

        for (Entry<DownloadType, String> entry : DOWNLOAD_TYPE_EXTENSIONS.entrySet()) {
            DownloadType dt = entry.getKey();
            String ext = entry.getValue();

            try {
                map.put(dt, new ImmutableLectureDownload(
                        new URL(url.toString() + urlPostfix + ext).toURI(), ext, false, false));
            } catch (MalformedURLException | URISyntaxException ex) {
                throw new DLectException(DLectExceptionCause.ILLEGAL_SERVICE_RESPONCE, ex);
            }
        }

        return map;
    }
}
