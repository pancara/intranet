package com.integrasolusi.pusda.intranet.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Programmer   : pancara
 * Date         : Feb 14, 2011
 * Time         : 11:06:58 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class SiteDaoImplTest {
    @Autowired
    private SiteDao siteDao;

    @Test
    public void testFindMaxIndex() throws Exception {
        Integer  maxIndex = siteDao.findMaxIndex();
        System.out.println(maxIndex);
    }
}
