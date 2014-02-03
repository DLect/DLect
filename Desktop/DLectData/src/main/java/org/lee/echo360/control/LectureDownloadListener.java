/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lee.echo360.control;

import org.lee.echo360.model.Lecture;

/**
 *
 * @author lee
 */
public interface LectureDownloadListener {

    /**
     * Notify the listener that the specified Lecture has started downloaded.
     * @param l
     */
    public void started(Lecture l);

    /**
     * Notify the listener that the specified Lecture has completed downloading.
     * @param l
     */
    public void completed(Lecture l);

    /**
     * Notify the listener that the download of the given Lecture has failed with
     * the given Exception. This function has a similar meaning to
     * {@linkplain #completed(org.lee.echo360.model.Lecture)}.
     * @param l The lecture.
     * @param e The exception thrown, or a generic exception indicating failure.
     */
    public void failed(Lecture l, Exception e);

    /**
     * Notify the listener that progress has been made on downloading the lecure.
     * @param downloadedBytes The number of bytes currently downloaded.
     * @param downloadTime The number of milliseconds between starting the
     *                      download and when this method was called.
     * @param totalBytes The total number of bytes to be downloaded. This can
     *                      be -1, which means that this value is unknown.
     */
    public void progress(Lecture l, long downloadedBytes, long downloadTime, long totalBytes);
}
