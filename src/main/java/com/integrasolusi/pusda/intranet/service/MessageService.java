package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.*;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 10:11:31 PM
 */
public interface MessageService {
    MessageLog send(Person sender, String message, List<Long> recipients);

    MessageLog notifyDocumentChange(Person sender, Document oldAttribute, Document newAttribute, List<Long> recipients);

    MessageLog notifyDocumentRevision(DocumentVersion version);

    MessageLog notifyDocumentSetShare(Document document, List<Share> shares);

    Message findById(Long id);

    List<Message> getMessages(Person person, String keyword, int start, int count);

    Long countUnread(Person person);

    Long countMessage(Person person, String keyword);

    void remove(Message message);

    List<Message> findByLog(MessageLog logID);

    void updateHasRead(Message message);
}
