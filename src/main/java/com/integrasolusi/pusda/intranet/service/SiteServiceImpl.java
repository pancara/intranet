package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.SiteDao;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Site;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 2:43:06 PM
 */
public class SiteServiceImpl implements SiteService {
    private SiteDao siteDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    @Override
    public List<Site> findViewable() {
        Filter filter = new ValueFilter("active", QueryOperator.EQUALS, true, "active");
        return siteDao.findByFilterOrderBy(filter, "index", OrderDir.ASC);
    }

    @Override
    public List<Site> findAlls() {
        return siteDao.findAllsOrderBy("index", OrderDir.ASC);
    }

    @Override
    public void save(Site site) throws IOException {
        save(site, null);
    }

    @Override
    public void save(Site site, InputStream is) throws IOException {
        siteDao.save(site);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.SITE, site.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public Site findById(Long id) {
        return siteDao.findById(id);
    }

    @Override
    public void writePictureContent(Long id, OutputStream os) {
        String path = RepositoryUtils.getPath(RepositoryUtils.SITE, id);
        contentRepositoryDao.copyBinaryContent(path, os);
    }

    @Override
    public void remove(Site site) {
        siteDao.remove(site);
        contentRepositoryDao.removeBinaryContent(RepositoryUtils.getPath(RepositoryUtils.SITE, site.getId()));
    }

    @Override
    public void moveDown(Site site) {
        Site after = siteDao.findAfter(site);
        if (after != null)
            swapOrder(site, after);
    }

    @Override
    public void moveUp(Site site) {
        Site before = siteDao.findBefore(site);
        if (before != null)
            swapOrder(site, before);
    }

    private void swapOrder(Site site_1, Site site_2) {
        int temp = site_1.getIndex();
        site_1.setIndex(site_2.getIndex());
        site_2.setIndex(temp);

        siteDao.save(site_1);
        siteDao.save(site_2);
    }

    @Override
    public Integer getNewIndex() {
        Integer maxIndex = siteDao.findMaxIndex();
        return (maxIndex == null ? 1 : maxIndex + 1);
    }
}

