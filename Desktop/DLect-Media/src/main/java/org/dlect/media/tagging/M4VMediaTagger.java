/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.media.tagging;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.ChunkOffsetBox;
import com.coremedia.iso.boxes.ContainerBox;
import com.coremedia.iso.boxes.StaticChunkOffsetBox;
import com.coremedia.iso.boxes.UserDataBox;
import com.coremedia.iso.boxes.apple.AbstractAppleMetaDataBox;
import com.google.common.collect.Lists;
import com.googlecode.mp4parser.util.Path;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import org.dlect.logging.MediaLogging;

/**
 *
 * @author lee
 */
@MediaTaggerFor({"m4v", "mp4" /*
 * ...
 */})
public class M4VMediaTagger implements MediaTagger {

    @Override
    public boolean apply(File inFile, File outFile, MediaMetaData metaData) {
        try {
            IsoFile out = new IsoFile(inFile.toString());

            UserDataBox userDataBox = (UserDataBox) Path.getPath(out, "/moov[0]/udta[0]");
            long sizeBefore = userDataBox.getSize();
            Box b = Path.getPath(userDataBox, "meta[0]/ilst[0]");

            set(b, "©cmt", metaData.getComment());
            set(b, "©nam", metaData.getTitle());
            set(b, "©alb", metaData.getAlbum());

            long sizeAfter = userDataBox.getSize();

            if (needsOffsetCorrection(out)) {
                correctChunkOffsets(out, sizeAfter - sizeBefore);
            }

            try (FileChannel fc = new FileOutputStream(outFile).getChannel()) {
                out.getBox(fc);
            }
            return true;
        } catch (UnsupportedOperationException | IOException ex) {
            MediaLogging.LOG.error("Error applying M4V tags. ", ex);
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

            List<Box> stblChildren = Lists.newArrayList(chunkOffsetBox.getParent().getBoxes());
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

}
