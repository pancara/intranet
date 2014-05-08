package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Gallery;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 7, 2011
 * Time         : 9:19:52 PM
 */
public interface GalleryService {

    List<Gallery> findAlls(int start, int count);

    Long countAlls();

    List<Gallery> findByKeyword(String keyword, int start, int count);

    Long countByKeyword(String keyword);

    void save(Gallery gallery) throws IOException;

    void save(Gallery gallery, java.io.InputStream is) throws IOException;

    Gallery findById(Long id);

    void removeById(Long id);

    BufferedImage createGalleryFoto(Long id) throws IOException;
}
