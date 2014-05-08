package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;

import java.io.IOException;
import java.io.InputStream;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:40:43 PM
 */
public interface DocumentVersionService {
    void save(Document document, DocumentVersion version, InputStream inputStream) throws IOException;

    DocumentVersion findLatest(Document document);

    DocumentVersion findById(Long id);

    void removeById(Long id);
}
