package com.integrasolusi;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

/**
 * Programmer   : pancara
 * Date         : Jan 3, 2011
 * Time         : 10:06:21 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml"})
public class PersistenceTest {
    @Autowired
    private BasicDataSource dataSource;

    @Test
    public void isNullDataSource() {
        Assert.notNull(dataSource);
    }
}