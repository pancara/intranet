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
 * Date         : Jan 3, 2011
 * Time         : 10:19:15 AM
 */
public class PersonDaoImpl extends GenericHibernateDaoImpl<Person, Long> implements PersonDao {

    @Override
    public Long countForShare(final String keyword, final Document document) {
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT COUNT(p.id) FROM Person p WHERE (p.name LIKE :keyword) AND NOT (p IN (SELECT s.person FROM Share s WHERE document = :document))";
                Query query = session.createQuery(queryString);
                query.setParameter("keyword", keyword);
                query.setParameter("document", document);

                return query.uniqueResult();
            }
        });
    }

    @Override
    public List<Person> findForShare(final String keyword, final Document document, final int start, final int count) {
        return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "FROM Person p WHERE (p.name LIKE :keyword) AND NOT (p IN (SELECT s.person FROM Share s WHERE document = :document))";
                Query query = session.createQuery(queryString);
                query.setParameter("keyword", keyword);
                query.setParameter("document", document);

                query.setFirstResult(start);
                query.setMaxResults(count);

                return query.list();
            }
        });
    }

    @Override
    public Long countPersonWithAccount(final String keyword) {
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT COUNT(p) FROM Person p LEFT JOIN p.account a WHERE (p.name LIKE :keyword) OR (p.email LIKE :keyword) OR (a.userID LIKE :keyword)";
                Query query = session.createQuery(queryString);
                query.setParameter("keyword", keyword);

                return query.uniqueResult();
            }
        });
    }

    @Override
    public Long countPersonWithAccount(final String keyword, final Boolean confirmed, final Boolean approved, final Boolean active) {
        return (Long) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT COUNT(p) FROM Person p LEFT JOIN p.account a " +
                        "WHERE ((p.name LIKE :keyword) OR (p.email LIKE :keyword) OR (a.userID LIKE :keyword)) " +
                        "AND (a.confirmed = :confirmed) AND (a.approved = :approved) AND (active = :active)";

                Query query = session.createQuery(queryString);
                query.setParameter("keyword", keyword);
                query.setParameter("confirmed", confirmed);
                query.setParameter("approved", approved);
                query.setParameter("active", active);

                return query.uniqueResult();
            }
        });
    }

    @Override
    public List<Person> findPersonWithAccount(final String keyword, final int start, final int count) {
        return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT p FROM Person p LEFT JOIN p.account a WHERE (p.name LIKE :keyword) OR (p.email LIKE :keyword) OR (a.userID LIKE :keyword)";
                Query query = session.createQuery(queryString);
                query.setParameter("keyword", keyword);
                query.setFirstResult(start);
                query.setMaxResults(count);

                return query.list();
            }
        });
    }

    @Override
    public List<Person> findPersonWithAccount(final String keyword, final Boolean confirmed, final Boolean approved, final Boolean active,
                                              final int start, final int count) {
        return (List<Person>) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT p FROM Person p LEFT JOIN p.account a " +
                        "WHERE ((p.name LIKE :keyword) OR (p.email LIKE :keyword) OR (a.userID LIKE :keyword)) " +
                        "AND (a.confirmed = :confirmed) AND (a.approved = :approved) AND (active = :active)";

                Query query = session.createQuery(queryString);
                query.setParameter("keyword", keyword);
                query.setParameter("confirmed", confirmed);
                query.setParameter("approved", approved);
                query.setParameter("active", active);
                query.setFirstResult(start);
                query.setMaxResults(count);

                return query.list();
            }
        });
    }
}
