/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.download.event;

import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;
import java.math.RoundingMode;
import javax.annotation.CheckForSigned;

/**
 *
 * @author lee
 */
public class DownloadState {

    private final long currentBytes;
    private final long totalSizeBytes;
    private final long downloadTimeMillis;

    public DownloadState(long currentBytes, long totalSizeBytes, long downloadTimeMillis) {
        this.currentBytes = currentBytes;
        this.totalSizeBytes = totalSizeBytes;
        this.downloadTimeMillis = downloadTimeMillis;
    }

    public DownloadState() {
        this.currentBytes = 0;
        this.totalSizeBytes = -1;
        this.downloadTimeMillis = 0;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public long getTotalSizeBytes() {
        return totalSizeBytes;
    }

    public long getDownloadTimeMillis() {
        return downloadTimeMillis;
    }

    /**
     * The current speed in bytes/second.
     *
     * @return
     */
    public double currentSpeed() {
        if (downloadTimeMillis <= 0 || currentBytes <= 0) {
            return 0;
        }
        double seconds = downloadTimeMillis / 1000.0;

        return currentBytes / seconds;
    }

    /**
     * The number of milliseconds until completion. This is only an estimate.
     *
     * @return
     */
    @CheckForSigned
    public long estimatedCompletion() {
        if (downloadTimeMillis <= 0 || currentBytes <= 0) {
            return -1;
        }
        double speed = ((double) currentBytes) / downloadTimeMillis;

        long remaining = totalSizeBytes - currentBytes;
        if (remaining < 0) {
            return -1;
        }
        return DoubleMath.roundToLong(remaining / speed, RoundingMode.HALF_UP);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.currentBytes, this.totalSizeBytes, this.downloadTimeMillis);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DownloadState other = (DownloadState) obj;
        return Objects.equal(this.currentBytes, other.currentBytes)
               && Objects.equal(this.totalSizeBytes, other.totalSizeBytes)
               && Objects.equal(this.downloadTimeMillis, other.downloadTimeMillis);
    }

}
