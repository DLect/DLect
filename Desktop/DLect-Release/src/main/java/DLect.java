/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.AWTException;
import java.io.IOException;
import java.util.Properties;
import org.lee.echo360.Launcher;
import org.lee.echo360.StandaloneLauncher;
import org.lee.echo360.SystemTrayLauncher;

/**
 *
 * @author lee
 */
public class DLect {

    private static Launcher standaloneLauncher = new StandaloneLauncher();
    private static Launcher systemTrayLauncher = new SystemTrayLauncher();

    public static void main(String[] args) throws IOException, AWTException {
        long startTime = System.currentTimeMillis();
        Properties prop = new Properties();
        try {
            prop.load(DLect.class.getResourceAsStream("/props/startup.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        LoadStyle loadStyle = LoadStyle.from(prop.getProperty("style"));
        if (args.length > 0) {
            loadStyle = getLoadStyleFromArgs(args, loadStyle);
        }
        //if ("tray".equalsIgnoreCase(loadStyle)) {
        //    systemTrayLauncher.launch(startTime, args);
        //} else if ("cmd".equalsIgnoreCase(loadStyle)) {
        //} else {
        standaloneLauncher.launch(startTime, args);
        //}
    }

    private static LoadStyle getLoadStyleFromArgs(String[] args, LoadStyle defaultStyle) {
        for (String arg : args) {
            if ("--tray".equalsIgnoreCase(arg) || "-t".equalsIgnoreCase(arg)) {
                return LoadStyle.TRAY;
            } else if ("--gui".equalsIgnoreCase(arg) || "-g".equalsIgnoreCase(arg)) {
                return LoadStyle.STANDALONE;
            }
        }
        return defaultStyle;
    }

    private static enum LoadStyle {

        TRAY, STANDALONE;

        protected static LoadStyle from(String s) {
            String n = s.toUpperCase();
            for (LoadStyle loadStyle : values()) {
                if (loadStyle.name().startsWith(n)) {
                    return loadStyle;
                }
            }
            return STANDALONE;
        }
    }
}
