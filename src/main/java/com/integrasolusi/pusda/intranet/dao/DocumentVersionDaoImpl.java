package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:38:54 PM
 */
public class DocumentVersionDaoImpl extends GenericHibernateDaoImpl<DocumentVersion, Long> implements DocumentVersionDao {

    @Override
    public DocumentVersion findLatest(final Document document) {
        return (DocumentVersion) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "FROM DocumentVersion v WHERE document = :document ORDER BY v.versionNumber DESC";
                Query query = session.createQuery(queryString);
                query.setParameter("document", document);
                query.setFirstResult(0);
                query.setMaxResults(1);
                return query.uniqueResult();
            }
        });
    }
}
