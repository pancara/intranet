package com.integrasolusi.pusda.intranet.web.utils;

import java.io.*;

/**
 * Programmer   : pancara
 * Date         : 4/12/11
 * Time         : 3:24 PM
 */
public class StreamBufferUtils {
    private File temporaryDir;
    private int temporaryIndex = 0;


    public void setTemporaryDir(File temporaryDir) {
        this.temporaryDir = temporaryDir;
    }

    public synchronized File createTemporaryFile() {
        temporaryIndex++;
        while (true) {
            File temporaryFile = new File(temporaryDir, String.valueOf(temporaryIndex));
            if (!temporaryFile.exists())
                return temporaryFile;
        }
    }

}
