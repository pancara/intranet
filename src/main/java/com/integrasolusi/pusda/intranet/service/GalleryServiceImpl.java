package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.GalleryDao;
import com.integrasolusi.pusda.intranet.dao.GalleryPictureDao;
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
 * Date         : Jan 7, 2011
 * Time         : 9:20:01 PM
 */
public class GalleryServiceImpl implements GalleryService {
    private final String DEFAULT_GALLERY_FOTO = "/images/gallery-unavailable.jpg";

    private GalleryDao galleryDao;
    private GalleryPictureDao galleryPictureDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setGalleryDao(GalleryDao galleryDao) {
        this.galleryDao = galleryDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    public void setGalleryPictureDao(GalleryPictureDao galleryPictureDao) {
        this.galleryPictureDao = galleryPictureDao;
    }

    @Override
    public List<Gallery> findAlls(int start, int count) {
        return galleryDao.findAlls(start, count);
    }

    @Override
    public Long countAlls() {
        return galleryDao.countAlls();
    }

    @Override
    public Long countByKeyword(String keyword) {
        return galleryDao.countByProperty("title", keyword);
    }

    @Override
    public List<Gallery> findByKeyword(String keyword, int start, int count) {
        return galleryDao.findByProperty("title", keyword, start, count);
    }

    @Override
    public void save(Gallery gallery) throws IOException {
        save(gallery, null);
    }

    @Override
    public void save(Gallery gallery, InputStream is) throws IOException {
        galleryDao.save(gallery);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY, gallery.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public Gallery findById(Long id) {
        return galleryDao.findById(id);
    }

    @Override
    public void removeById(Long id) {
        Gallery gallery = galleryDao.findById(id);
        List<GalleryPicture> pictures = galleryPictureDao.findByProperty("gallery", gallery);
        for (GalleryPicture picture : pictures) {
            galleryPictureDao.remove(picture);
            String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY_PICTURE, picture.getId());
            contentRepositoryDao.removeBinaryContent(path);
        }
        galleryDao.removeById(id);
        String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY, id);
        if (contentRepositoryDao.isPathExist(path)) contentRepositoryDao.removeBinaryContent(path);
    }

    @Override
    public BufferedImage createGalleryFoto(Long id) throws IOException {
        if (id != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.GALLERY, id);
            if (contentRepositoryDao.isPathExist(path))
                return contentRepositoryDao.getBufferedImage(path);
        }
        return ImageIO.read(new ClassPathResource(DEFAULT_GALLERY_FOTO).getInputStream());
    }

}
