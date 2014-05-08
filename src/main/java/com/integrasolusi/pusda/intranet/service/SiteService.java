package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Site;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 2:42:53 PM
 */
public interface SiteService {

    List<Site> findViewable();

    List<Site> findAlls();

    void save(Site site) throws IOException;

    void save(Site site, java.io.InputStream is) throws IOException;

    Site findById(Long id);

    void writePictureContent(Long id, OutputStream os);

    void remove(Site site);

    void moveUp(Site site);

    void moveDown(Site site);

    Integer getNewIndex();
}
