package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Publication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 18, 2011
 * Time         : 8:38:49 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/service.xml", "/spring/utils.xml"})
public class PublicationServiceImplTest {
    @Autowired
    private PublicationService publicationService;

    @Test
    public void testFindLatest() throws Exception {
        List<Publication> publications = publicationService.findLatest(6);
        for (Publication p : publications) {
            System.out.println("p.getId() = " + p.getId());
            System.out.println("p = " + p.getPublished());
        }
    }
}
