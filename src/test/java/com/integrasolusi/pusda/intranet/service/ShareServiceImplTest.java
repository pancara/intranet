package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.Share;
import com.integrasolusi.pusda.intranet.service.DocumentService;
import com.integrasolusi.pusda.intranet.service.ShareService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 11:37:04 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/persistence.xml", "classpath:/spring/dao.xml", "classpath:/spring/service.xml"})
public class ShareServiceImplTest {
    @Autowired
    private ShareService shareService;

    @Autowired
    private DocumentService documentService;

    @Test
    public void testFindByDocument() throws Exception {
        Document doc = documentService.findById(1L);
        List<Share> shares = shareService.findByDocument(doc);
    }
}
