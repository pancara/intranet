package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Person;

import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Jan 19, 2011
 * Time         : 7:26:57 PM
 */
public interface RegistrationService {

    boolean isUserIdAvailable(String userID);

    void saveRegistration(Person person, java.io.InputStream is) throws IOException;
}
