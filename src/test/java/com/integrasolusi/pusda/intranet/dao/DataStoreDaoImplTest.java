package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.DataStore;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Feb 6, 2011
 * Time         : 6:17:23 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class DataStoreDaoImplTest {
    @Autowired
    private DataStoreDao dataStoreDao;

    @Test
    public void findBefore() {
        DataStore dataStore = dataStoreDao.findById(13L);
        DataStore before = dataStoreDao.findBefore(dataStore);
        Assert.assertEquals(12L, before.getId().longValue());
    }
}
