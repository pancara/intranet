package com.integrasolusi.pusda.intranet.repository.jackrabbit;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 5:09:24 PM
 */

public interface ContentRepositoryDao {
    final String BINARY_CONTENT_PROPERTY = "binary_content";

    void saveBinaryContent(String path, final InputStream inputStream) throws IOException;

    void removeBinaryContent(String path);

    void copyBinaryContent(String path, final OutputStream outputStream);

    void copyBinaryContent(String path, final OutputStream outputStream, boolean useTemporaryFile);

    Boolean isPathExist(String path);

    BufferedImage getBufferedImage(final String path) throws IOException;

}
