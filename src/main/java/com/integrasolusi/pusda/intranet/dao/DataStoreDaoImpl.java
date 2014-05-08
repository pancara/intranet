package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.DataStore;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Feb 3, 2011
 * Time         : 5:46:53 PM
 */
public class DataStoreDaoImpl extends GenericHibernateDaoImpl<DataStore, Long> implements DataStoreDao {
    @Override
    public DataStore findBefore(final DataStore dataStore) {
        return (DataStore) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
                filter.add(new ValueFilter("parent", QueryOperator.IS, dataStore.getParent(), "parent"));
                filter.add(new ValueFilter("index", QueryOperator.LESS, dataStore.getIndex(), "index"));

                String queryString = String.format("FROM DataStore d WHERE %2$s ORDER BY index DESC", getPersistentClass().getCanonicalName(), filter.getExpression());
                Query query = session.createQuery(queryString);
                setQueryParameterValues(query, filter);

                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }

    @Override
    public DataStore findAfter(final DataStore dataStore) {
        return (DataStore) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
                filter.add(new ValueFilter("parent", QueryOperator.IS, dataStore.getParent(), "parent"));
                filter.add(new ValueFilter("index", QueryOperator.GREATER, dataStore.getIndex(), "index"));

                String queryString = String.format("FROM DataStore d WHERE %2$s ORDER BY index ASC", getPersistentClass().getCanonicalName(), filter.getExpression());
                Query query = session.createQuery(queryString);
                setQueryParameterValues(query, filter);

                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }
}
