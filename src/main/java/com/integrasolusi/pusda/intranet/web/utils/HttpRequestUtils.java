package com.integrasolusi.pusda.intranet.web.utils;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Programmer   : pancara
 * Date         : Feb 6, 2011
 * Time         : 3:59:00 PM
 */
public class HttpRequestUtils {
    public static boolean isCancelRequest(HttpServletRequest request, String paramName) {
        try {
            return ServletRequestUtils.getStringParameter(request, paramName) != null;
        } catch (ServletRequestBindingException e) {
            throw new RuntimeException(e);
        }
    }
}
