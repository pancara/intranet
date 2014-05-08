package com.integrasolusi.pusda.intranet.repository.fs;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Programmer   : pancara
 * Date         : 6/22/11
 * Time         : 9:51 AM
 */
public interface DocumentRepository {

    void storeDocument(DocumentType type, Long id, InputStream is) throws IOException;

    void readDocument(DocumentType type, Long id, OutputStream os) throws IOException;

    void removeDocument(DocumentType type, Long id);

    BufferedImage createImage(DocumentType type, Long id) throws IOException;

    Boolean documentExist(DocumentType type, Long id);

}
