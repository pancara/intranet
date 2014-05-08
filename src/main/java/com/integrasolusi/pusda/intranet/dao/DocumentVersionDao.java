package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:38:26 PM
 */
public interface DocumentVersionDao extends GenericDao<DocumentVersion, Long> {

    DocumentVersion findLatest(Document document);
}
