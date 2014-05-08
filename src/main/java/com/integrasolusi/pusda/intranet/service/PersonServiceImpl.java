package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.DocumentDao;
import com.integrasolusi.pusda.intranet.dao.PersonDao;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;
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
 * Date         : Jan 4, 2011
 * Time         : 10:19:54 AM
 */
public class PersonServiceImpl implements PersonService {
    private PersonDao personDao;
    private DocumentDao documentDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    @Override
    public Person findById(Long id) {
        return personDao.findById(id);
    }

    @Override
    public Long countByFilter(Filter filter) {
        return personDao.countByFilter(filter);
    }

    @Override
    public Long countForShare(String keyword, Long documentID) {
        Document document = documentDao.findById(documentID);
        return personDao.countForShare("%" + keyword + "%", document);
    }

    @Override
    public List<Person> findForShare(String keyword, Long documentID, int start, int count) {
        Document document = documentDao.findById(documentID);
        return personDao.findForShare("%" + keyword + "%", document, start, count);
    }

    @Override
    public List<Person> findByFilter(Filter filter) {
        return personDao.findByFilter(filter);
    }

    @Override
    public List<Person> findByFilter(Filter filter, int start, int count) {
        return personDao.findByFilter(filter, start, count);
    }

    @Override
    public List<Person> findAlls() {
        return personDao.findAlls();
    }

    @Override
    public BufferedImage createAvatar(Long id) throws IOException {
        if (id != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.AVATAR, id);
            if (contentRepositoryDao.isPathExist(path))
                return contentRepositoryDao.getBufferedImage(path);
        }
        return getDefaultAvatar();
    }

    private BufferedImage getDefaultAvatar() throws IOException {
        return ImageIO.read(new ClassPathResource("/images/avatar-unavailable.jpg").getInputStream());
    }

    @Override
    public void save(Person person) throws IOException {
        save(person, null);
    }

    @Override
    public void save(Person person, InputStream is) throws IOException {
        personDao.save(person);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.AVATAR, person.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public Long countPersonWithAccount(String keyword) {
        keyword = "%" + keyword + "%";
        return personDao.countPersonWithAccount(keyword);
    }

    @Override
    public Long countPersonWithAccount(String keyword, Boolean confirmed, Boolean approved, Boolean active) {
        keyword = "%" + keyword + "%";
        return personDao.countPersonWithAccount(keyword, confirmed, approved, active);
    }

    @Override
    public List<Person> findPersonWithAccount(String keyword, Integer start, Integer count) {
        keyword = "%" + keyword + "%";
        return personDao.findPersonWithAccount(keyword, start, count);
    }

    @Override
    public List<Person> findPersonWithAccount(String keyword, Boolean confirmed, Boolean approved, Boolean active, Integer start, Integer count) {
        keyword = "%" + keyword + "%";
        return personDao.findPersonWithAccount(keyword, confirmed, approved, active, start, count);
    }


}
