package com.integrasolusi.pusda.intranet.repository;

import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;

/**
 * Programmer   : pancara
 * Date         : Jan 6, 2011
 * Time         : 6:15:25 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml"})
public class ContentRepositoryDaoImplTest {
    @Autowired
    private ContentRepositoryDao contentRepositoryDao;
    private String path = "/test/binaryContent";

    @Test
    public void testSaveBinaryContent() throws Exception {
        Assert.assertNotNull(contentRepositoryDao);
        for (int i = 1; i <= 3; i++) {
            String filename = String.format("E:/test-data/%d.jpg", i);
            FileInputStream is = new FileInputStream(new File(filename));
            contentRepositoryDao.saveBinaryContent(path, is);
            is.close();
        }
    }

    @Test
    public void copyBinaryContent() throws IOException {
        FileOutputStream os = new FileOutputStream(new File("E:/test-data/target.jpg"));
        contentRepositoryDao.copyBinaryContent(path, os);
        os.close();
    }


    @Test
    public void saveBinaryContent() throws IOException {
        FileInputStream is = new FileInputStream(new File("E:/test-data/target.jpg"));
        contentRepositoryDao.saveBinaryContent(RepositoryUtils.getPath(RepositoryUtils.SITE, 12L), is);
        is.close();
    }

}
