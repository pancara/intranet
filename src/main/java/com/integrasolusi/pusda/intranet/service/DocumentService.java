package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import com.integrasolusi.pusda.intranet.persistence.Person;

import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 6:15:53 PM
 */
public interface DocumentService {

    Document findById(Long id);

    List<Document> findByFilter(Filter filter, int start, int count);

    void save(Document document);

    void writeDocumentContent(DocumentVersion version, OutputStream outputStream);

    void removeById(Long id);

    Long countByPersonAndKeyword(Person person, String keyword);

    List<Document> findByPersonAndKeyword(Person person, String keyword, int start, int count);

    List<DocumentVersion> getVersions(Document document);
}
