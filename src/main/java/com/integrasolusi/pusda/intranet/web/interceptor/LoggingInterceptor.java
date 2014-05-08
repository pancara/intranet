package com.integrasolusi.pusda.intranet.web.interceptor;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Programmer   : pancara
 * Date         : Feb 18, 2011
 * Time         : 8:53:46 AM
 */
public class LoggingInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(request.getRequestURI());
        return true;
    }
}
