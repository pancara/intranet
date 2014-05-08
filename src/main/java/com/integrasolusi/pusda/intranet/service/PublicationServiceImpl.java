package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.PublicationDao;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Publication;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:40:19 AM
 */
public class PublicationServiceImpl implements PublicationService {
    private final String UNAVAILABLE_PICTURE = "/images/unavailable.jpg";

    private PublicationDao publicationDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setPublicationDao(PublicationDao publicationDao) {
        this.publicationDao = publicationDao;
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
        return publicationDao.countByFilter(filter);
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
        return publicationDao.countByFilter(filter);
    }

    @Override
    public List<Publication> findByKeyword(String keyword, int start, int count) {
        String paramValue = "%" + keyword + "%";
        CompositeFilter filter = new CompositeFilter(QueryOperator.OR);
        filter.add(new ValueFilter("title", QueryOperator.LIKE, paramValue, "keyword"));
        filter.add(new ValueFilter("content", QueryOperator.LIKE, paramValue, "keyword"));
        return publicationDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, start, count);
    }

    @Override
    public List<Publication> findByKeywordAndPublished(String keyword, int start, int count) {
        String paramValue = "%" + keyword + "%";
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);

        CompositeFilter keywordFilter = new CompositeFilter(QueryOperator.OR);
        keywordFilter.add(new ValueFilter("title", QueryOperator.LIKE, paramValue, "keyword"));
        keywordFilter.add(new ValueFilter("content", QueryOperator.LIKE, paramValue, "keyword"));

        filter.add(keywordFilter);
        filter.add(new ValueFilter("published", QueryOperator.EQUALS, true, "published"));
        return publicationDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, start, count);
    }

    @Override
    public Publication findById(Long id) {
        return publicationDao.findById(id);
    }

    @Override
    public Publication findNext(Publication publication) {
        return publicationDao.findNext(publication);
    }

    @Override
    public Publication findPrevious(Publication publication) {
        return publicationDao.findPrevious(publication);
    }

    @Override
    public void save(Publication publication) throws IOException {
        publicationDao.save(publication);
    }

    @Override
    public void save(Publication publication, InputStream pictureStream, InputStream attachmentStream) throws IOException {
        publicationDao.save(publication);
        if (pictureStream != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.PUBLICATION_PICTURE, publication.getId());
            contentRepositoryDao.saveBinaryContent(path, pictureStream);
        }
        if (attachmentStream != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.PUBLICATION_ATTACHMENT, publication.getId());
            contentRepositoryDao.saveBinaryContent(path, attachmentStream);
        }
    }

    @Override
    public void saveWithPictureOnly(Publication publication, InputStream is) throws IOException {
        publicationDao.save(publication);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.PUBLICATION_PICTURE, publication.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public void saveWithAttachmentOnly(Publication publication, InputStream is) throws IOException {
        publicationDao.save(publication);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.PUBLICATION_ATTACHMENT, publication.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public void removeById(Long id) {
        publicationDao.removeById(id);
    }

    @Override
    public List<Publication> findLatest(int count) {
        Filter filter = new ValueFilter("published", QueryOperator.EQUALS, true, "published");
        return publicationDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, 0, count);
    }

    @Override
    public BufferedImage getPicture(Long id) throws IOException {
        if (id != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.PUBLICATION_PICTURE, id);
            if (contentRepositoryDao.isPathExist(path))
                return contentRepositoryDao.getBufferedImage(path);
        }
        return ImageIO.read(new ClassPathResource(UNAVAILABLE_PICTURE).getInputStream());
    }

    @Override
    public void writeAttachmentContent(Long id, OutputStream outputStream) {
        String path = RepositoryUtils.getPath(RepositoryUtils.PUBLICATION_ATTACHMENT, id);
        contentRepositoryDao.copyBinaryContent(path, outputStream);
    }
}
