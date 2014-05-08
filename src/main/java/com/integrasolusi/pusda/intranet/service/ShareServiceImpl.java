package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.ShareDao;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.persistence.Share;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 11:27:32 AM
 */
public class ShareServiceImpl implements ShareService {
    private ShareDao shareDao;

    public void setShareDao(ShareDao shareDao) {
        this.shareDao = shareDao;
    }

    @Override
    public List<Share> findByDocument(Document document) {
        return shareDao.findByFilter(new ValueFilter("document", QueryOperator.EQUALS, document, "document"));
    }

    @Override
    public Share findShareForPerson(Document document, Person person) {
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
        filter.add(new ValueFilter("person", QueryOperator.EQUALS, person, "person"));
        filter.add(new ValueFilter("document", QueryOperator.EQUALS, document, "document"));
        return shareDao.findUniqueByFilter(filter);
    }

    @Override
    public Long countByDocument(Document document) {
        return shareDao.countByFilter(new ValueFilter("document", QueryOperator.EQUALS, document, "document"));
    }

    @Override
    public Share shareTo(Document document, Person person, boolean canSubmitRevision) {
        Share share = new Share();
        share.setDocument(document);
        share.setPerson(person);
        share.setCanModify(canSubmitRevision);
        shareDao.save(share);
        return share;
    }

    @Override
    public void removeById(Long id) {
        shareDao.removeById(id);
    }
}
