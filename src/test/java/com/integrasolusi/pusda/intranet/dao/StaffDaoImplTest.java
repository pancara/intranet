package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.persistence.Staff;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 5, 2011
 * Time         : 4:46:23 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml"})
public class StaffDaoImplTest {
    @Autowired
    private StaffDao staffDao;

    @Test
    public void findAlls() {
        List<Staff> staffList = staffDao.findAlls(0, 10);
    }
}
