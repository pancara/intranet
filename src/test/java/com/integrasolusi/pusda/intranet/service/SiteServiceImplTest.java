package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Site;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 15, 2011
 * Time         : 7:32:24 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml", "classpath:/spring/utils.xml", "classpath:/spring/service.xml", "classpath:/spring/aspect.xml"})
public class SiteServiceImplTest {
    @Autowired
    SiteService siteService;

    @Test
    public void testSave() throws Exception {
        for (int i = 0; i < 5; i++) {
            Site site = new Site();
            site.setName("site " + i);
            site.setUrl("site");
            site.setIndex(i);
            FileInputStream is = new FileInputStream(new File("E:/test-data/kompas.jpg"));
            siteService.save(site, is);
            is.close();
        }
    }

    @Test
    public void testRemove() throws Exception {
        List<Site> sites = siteService.findAlls();
        for (Site site : sites) {
            siteService.remove(site);
        }
    }
}
