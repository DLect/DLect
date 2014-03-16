/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helper;

import com.google.common.base.Optional;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import javax.annotation.Nonnull;
import org.dlect.helpers.StringUtil;
import org.dlect.update.UpdateLogger;

/**
 *
 * @author lee
 */
public class JavaHelper {

    private final ExecutableWrapper executableWrapper;

    public static File getJarFile() {
        String encodedPath = JavaHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String path = encodedPath.replace("+", "%2B");

        String decoded = StringUtil.decodeURL(path);

        return new File(decoded);
    }

    public JavaHelper() {
        this(new ExecutableWrapperImpl());
    }

    public JavaHelper(ExecutableWrapper executableWrapper) {
        this.executableWrapper = executableWrapper;
    }

    @Nonnull
    public Optional<String> getJavaExecutable() {
        Optional<String> java = checkJava();
        if (!java.isPresent()) {
            return searchForJava();
        }
        return java;
    }

    @Nonnull
    protected Optional<String> checkJava() {
        try {
            Process exec = executableWrapper.exec(false, "java", "-version");
            // This will throw an IOException if it can't find `java`.
            exec.destroy();
            // So we can just destroy it, We don't care about the output or
            //  the exit code, and during testing it took some time to exit.
            // So just destroying it will save time and memory.
            return Optional.of("java");
        } catch (IOException ex) {
            UpdateLogger.LOGGER.error("Failed to create process from plain java instance!", ex);
            return Optional.absent();
        }
    }

    @Nonnull
    protected Optional<String> searchForJava() {
        String p = System.getProperty("java.home", null);
        // Use the `java.home` property. It points to the folder that the `bin`
        //  directory resides.
        // However it could be null, so check that.
        if (p != null) {
            p = Files.simplifyPath(p + "/bin/java");
            // Move into the bin directory and call java.
            // Could call javaw if on windows but it would make no difference,
            //  as this application controls the terminal/command line.
            if (System.getProperty("os.name", "Unknown").contains("Windows")) {
                // Add the `exe` extension on windows.
                p += ".exe";
            }
            try {
                // Don't need the quotes around the command, as the exec
                //  command calls it directly instead of through the terminal.
                Process exec = executableWrapper.exec(false, p, "-version");
                // If we get here, then it must have succedded, so destroy the
                //  process and exit.
                exec.destroy();
                return Optional.of(p);
            } catch (IOException ex) {
                UpdateLogger.LOGGER.error("Failed to create process from `java.home` java instance!", ex);
            }
        }
        return Optional.absent();
    }

}
