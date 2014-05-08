package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.DataStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 3, 2011
 * Time         : 11:22:26 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/utils.xml", "/spring/service.xml"})
public class DataStoreServiceImplTest {
    @Autowired
    private DataStoreService dataStoreService;

    @Test
    public void findRoot() {
        DataStore root = dataStoreService.getRoot();
        System.out.println(root.getName());
    }

    @Test
    public void moveUp() {
        DataStore dataStore = dataStoreService.findById(13L);
        Integer old = dataStore.getIndex();
        dataStoreService.moveUp(dataStore);
        System.out.println(dataStore.getIndex());
    }

    @Test
    public void moveDown() {
        DataStore dataStore = dataStoreService.findById(13L);
        Integer old = dataStore.getIndex();
        dataStoreService.moveDown(dataStore);
        System.out.println(dataStore.getIndex());
    }
}
