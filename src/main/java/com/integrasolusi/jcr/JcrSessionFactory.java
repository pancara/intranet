package com.integrasolusi.jcr;

import org.apache.jackrabbit.core.RepositoryImpl;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created by IntelliJ IDEA.
 * Account: koko
 * Date: Jan 31, 2011
 * Time: 7:16:04 PM
 */
public class JcrSessionFactory {
    private Credentials credentials;
    private RepositoryImpl repository;

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void setRepository(RepositoryImpl repository) {
        this.repository = repository;
    }

    public Session login() throws RepositoryException {
        return repository.login(credentials);
    }

    public void logout(Session session) {
        session.logout();
    }

    public void shutdown() {
        repository.shutdown();
    }
}
