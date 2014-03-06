/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider;

import java.io.InputStream;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableLectureDownload;

/**
 *
 * @author lee
 */
public interface DownloadProvider {

    public InputStream getDownloadSreamFor(ImmutableLectureDownload lectureDownload) throws DLectException;

}
