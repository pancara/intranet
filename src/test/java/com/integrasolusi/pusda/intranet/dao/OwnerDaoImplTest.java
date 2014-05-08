package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 5:01:30 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class OwnerDaoImplTest {
    @Autowired
    private PersonDao personDao;

    @Test
    public void createBean() {
        Assert.assertNotNull(personDao);
    }

    @Test
    public void createOwner() {
        Person person = new Person();
        person.setName("test");
        personDao.save(person);
    }
}
