package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.News;
import com.integrasolusi.pusda.intranet.persistence.Publication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:40:01 AM
 */
public interface NewsService {

    Long countByKeyword(String keyword);

    Long countByKeywordAndPublished(String keyword);

    List<News> findByKeyword(String keyword, int start, int count);

    List<News> findByKeywordAndPublished(String keyword, int start, int rowPerPage);

    News findById(Long id);

    News findNext(News news);

    News findPrevious(News news);

    void save(News news) throws IOException;

    void save(News news, java.io.InputStream is) throws IOException;

    void removeById(Long id);

    List<News> findLatest(int count);

    BufferedImage getPicture(Long newsID) throws IOException;

}