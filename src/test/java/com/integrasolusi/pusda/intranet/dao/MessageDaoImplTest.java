package com.integrasolusi.pusda.intranet.dao;

import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.persistence.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.management.Query;
import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 8:20:35 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class MessageDaoImplTest {
    @Autowired
    private MessageDao messageDao;

    @Test
    public void testFindByFilter() {
        List<Long> ids = new LinkedList<Long>();
        ids.add(1L);
        ids.add(4L);
        ids.add(5L);
        ids.add(7L);

        Filter filter = new ValueFilter("id", QueryOperator.IN, ids, "ids");
        List<Message> messages = messageDao.findByFilter(filter);
        for (Message m : messages) {
            System.out.println("m.getId() = " + m.getId());
        }

    }

}
