/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.controllers;

import java.io.File;
import org.lee.echo360.model.UpdateStyle;

/**
 *
 * @author lee
 */
public class ApplicationPropertiesController {

    private UpdateStyle updateStyle;
    private File dataDirectory;
    private final MainController mc;

    public ApplicationPropertiesController(MainController mc) {
        this.mc = mc;
    }

    public void setDataDirectory(File dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void setUpdateStyle(UpdateStyle updateStyle) {
        this.updateStyle = updateStyle;
    }

    public File getDataDirectory() {
        if (dataDirectory == null || (!dataDirectory.exists() && !dataDirectory.mkdirs())) {
            dataDirectory = new File(".");
        }
        return dataDirectory;
    }

    public UpdateStyle getUpdateStyle() {
        if (updateStyle == null) {
            updateStyle = UpdateStyle.COMPLETELY_AUTOMATIC;
        }
        return updateStyle;
    }
}
