package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.DataStore;
import com.integrasolusi.pusda.intranet.persistence.Site;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 2:41:53 PM
 */
public class SiteDaoImpl extends GenericHibernateDaoImpl<Site, Long> implements SiteDao {

    @Override
    public Site findBefore(final Site site) {
        return (Site) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Filter filter = new ValueFilter("index", QueryOperator.LESS, site.getIndex(), "index");

                String queryString = String.format("FROM Site s WHERE %2$s ORDER BY index DESC", getPersistentClass().getCanonicalName(), filter.getExpression());
                Query query = session.createQuery(queryString);
                setQueryParameterValues(query, filter);

                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }

    @Override
    public Site findAfter(final Site site) {
        return (Site) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Filter filter = new ValueFilter("index", QueryOperator.GREATER, site.getIndex(), "index");

                String queryString = String.format("FROM Site s WHERE %2$s ORDER BY index ASC", getPersistentClass().getCanonicalName(), filter.getExpression());
                Query query = session.createQuery(queryString);
                setQueryParameterValues(query, filter);

                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }

    @Override
    public Integer findMaxIndex() {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {

                String queryString = "SELECT MAX(s.index) FROM Site s";
                Query query = session.createQuery(queryString);
                return query.uniqueResult();
            }
        });
    }
}
