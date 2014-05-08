package com.integrasolusi.pusda.intranet.repository.jackrabbit;

import com.integrasolusi.jcr.JcrCallback;
import com.integrasolusi.jcr.JcrSessionFactory;
import org.apache.log4j.Logger;

import javax.jcr.Session;

/**
 * Programmer   : pancara
 * Date         : 6/22/11
 * Time         : 9:52 AM
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
