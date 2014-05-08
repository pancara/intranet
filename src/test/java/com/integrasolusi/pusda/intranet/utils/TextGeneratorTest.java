package com.integrasolusi.pusda.intranet.utils;

import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import com.integrasolusi.pusda.intranet.persistence.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;

import com.integrasolusi.pusda.intranet.persistence.Account;

/**
 * Programmer   : pancara
 * Date         : Jan 19, 2011
 * Time         : 9:16:36 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/utils.xml"})
public class TextGeneratorTest {
    @Autowired
    private TextGenerator textGenerator;

    @Test
    public void testGetRegistrationConfirmationText() throws Exception {
        java.util.Map model = new HashMap();
        Account account = new Account();
        account.setId(100L);
        account.setUserID("-- NEW USER --");
        account.setPassword("--PASWORD--");
        account.setRegistrationToken("XXX XXX XXX");
        model.put("account", account);
        String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_REGISTRATION_CONFIRMATION, model);
        System.out.println("text = " + text);
    }

    @Test
    public void getNotificationDocumentChangeText() {
        java.util.Map model = new HashMap();

        Document oldDoc = new Document();
        oldDoc.setId(999L);
        oldDoc.setName("Judul Lama");
        oldDoc.setDescription("keterangan lama");

        model.put("old", oldDoc);

        Document newDoc = new Document();
        newDoc.setId(999L);
        newDoc.setName("judul baru");
        newDoc.setDescription("keterangan baru");
        model.put("new", newDoc);

        String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_DOCUMENT_CHANGE, model);
        System.out.println("text = " + text);

    }

    @Test
    public void getNotificationDocumentRevisionText() {
        DocumentVersion version = new DocumentVersion();
        version.setVersionNumber(99);
        version.setCommitDate(new Date());

        version.setCommitter(new Person());
        version.getCommitter().setName("Badu");
        version.getCommitter().setEmail("badu@gmail.com");

        version.setDocument(new Document());
        version.getDocument().setName("Document title");
        version.getDocument().setDescription("document description");

        java.util.Map<String, Object> model = new HashMap<String, Object>();
        model.put("version", version);
        String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_DOCUMENT_REVISION, model);
        System.out.println("text = " + text);

    }
}


