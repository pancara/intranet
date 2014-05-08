package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 5:36:26 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class PersonDaoImplTest {
    @Autowired
    private PersonDao personDao;

    @Test
    public void addPersons() {
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setName("Person Number " + i);
            personDao.save(person);
        }
    }
}
