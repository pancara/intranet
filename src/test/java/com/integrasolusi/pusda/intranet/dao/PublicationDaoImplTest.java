package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Publication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Feb 27, 2011
 * Time         : 7:02:05 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class PublicationDaoImplTest {
    @Autowired
    private PublicationDao publicationDao;

    @Test
    public void findNext() {
        Publication current = publicationDao.findById(11L);
        Publication next = publicationDao.findNext(current);
        System.out.println(next.getId());
    }

    @Test
    public void findPrevious() {
        Publication current = publicationDao.findById(12L);
        Publication previous = publicationDao.findPrevious(current);
        System.out.println("previous.getId() = " + previous.getId());
    }
}
