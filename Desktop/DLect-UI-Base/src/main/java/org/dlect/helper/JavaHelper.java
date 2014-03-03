/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper;

import java.io.File;
import org.dlect.helpers.StringUtil;

/**
 *
 * @author lee
 */
public class JavaHelper {

    public static File getJarFile() {
        String encodedPath = JavaHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String path = encodedPath.replace("+", "%2B");
        
        String decoded = StringUtil.decodeURL(path);
        
        return new File(decoded);
    }
    
    public static String getJavaExecutable() {
        return null;
    }

}
