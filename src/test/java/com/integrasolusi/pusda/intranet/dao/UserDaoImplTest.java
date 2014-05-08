package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:26:13 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml"})
public class UserDaoImplTest {
    @Autowired
    private AccountDao accountDao;

    @Test
    public void findUniqueByProperty() {
        Account account = accountDao.findUniqueByProperty("userID", "account");
        
    }

}
