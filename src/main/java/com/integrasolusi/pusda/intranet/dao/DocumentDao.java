package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 6:14:25 PM
 */
public interface DocumentDao extends GenericDao<Document, Long> {
    Long countByPersonAndKeyword(Person person, String keyword);

    List<Document> findByPersonAndKeyword(Person person, String keyword, int start, int count);
}
