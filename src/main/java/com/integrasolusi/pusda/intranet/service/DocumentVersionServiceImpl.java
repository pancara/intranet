package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.DocumentVersionDao;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;

import java.io.IOException;
import java.io.InputStream;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:40:53 PM
 */
public class DocumentVersionServiceImpl implements DocumentVersionService {
    private DocumentVersionDao documentVersionDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setDocumentVersionDao(DocumentVersionDao documentVersionDao) {
        this.documentVersionDao = documentVersionDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }


    @Override
    public void save(Document document, DocumentVersion version, InputStream inputStream) throws IOException {
        version.setDocument(document);
        Integer newVersionNumber = getNewVersion(document);
        version.setVersionNumber(newVersionNumber);
        documentVersionDao.save(version);
        if (inputStream != null) {
            String path = String.format("/%s/%d/%d", RepositoryUtils.DOCUMENT, version.getDocument().getId(), version.getId());
            contentRepositoryDao.saveBinaryContent(path, inputStream);
        }
    }


    private Integer getNewVersion(Document document) {
        DocumentVersion version = documentVersionDao.findLatest(document);
        if (version == null) return 1;
        else return version.getVersionNumber() + 1;
    }

    @Override
    public DocumentVersion findLatest(Document document) {
        return documentVersionDao.findLatest(document);
    }

    @Override
    public DocumentVersion findById(Long id) {
        return documentVersionDao.findById(id);
    }

    @Override
    public void removeById(Long id) {
        DocumentVersion version = documentVersionDao.findById(id);
        documentVersionDao.remove(version);
        String path = String.format("/%s/%d/%d", RepositoryUtils.DOCUMENT, version.getDocument().getId(), version.getId());
        contentRepositoryDao.removeBinaryContent(path);
    }
}
