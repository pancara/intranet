package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Feb 8, 2011
 * Time         : 8:48:33 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/utils.xml", "/spring/service.xml"})
public class MessageServiceImplTest {
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private PersonService personService;

    @Test
    public void testCountMessage() throws Exception {
        Person person = personService.findById(3L);
        System.out.println(person.getId());
        Long count = messageService.countMessage(person, "");
        System.out.println("count = " + count);
    }
}
