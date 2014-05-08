package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.News;
import com.integrasolusi.pusda.intranet.persistence.Publication;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:38:01 AM
 */
public interface NewsDao extends GenericDao<News, Long> {

    News findNext(News news);

    News findPrevious(News news);
}