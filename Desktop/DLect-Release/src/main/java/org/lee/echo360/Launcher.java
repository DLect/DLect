package org.lee.echo360;

import java.awt.AWTException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lee
 */
public interface Launcher {

    public void launch(long startTime, String... args) throws AWTException;
}
