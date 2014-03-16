/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper;

import java.io.IOException;

/**
 *
 * @author lee
 */
public interface ExecutableWrapper {

    public Process exec(boolean redirect, String... cmds) throws IOException;

}
