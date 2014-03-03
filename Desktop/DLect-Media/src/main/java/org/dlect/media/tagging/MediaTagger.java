/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.media.tagging;

import java.io.File;

/**
 * This interface must be used in conjunction with {@link MediaTaggerFor}; although this is not implemented yet.
 *
 * @author lee
 */
public interface MediaTagger {

    /**
     * Move the input file to the output file and in the process modify the file to have the given meta data.
     *
     * @param inFile
     * @param outFile
     * @param metaData
     *
     * @return {@code true} if the file was successfully tagged AND copied, {@code false}
     *         otherwise. If this method did not successfully mode the file then {@code false} must be returned.
     */
    public boolean apply(File inFile, File outFile, MediaMetaData metaData);

}
