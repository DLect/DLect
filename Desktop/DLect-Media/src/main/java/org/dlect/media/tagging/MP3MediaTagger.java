/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.media.tagging;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.dlect.logging.MediaLogging;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author lee
 */
@MediaTaggerFor("mp3")
public class MP3MediaTagger implements MediaTagger {
    
    public boolean apply(File inFile, File outFile, MediaMetaData data) {
        try {
            FileUtils.moveFile(inFile, outFile);
            AudioFile read = AudioFileIO.read(outFile);
            Tag tag = read.getTagOrCreateAndSetDefault();
            
            tag.setField(FieldKey.TITLE, data.getTitle());
            tag.setField(FieldKey.ALBUM, data.getAlbum());
            tag.setField(FieldKey.COMMENT, data.getComment());
            
            read.commit();
            return true;
        } catch (CannotReadException | CannotWriteException | IOException | ReadOnlyFileException ex) {
            MediaLogging.LOG.error("Error reading/writing file.", ex);
        } catch (InvalidAudioFrameException | KeyNotFoundException | TagException ex) {
            MediaLogging.LOG.error("Invalid MP3 file.", ex);
        }
        return false;
    }
    
}
