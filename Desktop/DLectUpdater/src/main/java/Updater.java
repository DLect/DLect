
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dlect.update.InstallImpl;
import org.dlect.update.UpdateImpl;
import org.dlect.update.ui.UpdaterWindow;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lee
 */
public class Updater {

    public static void main(String[] args) {
        String location = (args.length > 0 ? args[0] : null);
        boolean silent = false;
        if (args.length > 1) {
            for (int i = 1; i < args.length; i++) {
                String a = args[i];
                System.out.println("Arg: " + a);
                if (a.matches("^-[-]?[AaQqSs].*")) {
                    silent = true;
                } else if (a.matches("^-[-]?[MmGgVv].*")) {
                    silent = true;
                }
            }
        }
        if (location == null) {
            location = "./DLect.jar";
        }
        if (silent) {
            System.setProperty("java.awt.headless", "true");
            try {
                File update = UpdateImpl.downloadUpdate();
                File dlectJar = new File(location);
                InstallImpl.doInstall(dlectJar, update);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            UpdaterWindow.start(location);
        }
    }
}
