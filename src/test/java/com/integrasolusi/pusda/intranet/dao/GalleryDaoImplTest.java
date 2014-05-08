package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.persistence.Gallery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 8, 2011
 * Time         : 9:20:02 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class GalleryDaoImplTest {
    @Autowired
    private GalleryDao galleryDao;

    @Test
    public void findAlls() {
        List<Gallery> galleryList = galleryDao.findAlls(0, 100);
    }

    @Test
    public void findByFilter() {
        CompositeFilter filter = new CompositeFilter();
        filter.setOperator(QueryOperator.OR);
        filter.add(new ValueFilter("title", QueryOperator.LIKE, "%a%", "title"));
        filter.add(new ValueFilter("description", QueryOperator.LIKE, "%a%", "title"));
        List<Gallery> galleryList = galleryDao.findByFilter(filter, 0, 100);
        for (Gallery g : galleryList)
            System.out.println(g.getId());

    }

    @Test
    public void countByFilter() {
        CompositeFilter filter = new CompositeFilter();
        filter.setOperator(QueryOperator.OR);
        filter.add(new ValueFilter("title", QueryOperator.LIKE, "%a%", "title"));
        filter.add(new ValueFilter("description", QueryOperator.LIKE, "%a%", "title"));
        Long count = galleryDao.countByFilter(filter);
        System.out.println("count = " + count);
    }

}
