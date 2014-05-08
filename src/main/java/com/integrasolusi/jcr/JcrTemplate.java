package com.integrasolusi.jcr;

import org.apache.log4j.Logger;

import javax.jcr.Session;

/**
 * Created by IntelliJ IDEA.
 * Account: koko
 * Date: Jan 31, 2011
 * Time: 7:23:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class JcrTemplate {
    private static Logger logger = Logger.getLogger(JcrTemplate.class);
    private JcrSessionFactory sessionFactory;

    public void setSessionFactory(JcrSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Object execute(JcrCallback callback) {
        Session session = null;
        try {
            session = sessionFactory.login();
            return callback.doInJcr(session);
        } catch (Exception e) {
            logger.info(e);
            throw new RuntimeException(e);
        } finally {
            if (session != null) {
                session.logout();
            }
        }
    }
}
