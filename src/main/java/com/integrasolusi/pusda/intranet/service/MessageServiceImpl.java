package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.*;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.*;
import com.integrasolusi.pusda.intranet.utils.TextGenerator;

import java.util.*;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 10:11:39 PM
 */
public class MessageServiceImpl implements MessageService {
    private MessageDao messageDao;
    private ShareDao shareDao;
    private PersonDao personDao;
    private MessageLogDao messageLogDao;
    private TextGenerator textGenerator;
    private DocumentDao documentDao;

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setMessageLogDao(MessageLogDao messageLogDao) {
        this.messageLogDao = messageLogDao;
    }

    public void setTextGenerator(TextGenerator textGenerator) {
        this.textGenerator = textGenerator;
    }

    public void setShareDao(ShareDao shareDao) {
        this.shareDao = shareDao;
    }

    public void setDocumentDao(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @Override
    public MessageLog send(Person sender, String text, List<Long> recipients) {
        MessageLog log = new MessageLog();
        messageLogDao.save(log);

        for (Long recipient : recipients) {
            Message message = new Message();
            message.setSender(sender);
            message.setRecipient(personDao.findById(recipient));
            message.setContent(text);
            message.setLog(log);

            messageDao.save(message);
        }
        return log;
    }

    @Override
    public Message findById(Long id) {
        return messageDao.findById(id);
    }

    @Override
    public Long countUnread(Person person) {
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
        filter.add(new ValueFilter("recipient", QueryOperator.EQUALS, person, "person"));
        filter.add(new ValueFilter("hasRead", QueryOperator.EQUALS, false, "hasRead"));
        return messageDao.countByFilter(filter);
    }

    @Override
    public Long countMessage(Person person, String keyword) {
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
        filter.add(new ValueFilter("recipient", QueryOperator.EQUALS, person, "person"));
        filter.add(new ValueFilter("content", QueryOperator.LIKE, "%" + keyword + "%", "content"));
        return messageDao.countByFilter(filter);
    }

    @Override
    public List<Message> getMessages(Person person, String keyword, int start, int count) {
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
        filter.add(new ValueFilter("recipient", QueryOperator.EQUALS, person, "person"));
        filter.add(new ValueFilter("content", QueryOperator.LIKE, "%" + keyword + "%", "content"));
        return messageDao.findByFilterOrderBy(filter, "postDate", OrderDir.DESC, start, count);
    }

    @Override
    public void remove(Message message) {
        messageDao.remove(message);
    }

    @Override
    public List<Message> findByLog(MessageLog log) {
        ValueFilter filter = new ValueFilter("log", QueryOperator.EQUALS, log, "log");
        return messageDao.findByFilterOrderBy(filter, "id", OrderDir.ASC);
    }

    @Override
    public void updateHasRead(Message message) {
        message.setHasRead(true);
        messageDao.save(message);
    }

    @Override
    public MessageLog notifyDocumentChange(Person sender, Document oldAttribute, Document newAttribute, List<Long> recipients) {
        MessageLog log = new MessageLog();
        messageLogDao.save(log);

        // generate text
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("oldDocument", oldAttribute);
        model.put("newDocument", newAttribute);
        String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_DOCUMENT_CHANGE, model);

        for (Long recipient : recipients) {
            Message message = new Message();
            message.setSender(sender);
            message.setRecipient(personDao.findById(recipient));
            message.setContent(text);
            message.setType(Message.NOTIFICATION);
            message.setLog(log);

            messageDao.save(message);
        }
        return log;
    }

    @Override
    public MessageLog notifyDocumentRevision(DocumentVersion version) {
        Document document = documentDao.findById(version.getDocument().getId());
        List<Share> shares = shareDao.findByFilter(new ValueFilter("document", QueryOperator.EQUALS, document, "document"));

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("version", version);
        String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_DOCUMENT_REVISION, model);

        MessageLog log = new MessageLog();
        messageLogDao.save(log);

        // send to document owner
        sentRevisionNotification(version.getCommitter(), document.getOwner(), log, text);

        for (Share share : shares)
            sentRevisionNotification(version.getCommitter(), share.getPerson(), log, text);

        return log;
    }

    @Override
    public MessageLog notifyDocumentSetShare(Document document, List<Share> shares) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("document", document);
        String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_DOCUMENT_SET_SHARE, model);

        MessageLog log = new MessageLog();
        messageLogDao.save(log);

        for (Share share : shares) {
            Message message = new Message();
            message.setSender(document.getOwner());
            message.setRecipient(share.getPerson());
            message.setContent(text);
            message.setPostDate(log.getPostDate());
            message.setType(Message.NOTIFICATION);
            message.setLog(log);

            messageDao.save(message);
        }
        return log;
    }

    private void sentRevisionNotification(Person sender, Person recipient, MessageLog log, String text) {
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(text);
        message.setPostDate(log.getPostDate());
        message.setType(Message.NOTIFICATION);
        message.setLog(log);

        messageDao.save(message);
    }
}

