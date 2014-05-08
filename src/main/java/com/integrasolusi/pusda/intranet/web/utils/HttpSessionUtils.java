package com.integrasolusi.pusda.intranet.web.utils;

import com.integrasolusi.pusda.intranet.persistence.Person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 5:38:57 PM
 */
public class HttpSessionUtils {
    public static final String CURRENT_PRINCIPAL = "current_principal";

    public static void setSessionAttribute(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    public static void setSessionAttribute(HttpServletRequest request, String key, Object value) {
        setSessionAttribute(request.getSession(), key, value);
    }

    public static Object getSessionAttribute(HttpSession session, String key) {
        return session.getAttribute(key);
    }

    public static Object getSessionAttribute(HttpServletRequest request, String key) {
        return getSessionAttribute(request.getSession(), key);
    }

    public static void removeSessionAttribute(HttpSession session, String key) {
        session.removeAttribute(key);
    }

    public static void removeSessionAttribute(HttpServletRequest request, String key) {
        removeSessionAttribute(request.getSession(), key);
    }

    public static void invalidateSession(HttpSession session) {
        Enumeration<String> attributes = session.getAttributeNames();
        while (attributes.hasMoreElements()) {
            String attribute = attributes.nextElement();
            session.removeAttribute(attribute);
        }
        session.invalidate();
    }

    public static void invalidateSession(HttpServletRequest request) {
        invalidateSession(request.getSession());
    }

    public static Person getLoggedPrincipal(HttpServletRequest request) {
        return (Person) HttpSessionUtils.getSessionAttribute(request, HttpSessionUtils.CURRENT_PRINCIPAL);
    }

}
