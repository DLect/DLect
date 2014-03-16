/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper;

import java.io.IOException;
import org.dlect.controller.data.DatabaseHandler;

public class ExecutableWrapperImpl implements ExecutableWrapper {

    @Override
    public Process exec(boolean redirect, String... cmds) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(cmds);
        if (redirect) {
            pb.redirectErrorStream(true);
            pb.redirectOutput(DatabaseHandler.getUpdateLoggingFile());
        }
        return pb.start();
    }

}
