package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.SlideDao;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Slide;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 27, 2011
 * Time         : 4:36:33 PM
 */
public class SlideServiceImpl implements SlideService {
    private SlideDao slideDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setSlideDao(SlideDao slideDao) {
        this.slideDao = slideDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    @Override
    public List<Slide> findByType(Integer type) {
        Filter filter = new ValueFilter("type", QueryOperator.EQUALS, type, "type");
        return slideDao.findByFilterOrderBy(filter, "id", OrderDir.ASC);
    }

    @Override
    public Slide findById(Long slideID) {
        return slideDao.findById(slideID);
    }

    @Override
    public void writeContent(Long slideID, OutputStream os) {
        String path = RepositoryUtils.getPath(RepositoryUtils.SLIDE_PICTURE, slideID);
        contentRepositoryDao.copyBinaryContent(path, os);
    }

    @Override
    public void save(Slide slide) throws IOException {
        save(slide, null);
    }

    @Override
    public void save(Slide slide, InputStream is) throws IOException {
        slideDao.save(slide);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.SLIDE_PICTURE, slide.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public void remove(Slide slide) {
        slideDao.remove(slide);
        contentRepositoryDao.removeBinaryContent(RepositoryUtils.getPath(RepositoryUtils.SLIDE_PICTURE, slide.getId()));
    }
}
