package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.AccountDao;
import com.integrasolusi.pusda.intranet.dao.PersonDao;
import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import com.integrasolusi.pusda.intranet.utils.StringHelper;

import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 19, 2011
 * Time         : 7:27:05 PM
 */
public class RegistrationServiceImpl implements RegistrationService {
    private String registrationMailFrom;
    private PersonDao personDao;
    private AccountDao accountDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setUserDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    public void setRegistrationMailFrom(String registrationMailFrom) {
        this.registrationMailFrom = registrationMailFrom;
    }

    @Override
    public void saveRegistration(Person person, java.io.InputStream is) throws IOException {
        person.getAccount().setRegistrationToken(generateRegistrationToken());
        personDao.save(person);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.AVATAR, person.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    private String generateRegistrationToken() {
        return StringHelper.generateRandomText(255);
    }

    @Override
    public boolean isUserIdAvailable(String userID) {
        List<Account> accounts = accountDao.findByProperty("userID", userID);
        return (accounts.size() == 0);
    }
}
