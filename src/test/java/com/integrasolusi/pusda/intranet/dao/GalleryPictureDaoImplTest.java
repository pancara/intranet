package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 9, 2011
 * Time         : 5:19:18 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml"})
public class GalleryPictureDaoImplTest {
    @Autowired
    private GalleryPictureDao galleryPictureDao;

    @Autowired
    private GalleryDao galleryDao;

    @Test
    public void getLastPictureIndex() {
        Gallery gallery = galleryDao.findById(1L);
        Integer lastIndex = galleryPictureDao.getLastPictureIndex(gallery);
        System.out.println(lastIndex);
    }

    @Test
    public void findByProperty() {
        List<GalleryPicture> pictures = galleryPictureDao.findByProperty("gallery.id", 1L);
        for(GalleryPicture picture : pictures)
            System.out.println("picture.getId() = " + picture.getId());

    }
}
