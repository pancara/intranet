package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:18:47 AM
 */
public class AccountDaoImpl extends GenericHibernateDaoImpl<Account, Long> implements AccountDao {
    @Override
    public Person getOwner(final Account account) {
        return (Person) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT p FROM Person p WHERE p.account = :account";
                Query query = session.createQuery(queryString);
                query.setParameter("account", account);
                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }
}
