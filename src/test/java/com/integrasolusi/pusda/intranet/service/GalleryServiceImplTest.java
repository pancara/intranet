package com.integrasolusi.pusda.intranet.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 5:56:27 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/service.xml"})
public class GalleryServiceImplTest {
    @Autowired
    private GalleryService galleryService;

    @Test
    public void removeById() {
        galleryService.removeById(7L);
    }

}
