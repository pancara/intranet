package com.integrasolusi.pusda.intranet.utils;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 11:08:41 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/utils.xml"})
public class EmailSenderTest extends TestCase {
    @Autowired
    private EmailSender emailSender;

    @Test
    public void testSendConfirmationRegistrationEmail() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setName("test name");


        person.setAccount(new Account());
        person.getAccount().setId(1L);
        person.getAccount(). setUserID("test user");
        person.getAccount(). setPassword("test password");
        person.getAccount().setRegistrationToken("registration token");
        emailSender.sendConfirmationRegistrationEmail(person);
    }

    @Test
    public void testSendApprovalEmail() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setName("test name");


        person.setAccount(new Account());
        person.getAccount().setId(1L);
        person.getAccount(). setUserID("test user");
        person.getAccount(). setPassword("test password");
        person.getAccount().setRegistrationToken("registration token");
        emailSender.sendApprovalInformation(person);
    }
}
