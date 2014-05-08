package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:19:44 AM
 */
public interface AccountService {

    Account authenticate(String userID, String password);

    Account findById(Long id);

    Person getOwner(Account account);

    void save(Account save);

    Account findByUserID(String userID);
}