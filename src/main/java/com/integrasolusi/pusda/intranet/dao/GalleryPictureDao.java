package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericDao;
import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;

/**
 * Programmer   : pancara
 * Date         : Jan 7, 2011
 * Time         : 9:16:40 PM
 */
public interface GalleryPictureDao extends GenericDao<GalleryPicture, Long> {
    Integer getLastPictureIndex(Gallery gallery);
}
