package com.integrasolusi.pusda.intranet.web.interceptor;

import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 18, 2011
 * Time         : 9:01:29 AM
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = Logger.getLogger(SecurityInterceptor.class);

    private List<String> publicUrls = new LinkedList<String>();
    private String redirectUrl;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("check request URI");
        if (!validateRequest(request)) {
            response.sendRedirect(getRedirectUrl());
            return false;
        }

        return true;
    }

    public boolean validateRequest(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (isPublicUrl(url))
            return true;

        if (HttpSessionUtils.getLoggedPrincipal(request) != null)
            return true;

        return false;
    }

    private boolean isPublicUrl(String url) {
        for (String path : publicUrls) {
            if (pathMatcher.isPattern(path)) {
                if (pathMatcher.match(path, url))
                    return true;
            } else if (StringUtils.equals(path, url)) {
                return true;
            }
        }
        return false;
    }

    public void setPublicUrls(List<String> publicUrls) {
        this.publicUrls = publicUrls;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
