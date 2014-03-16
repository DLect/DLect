/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.update;

import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.dlect.helper.ExecutableWrapper;
import org.dlect.helper.ExecutableWrapperImpl;
import org.dlect.helper.JavaHelper;

public class UpdateExecutorImpl implements UpdateExecutor {

    private final ExecutableWrapper executableWrapper;
    private final JavaHelper javaHelper;

    public UpdateExecutorImpl(ExecutableWrapper executableWrapper) {
        this(executableWrapper, new JavaHelper(executableWrapper));
    }

    public UpdateExecutorImpl(ExecutableWrapper executableWrapper, JavaHelper javaHelper) {
        this.executableWrapper = executableWrapper;
        this.javaHelper = javaHelper;
    }

    public UpdateExecutorImpl() {
        this(new ExecutableWrapperImpl());
    }

    protected String copyUpdater() throws UpdateException {
        try {
            File f = File.createTempFile("DLect-Updater", "jar");
            try (InputStream updater = getUpdaterJarLocation().openStream();
                 FileOutputStream tmpStream = new FileOutputStream(f)) {
                ByteStreams.copy(updater, tmpStream);
            } catch (IOException ex) {
                UpdateLogger.LOGGER.error("Failed to copy into temporary file.", ex);
                throw new UpdateException("Failed to copy into temporary file.", ex);
            }
            return f.getPath();
        } catch (IOException ex) {
            UpdateLogger.LOGGER.error("Failed to create temporary file.", ex);
            throw new UpdateException("Failed to create temporary file.", ex);
        }
    }

    @Override
    public void executeUpdate(UpdateStyle us) throws UpdateException {
        Optional<String> javaCommand = javaHelper.getJavaExecutable();

        if (javaCommand.isPresent()) {

            String updaterTempPath = copyUpdater();
            String currentLocation = JavaHelper.getJarFile().getName();
            try {
                executableWrapper.exec(true, javaCommand.get(), "-jar", updaterTempPath, currentLocation, "--" + us.name());
            } catch (IOException ex) {
                UpdateLogger.LOGGER.error("Failed to run java command.", ex);
                throw new UpdateException("Java failed to execute", ex);
            }

        }
    }

    protected URL getUpdaterJarLocation() {
        return this.getClass().getResource("/updater/updater.jar");
    }

}
