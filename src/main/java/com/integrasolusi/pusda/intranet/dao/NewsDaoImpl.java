package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.News;
import com.integrasolusi.pusda.intranet.persistence.Publication;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Feb 27, 2011
 * Time         : 9:44:42 AM
 */
public class NewsDaoImpl extends GenericHibernateDaoImpl<News, Long> implements NewsDao {
    @Override
    public News findNext(final News news) {
        return (News) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "FROM News n WHERE (n.id > :id) ORDER BY n.id ASC";
                Query query = session.createQuery(queryString);
                query.setParameter("id", news.getId());
                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }

    @Override
    public News findPrevious(final News news) {
        return (News) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "FROM News n WHERE  (n.id < :id) ORDER BY n.id DESC";
                Query query = session.createQuery(queryString);
                query.setParameter("id", news.getId());
                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }
}
