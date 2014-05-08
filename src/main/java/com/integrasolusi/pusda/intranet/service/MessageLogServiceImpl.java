package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.MessageLogDao;
import com.integrasolusi.pusda.intranet.persistence.MessageLog;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:36:21 AM
 */
public class MessageLogServiceImpl implements MessageLogService {
    private MessageLogDao messageLogDao;

    public void setMessageLogDao(MessageLogDao messageLogDao) {
        this.messageLogDao = messageLogDao;
    }

    @Override
    public MessageLog findById(Long id) {
        return messageLogDao.findById(id);
    }
}
