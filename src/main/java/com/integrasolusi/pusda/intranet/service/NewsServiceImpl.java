package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.NewsDao;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.News;
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
 * Date         : Feb 17, 2011
 * Time         : 9:40:19 AM
 */
public class NewsServiceImpl implements NewsService {
    private final String UNAVAILABLE_PICTURE = "/images/unavailable.jpg";

    private NewsDao newsDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setNewsDao(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }


    @Override
    public Long countByKeyword(String keyword) {
        String paramValue = "%" + keyword + "%";
        CompositeFilter filter = new CompositeFilter(QueryOperator.OR);
        filter.add(new ValueFilter("title", QueryOperator.LIKE, paramValue, "keyword"));
        filter.add(new ValueFilter("content", QueryOperator.LIKE, paramValue, "keyword"));
        return newsDao.countByFilter(filter);
    }

    @Override
    public Long countByKeywordAndPublished(String keyword) {
        String paramValue = "%" + keyword + "%";
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);

        CompositeFilter keywordFilter = new CompositeFilter(QueryOperator.OR);
        keywordFilter.add(new ValueFilter("title", QueryOperator.LIKE, paramValue, "keyword"));
        keywordFilter.add(new ValueFilter("content", QueryOperator.LIKE, paramValue, "keyword"));

        filter.add(keywordFilter);
        filter.add(new ValueFilter("published", QueryOperator.EQUALS, true, "published"));
        return newsDao.countByFilter(filter);
    }

    @Override
    public List<News> findByKeyword(String keyword, int start, int count) {
        String paramValue = "%" + keyword + "%";
        CompositeFilter filter = new CompositeFilter(QueryOperator.OR);
        filter.add(new ValueFilter("title", QueryOperator.LIKE, paramValue, "keyword"));
        filter.add(new ValueFilter("content", QueryOperator.LIKE, paramValue, "keyword"));
        return newsDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, start, count);
    }

    @Override
    public List<News> findByKeywordAndPublished(String keyword, int start, int count) {
        String paramValue = "%" + keyword + "%";
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);

        CompositeFilter keywordFilter = new CompositeFilter(QueryOperator.OR);
        keywordFilter.add(new ValueFilter("title", QueryOperator.LIKE, paramValue, "keyword"));
        keywordFilter.add(new ValueFilter("content", QueryOperator.LIKE, paramValue, "keyword"));

        filter.add(keywordFilter);
        filter.add(new ValueFilter("published", QueryOperator.EQUALS, true, "published"));
        return newsDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, start, count);
    }

    @Override
    public News findById(Long id) {
        return newsDao.findById(id);
    }

    @Override
    public News findNext(News news) {
        return newsDao.findNext(news);
    }

    @Override
    public News findPrevious(News news) {
        return newsDao.findPrevious(news);
    }

    @Override
    public void save(News news) throws IOException {
        save(news, null);
    }

    @Override
    public void save(News news, InputStream is) throws IOException {
        newsDao.save(news);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.NEWS_PICTURE, news.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public void removeById(Long id) {
        newsDao.removeById(id);
    }

    @Override
    public List<News> findLatest(int count) {
        Filter filter = new ValueFilter("published", QueryOperator.EQUALS, true, "published");
        return newsDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, 0, count);
    }

    @Override
    public BufferedImage getPicture(Long id) throws IOException {
        if (id != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.NEWS_PICTURE, id);
            if (contentRepositoryDao.isPathExist(path))
                return contentRepositoryDao.getBufferedImage(path);
        }
        return ImageIO.read(new ClassPathResource(UNAVAILABLE_PICTURE).getInputStream());
    }

}