package com.integrasolusi.pusda.intranet.utils;

import java.io.*;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 5:14:57 PM
 */
public class StreamHelper {
    private int bufferSize = 1024;


    public static StreamHelper getInstance() {
        return new StreamHelper();
    }

    public void copy(InputStream source, OutputStream target, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        while (source.available() > 0) {
            int readCount = source.read(buffer);
            target.write(buffer, 0, readCount);
        }
    }

    public void copy(InputStream source, OutputStream target) throws IOException {
        copy(source, target, bufferSize);
    }

    public void copyFile(File source, File target) throws IOException {
        InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(target);
        try {
            copy(is, os);
        } finally {
            if (is != null)
                is.close();
            if (os != null)
                os.close();
        }
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
