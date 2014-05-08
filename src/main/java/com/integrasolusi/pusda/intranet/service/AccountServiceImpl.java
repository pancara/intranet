package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.AccountDao;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import org.apache.commons.lang.StringUtils;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:19:54 AM
 */
public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    @Override
    public Account authenticate(String userID, String password) {
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
        filter.add(new ValueFilter("userID", QueryOperator.EQUALS, userID, "userID"));
        filter.add(new ValueFilter("active", QueryOperator.EQUALS, true, "active"));
        Account account = accountDao.findUniqueByFilter(filter);
        if (account != null)
            if (StringUtils.equals(account.getPassword(), password))
                return account;
        return null;
    }

    @Override
    public Account findById(Long id) {
        return accountDao.findById(id);
    }

    @Override
    public Person getOwner(Account account) {
        return accountDao.getOwner(account);
    }

    public void setUserDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void save(Account account) {
        accountDao.save(account);
    }

    @Override
    public Account findByUserID(String userID) {
        Filter filter = new ValueFilter("userID", QueryOperator.EQUALS, userID, "userID");
        return accountDao.findUniqueByFilter(filter);
    }
}