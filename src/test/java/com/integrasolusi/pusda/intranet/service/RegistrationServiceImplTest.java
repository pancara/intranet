package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.utils.StringHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Jan 21, 2011
 * Time         : 6:48:52 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/utils.xml", "/spring/service.xml"})
public class RegistrationServiceImplTest {
    @Autowired
    private RegistrationService registrationService;

    @Test
    public void sendConfirmationEmail() {
        Account account = new Account();
        account.setId(100L);
        account.setUserID("userID");
        account.setPassword("password--");
        account.setRegistrationToken(StringHelper.generateRandomText(100));

        Person person = new Person();
        person.setAccount(account);

    }

}
