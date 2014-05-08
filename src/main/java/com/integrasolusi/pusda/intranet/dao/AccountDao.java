package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:18:32 AM
 */
public interface AccountDao extends GenericDao<Account, Long> {

    Person getOwner(Account account);
}
