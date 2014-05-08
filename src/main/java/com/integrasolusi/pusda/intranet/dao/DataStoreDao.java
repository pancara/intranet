package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.DataStore;

/**
 * Programmer   : pancara
 * Date         : Feb 3, 2011
 * Time         : 5:46:32 PM
 */
public interface DataStoreDao extends GenericDao<DataStore, Long> {
    DataStore findBefore(DataStore dataStore);

    DataStore findAfter(DataStore dataStore);
}
