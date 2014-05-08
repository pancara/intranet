package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 8:46:12 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/service.xml"})
public class GalleryPictureServiceImplTest {
    @Autowired
    private GalleryService galleryService;

    @Autowired
    private GalleryPictureService galleryPictureService;

    @Test
    public void testSave() throws Exception {
        Gallery gallery = galleryService.findById(1L);
        for (int i = 2; i < 5; i++) {
            GalleryPicture picture = new GalleryPicture();
            picture.setDescription("Picture " + i);
            picture.setTitle("Title Picture " + i);
            picture.setGallery(gallery);
            FileInputStream is = new FileInputStream("E:/pusda/data/test" + i + ".jpg");
            galleryPictureService.save(picture, is);
            is.close();
        }
    }

    @Test
    public void updateBinaryContent() throws IOException {
        GalleryPicture picture = galleryPictureService.findById(1L);
        FileInputStream is = new FileInputStream("E:/pusda/data/scaled.jpg");
        galleryPictureService.save(picture, is);
        is.close();


    }
}
