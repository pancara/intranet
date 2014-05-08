package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 11:41:20 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class DocumentDaoImplTest {
    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private PersonDao personDao;

    @Test
    public void testCount() throws Exception {
        Person person = personDao.loadByID(2L);
        String keyword = "%a%";
        Long count = documentDao.countByPersonAndKeyword(person, keyword);
        System.out.println("count = " + count);
        List<Document> documents = documentDao.findByPersonAndKeyword(person, keyword, 0, 100);

        for (Document doc : documents) {
            System.out.println("doc.getId() = " + doc.getId());
        }
    }
}
