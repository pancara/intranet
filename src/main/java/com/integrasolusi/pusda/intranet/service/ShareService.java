package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.persistence.Share;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 11:27:25 AM
 */
public interface ShareService {

    List<Share> findByDocument(Document document);

    Share findShareForPerson(Document document, Person person);

    Long countByDocument(Document document);

    Share shareTo(Document document, Person person, boolean canSubmitRevision);

    void removeById(Long id);
}
