package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.generic.GenericHibernateDaoImpl;
import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;

/**
 * Programmer   : pancara
 * Date         : Jan 7, 2011
 * Time         : 9:17:08 PM
 */
public class GalleryPictureDaoImpl extends GenericHibernateDaoImpl<GalleryPicture, Long> implements GalleryPictureDao {
    @Override
    public Integer getLastPictureIndex(final Gallery gallery) {
        return (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String queryString = "SELECT MAX(p.index) FROM GalleryPicture p WHERE p.gallery = :gallery ";
                Query query = session.createQuery(queryString);
                query.setParameter("gallery", gallery);
                return query.uniqueResult();
            }
        });
    }
}
