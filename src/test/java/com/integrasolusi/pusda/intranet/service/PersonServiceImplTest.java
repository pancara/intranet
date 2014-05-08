package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.persistence.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 8:33:32 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/service.xml"})
public class PersonServiceImplTest {
    @Autowired
    private PersonService personService;

    @Test
    public void testCountByFilterForShare() throws Exception {
        String keyword = "%person%";
        Long count = personService.countForShare(keyword, 1L);
        System.out.println(count);

        List<Person> persons = personService.findForShare(keyword, 1L, 0, 5);
        for (Person p : persons)
            System.out.println("p.getName() = " + p.getName());
    }
}
