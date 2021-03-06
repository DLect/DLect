/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.entity.ContentType;

/**
 *
 * @author lee
 */
public class HttpResponceStream extends InputStream {

    private final InputStream wrapped;
    private final ContentType contentType;
    private final URI location;

    public HttpResponceStream(InputStream wrapped, ContentType contentType, URI location) {
        this.wrapped = wrapped;
        this.contentType = contentType;
        this.location = location;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public URI getLocation() {
        return location;
    }

    @Override
    public int read() throws IOException {
        return wrapped.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return wrapped.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return wrapped.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return wrapped.skip(n);
    }

    @Override
    public int available() throws IOException {
        return wrapped.available();
    }

    @Override
    public void close() throws IOException {
        wrapped.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        wrapped.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        wrapped.reset();
    }

    @Override
    public boolean markSupported() {
        return wrapped.markSupported();
    }

}
