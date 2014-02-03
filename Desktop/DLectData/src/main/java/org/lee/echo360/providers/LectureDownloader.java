/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers;

import java.io.File;
import java.io.IOException;
import org.lee.echo360.model.ActionResult;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.model.DownloadType;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;

/**
 *
 * @author lee
 */
public interface LectureDownloader {

    public ActionResult downloadLectureTo(Blackboard b, Subject s, Lecture l, DownloadType dt, File f) throws IOException;
}
