package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.Site;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 2:41:35 PM
 */
public interface SiteDao extends GenericDao<Site, Long> {
    Site findAfter(Site site);

    Site findBefore(Site site);

    Integer findMaxIndex();
}
