package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.persistence.Person;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:19:44 AM
 */
public interface PersonService {

    Person findById(Long id);

    Long countByFilter(Filter filter);

    Long countForShare(String keyword, Long documentID);

    List<Person> findForShare(String keyword, Long documentID, int start, int count);

    List<Person> findByFilter(Filter filter);

    List<Person> findByFilter(Filter filter, int start, int count);

    BufferedImage createAvatar(Long id) throws IOException;

    List<Person> findAlls();

    void save(Person person) throws IOException;

    void save(Person person, java.io.InputStream is) throws IOException;

    Long countPersonWithAccount(String keyword);

    Long countPersonWithAccount(String keyword, Boolean confirmed, Boolean approved, Boolean active);

    List<Person> findPersonWithAccount(String keyword, Integer start, Integer count);

    List<Person> findPersonWithAccount(String keyword, Boolean confirmed, Boolean approved, Boolean active, Integer start, Integer count);
}
