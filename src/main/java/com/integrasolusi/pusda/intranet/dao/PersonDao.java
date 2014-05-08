package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 10:19:03 AM
 */
public interface PersonDao extends GenericDao<Person, Long> {
    Long countForShare(String keyword, Document document);

    List<Person> findForShare(String keyword, Document document, int start, int count);

    Long countPersonWithAccount(String keyword);

    Long countPersonWithAccount(String keyword, Boolean confirmed, Boolean approved, Boolean active);

    List<Person> findPersonWithAccount(String keyword, int start, int count);

    List<Person> findPersonWithAccount(String keyword, Boolean confirmed, Boolean approved, Boolean active, int start, int count);
}
