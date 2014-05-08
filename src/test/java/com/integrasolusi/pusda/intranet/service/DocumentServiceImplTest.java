package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;

/**
 * Programmer   : pancara
 * Date         : Jan 26, 2011
 * Time         : 4:31:32 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/persistence.xml", "/spring/dao.xml", "/spring/utils.xml", "/spring/service.xml"})
public class DocumentServiceImplTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentVersionService documentVersionService;

    @Test
    public void testRemoveById() throws Exception {
        documentService.removeById(8L);
    }

    @Test
    public void writeDocumentContent() throws IOException {
        DocumentVersion version = documentVersionService.findById(10L);
        InputStream is = new FileInputStream(new File("E:/test-data/1.jpg"));
        documentVersionService.save(version.getDocument(),version,  is);
        is.close();
        
        OutputStream os = new FileOutputStream(new File("E:/test-data/version-10.dat"));
        documentService.writeDocumentContent(version, os);
        os.close();
    }
}
