/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.media;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.ChunkOffsetBox;
import com.coremedia.iso.boxes.ContainerBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.UserDataBox;
import com.coremedia.iso.boxes.apple.AbstractAppleMetaDataBox;
import com.googlecode.mp4parser.util.Path;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.lee.echo360.model.DownloadType;
import static org.lee.echo360.model.DownloadType.AUDIO;
import static org.lee.echo360.model.DownloadType.VIDEO;
import org.lee.echo360.model.Lecture;
import org.lee.echo360.model.Subject;
import org.lee.echo360.util.ExceptionReporter;

/**
 *
 * @author lee
 */
public class MediaTagEncoder {

    static {
        AudioFile.logger.setLevel(Level.OFF);
    }
    public static final String COMMENT = "Downloaded By DLect. To find out more visit http://facebook.com/DLect";

    public static Runnable tagInThread(final File inFile, final File outFile, final Subject s, final Lecture l, final DownloadType dt) {
        return new Runnable() {
            @Override
            public void run() {
                tag(inFile, outFile, s, l, dt);
            }
        };
    }

    public static boolean tag(final File inFile, final File outFile, final Subject s, final Lecture l, final DownloadType dt) {
        boolean success = false;
        if (s.getTagFormatter().isEnabled() && s.getTagFormatter().isFormatting(dt)) {
            switch (dt) {
                case VIDEO:
                    success = applyM4vTag(inFile, outFile, s, l, dt);
                    break;
                case AUDIO:
                    success = applyMP3Tag(inFile, outFile, s, l, dt);
                    break;
            }
        }
        if (!success) {
            try {
                FileUtils.copyFile(inFile, outFile);
                return true;
            } catch (IOException ex) {
                ExceptionReporter.reportException(ex);
            }
        }
        return success;
    }

    public static boolean applyMP3Tag(File inFile, File outFile, Subject s, Lecture l, DownloadType dt) {
        try {
            FileUtils.moveFile(inFile, outFile);
            AudioFile read = AudioFileIO.read(outFile);
            Tag tag = read.getTagOrCreateAndSetDefault();
            tag.setField(FieldKey.TITLE, getName(s, l, dt));
            tag.setField(FieldKey.ALBUM, getAlbum(s, l, dt));
            tag.setField(FieldKey.COMMENT, COMMENT);
            read.commit();
            return true;
        } catch (Exception ex) {
            ExceptionReporter.reportException(ex);
        }
        return false;
    }

    /**
     *
     * @return {@code true} if the file was successfully tagged, {@code false}
     * otherwise.
     */
    public static boolean applyM4vTag(File inFile, File outFile, Subject s, Lecture l, DownloadType dt) {
        try {
            IsoFile out = new IsoFile(inFile.toString());

            UserDataBox userDataBox = (UserDataBox) Path.getPath(out, "/moov[0]/udta[0]");
            long sizeBefore = userDataBox.getSize();
            Box b = Path.getPath(userDataBox, "meta[0]/ilst[0]");

            set(b, "©cmt", COMMENT); // TODO Localise
            set(b, "©nam", getName(s, l, dt));
            set(b, "©alb", getAlbum(s, l, dt));

            long sizeAfter = userDataBox.getSize();

            if (needsOffsetCorrection(out)) {
                correctChunkOffsets(out, sizeAfter - sizeBefore);
            }

            FileOutputStream fos = new FileOutputStream(outFile);
            FileChannel fc = fos.getChannel();
            out.getBox(fc);
            fos.close();
            return true;
        } catch (Exception ex) {
            ExceptionReporter.reportException(ex);
        }
        return false;
    }

    private static boolean needsOffsetCorrection(IsoFile isoFile) throws UnsupportedOperationException {

        if (Path.getPaths(isoFile, "mdat").size() > 1) {
            throw new UnsupportedOperationException("There might be the weird case that a file has two mdats. One before"
                    + " moov and one after moov. That would need special handling therefore I just throw an "
                    + "exception here. ");
        }

        if (Path.getPaths(isoFile, "moof").size() > 0) {
            throw new UnsupportedOperationException("Fragmented MP4 files need correction, too. (But I would need to look where)");
        }

        for (Box box : isoFile.getBoxes()) {
            if ("mdat".equals(box.getType())) {
                return false;
            }
            if ("moov".equals(box.getType())) {
                return true;
            }
        }
        throw new UnsupportedOperationException("Hmmm - shouldn't happen");
    }

    private static void correctChunkOffsets(IsoFile tempIsoFile, long correction) {
        List<Box> chunkOffsetBoxes = Path.getPaths(tempIsoFile, "/moov[0]/trak/mdia[0]/minf[0]/stbl[0]/stco[0]");
        for (Box chunkOffsetBox : chunkOffsetBoxes) {

            LinkedList<Box> stblChildren = new LinkedList<Box>(chunkOffsetBox.getParent().getBoxes());
            stblChildren.remove(chunkOffsetBox);

            long[] cOffsets = ((ChunkOffsetBox) chunkOffsetBox).getChunkOffsets();
            for (int i = 0; i < cOffsets.length; i++) {
                cOffsets[i] += correction;
            }

            StaticChunkOffsetBox cob = new StaticChunkOffsetBox();
            cob.setChunkOffsets(cOffsets);
            stblChildren.add(cob);
            chunkOffsetBox.getParent().setBoxes(stblChildren);
        }
    }

    private static void set(Box b, String tag, String val) {
        ContainerBox a = (ContainerBox) b;
        for (Box box : a.getBoxes()) {
            if (box.getType().equals(tag) && box instanceof AbstractAppleMetaDataBox) {
                ((AbstractAppleMetaDataBox) box).setValue(val);
            }
        }
    }

    private static String getName(Subject s, Lecture l, DownloadType dt) {
        return s.getTagFormatter().formatName(dt, s, l);
    }

    private static String getAlbum(Subject s, Lecture l, DownloadType dt) {
        return s.getTagFormatter().formatAlbum(dt, s, l);
    }
}
