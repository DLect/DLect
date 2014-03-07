/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.file;

import java.io.IOException;
import java.io.OutputStream;
import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import org.dlect.model.Subject;

/**
 *
 * @author lee
 */
public interface FileController {

    public OutputStream getStreamForDownload(Subject s, Lecture l, LectureDownload ld) throws IOException;

}
