package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.DocumentDao;
import com.integrasolusi.pusda.intranet.dao.DocumentVersionDao;
import com.integrasolusi.pusda.intranet.dao.ShareDao;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import org.hibernate.ScrollableResults;

import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 6:16:01 PM
 */
public class DocumentServiceImpl implements DocumentService {
    private DocumentDao documentDao;
    private ShareDao shareDao;
    private DocumentVersionDao documentVersionDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    public void setShareDao(ShareDao shareDao) {
        this.shareDao = shareDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    public void setDocumentVersionDao(DocumentVersionDao documentVersionDao) {
        this.documentVersionDao = documentVersionDao;
    }

    @Override
    public Long countByPersonAndKeyword(Person person, String keyword) {
        return documentDao.countByPersonAndKeyword(person, keyword);
    }

    @Override
    public List<Document> findByPersonAndKeyword(Person person, String keyword, int start, int count) {
        return documentDao.findByPersonAndKeyword(person, keyword, start, count);
    }

    @Override
    public Document findById(Long id) {
        return documentDao.findById(id);
    }

    @Override
    public List<Document> findByFilter(Filter filter, int start, int count) {
        return documentDao.findByFilter(filter, start, count);
    }

    @Override
    public void save(Document document) {
        documentDao.save(document);
    }

    @Override
    public void writeDocumentContent(DocumentVersion version, OutputStream outputStream) {
        String path = String.format("/%s/%d/%d", RepositoryUtils.DOCUMENT, version.getDocument().getId(), version.getId());
        contentRepositoryDao.copyBinaryContent(path, outputStream);
    }

    @Override
    public void removeById(Long id) {
        Document document = documentDao.findById(id);

        // remove share records
        removeShareData(document);

        // remove version records and binary
        ValueFilter versionFilter = new ValueFilter("document", QueryOperator.EQUALS, document, "document");
        Filter filter = versionFilter;
        ScrollableResults cursor = documentVersionDao.findByFilterAsCursor(filter);
        while (cursor.next()) {
            DocumentVersion version = (DocumentVersion) cursor.get(0);
            String versionPath = String.format("/%s/%d/%d", RepositoryUtils.DOCUMENT, version.getDocument().getId(), version.getId());
            contentRepositoryDao.removeBinaryContent(versionPath);
        }

        documentVersionDao.removeByFilter(versionFilter);


        // remove document record and binary
        String path = RepositoryUtils.getPath(RepositoryUtils.DOCUMENT, id);
        contentRepositoryDao.removeBinaryContent(path);
        documentDao.remove(document);
    }


    private void removeShareData(Document document) {
        shareDao.removeByFilter(new ValueFilter("document", QueryOperator.EQUALS, document, "document"));
    }

    @Override
    public List<DocumentVersion> getVersions(Document document) {
        Filter filter = new ValueFilter("document", QueryOperator.EQUALS, document, "document");
        return documentVersionDao.findByFilterOrderBy(filter, "versionNumber", OrderDir.DESC);
    }
}
