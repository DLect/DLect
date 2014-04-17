/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import org.dlect.helper.JavaHelper;

/**
 *
 * @author lee
 */
public class JarFolderFileController extends FolderFileController {

    public JarFolderFileController() {
        super(JavaHelper.getJarFile().getParentFile());
    }

}
