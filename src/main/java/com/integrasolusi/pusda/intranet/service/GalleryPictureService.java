package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 3:02:51 PM
 */
public interface GalleryPictureService {
    GalleryPicture findById(Long id);

    List<GalleryPicture> findByGallery(Gallery gallery);

    BufferedImage getPicture(Long id) throws IOException;

    void save(GalleryPicture galleryPicture, InputStream is) throws IOException;


    void remove(GalleryPicture picture);
}
