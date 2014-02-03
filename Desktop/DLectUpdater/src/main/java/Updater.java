
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lee.echo360.update.InstallImpl;
import org.lee.echo360.update.UpdateImpl;
import org.lee.echo360.update.ui.UpdaterWindow;

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
                if (opt(a, 'a', 's', 'q')) {
                    silent = true;
                } else if (opt(a, 'n', 'g')) {
                    silent = false;
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

    private static boolean opt(String a, char... chars) {
        for (char c : chars) {
            if (a.charAt(1) == c || (a.charAt(1) == '-' && a.charAt(2) == c)) {
                return true;
            }
        }
        return false;
    }
}
