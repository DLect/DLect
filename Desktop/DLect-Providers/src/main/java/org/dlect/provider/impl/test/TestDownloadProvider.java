/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableLectureDownload;
import org.dlect.provider.DownloadProvider;

/**
 *
 * @author lee
 */
public class TestDownloadProvider implements DownloadProvider {

    @Override
    public InputStream getDownloadStreamFor(ImmutableLectureDownload lectureDownload) throws DLectException {

        return new ByteArrayInputStream(lectureDownload.toString().getBytes());
    }

}
