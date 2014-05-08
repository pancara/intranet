package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.MessageLog;

/**
 * Programmer   : pancara
 * Date         : Feb 12, 2011
 * Time         : 7:36:14 AM
 */
public interface MessageLogService {
    MessageLog findById(Long id);
}
