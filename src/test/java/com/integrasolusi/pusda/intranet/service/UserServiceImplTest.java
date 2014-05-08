package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Feb 15, 2011
 * Time         : 12:49:30 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml", "classpath:/spring/utils.xml", "classpath:/spring/service.xml", "classpath:/spring/aspect.xml"})
public class UserServiceImplTest {
    @Autowired
    private AccountService accountService;

    @Test
    public void testAuthenticate() throws Exception {
        Account account = accountService.authenticate("user", "user");
        System.out.println("account.getId() = " + account.getId());
    }
}
