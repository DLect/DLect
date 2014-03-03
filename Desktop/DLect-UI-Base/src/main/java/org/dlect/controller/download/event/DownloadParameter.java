/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.download.event;

import com.google.common.base.Objects;
import javax.annotation.Nonnull;
import org.dlect.model.Lecture;
import org.dlect.model.Subject;
import org.dlect.model.formatter.DownloadType;

import static org.dlect.helper.Conditions.checkNonNull;

/**
 *
 * @author lee
 */
public class DownloadParameter {

    private final DownloadType downloadType;
    private final Lecture lecture;
    private final Subject subject;

    public DownloadParameter(@Nonnull Subject s, @Nonnull Lecture l, @Nonnull DownloadType dt) {
        checkNonNull(s, "Subject");
        checkNonNull(l, "Lecture");
        checkNonNull(dt, "Download Type");
        this.subject = s;
        this.lecture = l;
        this.downloadType = dt;
    }

    @Nonnull
    public DownloadType getDownloadType() {
        return downloadType;
    }

    @Nonnull
    public Lecture getLecture() {
        return lecture;
    }

    @Nonnull
    public Subject getSubject() {
        return subject;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.downloadType, this.lecture, this.subject);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DownloadParameter other = (DownloadParameter) obj;
        return Objects.equal(this.downloadType, other.downloadType)
               && Objects.equal(this.lecture, other.lecture)
               && Objects.equal(this.subject, other.subject);
    }

}
