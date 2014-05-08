package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.GalleryPictureDao;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 3:02:59 PM
 */
public class GalleryPictureServiceImpl implements GalleryPictureService {
    private final String UNAVAILABLE_PICTURE = "/images/unavailable.jpg";

    private GalleryPictureDao galleryPictureDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setGalleryPictureDao(GalleryPictureDao galleryPictureDao) {
        this.galleryPictureDao = galleryPictureDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    @Override
    public GalleryPicture findById(Long id) {
        return galleryPictureDao.findById(id);
    }

    @Override
    public void save(GalleryPicture galleryPicture, InputStream is) throws IOException {
        if (galleryPicture.getId() == null) {
            Integer lastIndex = galleryPictureDao.getLastPictureIndex(galleryPicture.getGallery());
            if (lastIndex == null)
                lastIndex = 0;
            galleryPicture.setIndex(lastIndex + 1);
        }
        galleryPictureDao.save(galleryPicture);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY_PICTURE, galleryPicture.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public BufferedImage getPicture(Long id) throws IOException {
        if (id != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY_PICTURE, id);
            if (contentRepositoryDao.isPathExist(path))
                return contentRepositoryDao.getBufferedImage(path);
        }
        return ImageIO.read(new ClassPathResource(UNAVAILABLE_PICTURE).getInputStream());
    }

    public List<GalleryPicture> findByGallery(Gallery gallery) {
        return galleryPictureDao.findByPropertyOrderBy("gallery", gallery, "index", OrderDir.ASC);
    }

    @Override
    public void remove(GalleryPicture picture) {
        galleryPictureDao.remove(picture);
        String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY_PICTURE, picture.getId());
        if (contentRepositoryDao.isPathExist(path))
            contentRepositoryDao.removeBinaryContent(path);
    }
}
