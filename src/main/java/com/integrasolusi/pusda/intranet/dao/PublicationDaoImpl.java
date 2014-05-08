package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.Publication;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:38:17 AM
 */
public class PublicationDaoImpl extends GenericHibernateDaoImpl<Publication, Long> implements PublicationDao {

    @Override
    public Publication findNext(final Publication publication) {
        return (Publication) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "FROM Publication p WHERE (p.id > :id) ORDER BY p.id ASC";
                Query query = session.createQuery(queryString);
                query.setParameter("id", publication.getId());
                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }

    @Override
    public Publication findPrevious(final Publication publication) {
        return (Publication) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "FROM Publication p WHERE  (p.id < :id) ORDER BY p.id DESC";
                Query query = session.createQuery(queryString);
                query.setParameter("id", publication.getId());
                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }
}
