package com.integrasolusi.pusda.intranet.dao.generic;

import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.persistence.Share;
import org.hibernate.ScrollableResults;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 10:19:55 AM
 */
public interface GenericDao<T, TId> {

    T findById(TId id);

    List<T> findByProperty(String propertyName, Object propertyValue);

    List<T> findByProperty(String propertyName, Object propertyValue, int start, int count);

    List<T> findByPropertyOrderBy(String propertyName, Object propertyValue, String orderProperty, OrderDir orderDir);

    List<T> findByPropertyOrderBy(String propertyName, Object propertyValue, String orderProperty, OrderDir orderDir, int start, int count);

    List<T> findByFilter(Filter filter);

    ScrollableResults findByFilterAsCursor(Filter filter);

    List<T> findByFilter(Filter filter, int start, int count);

    List<T> findByFilterOrderBy(Filter filter, String orderProperty, OrderDir desc);

    List<T> findByFilterOrderBy(Filter filter, String orderProperty, OrderDir desc, int start, int count);

    Long countByFilter(Filter filter);

    T findUniqueByFilter(Filter filter);

    T findUniqueByProperty(String propertyName, Object propertyValue);

    Long countByProperty(String propertyName, Object propertyValue);

    T loadByID(TId id);

    void refresh(T entity);

    Long countAlls();

    T save(T obj);

    void remove(T entity);

    void removeById(TId id);

    void removeByFilter(Filter filter);

    List<T> findAlls();

    List<T> findAlls(int start, int count);

    List<T> findAllsOrderBy(String orderProperty, OrderDir orderDir);

    List<T> findAllsOrderBy(String orderProperty, OrderDir orderDir, int start, int count);

}
