package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Person;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 6:14:46 PM
 */
public class DocumentDaoImpl extends GenericHibernateDaoImpl<Document, Long> implements DocumentDao {


    @Override
    public Long countByPersonAndKeyword(final Person person, final String keyword) {
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT COUNT(DISTINCT d.id) FROM Document d LEFT JOIN d.shares s " +
                        "WHERE ((d.owner = :person) OR (s.person = :person)) " +
                        "AND ((name LIKE :keyword) OR (description LIKE :keyword))";

                Query query = session.createQuery(queryString);
                query.setParameter("person", person);
                query.setParameter("keyword", keyword);

                return query.uniqueResult();
            }
        });
    }

    @Override
    public List<Document> findByPersonAndKeyword(final Person person, final String keyword, final int start, final int count) {
        return (List<Document>) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT DISTINCT d FROM Document d LEFT JOIN d.shares s " +
                        "WHERE ((d.owner = :person) OR (s.person = :person)) " +
                        "AND ((name LIKE :keyword) OR (description LIKE :keyword)) " +
                        "ORDER BY d.id DESC";

                Query query = session.createQuery(queryString);
                query.setParameter("person", person);
                query.setParameter("keyword", keyword);

                query.setFirstResult(start);
                query.setMaxResults(count);

                return query.list();
            }
        });
    }
}
