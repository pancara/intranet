package com.integrasolusi.jcr;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Account: koko
 * Date: Jan 31, 2011
 * Time: 7:26:48 PM
 */
public interface JcrCallback {
    Object doInJcr(Session session) throws IOException, RepositoryException;
}
