package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.MessageService;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Programmer   : pancara
 * Date         : Jan 21, 2011
 * Time         : 10:23:03 PM
 */
@Controller
public class WebPartsController {
    private static Logger logger = Logger.getLogger(WebPartsController.class);
    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(value = "ui/js/document_ready")
    public ModelAndView getScript(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/javascript");
        return new ModelAndView("js/document_ready");
    }

    @RequestMapping(value = "config/{paramName}/{value}")
    @ResponseBody
    public void setParameter(@PathVariable("paramName") String paramName, @PathVariable("value") String value, HttpServletRequest request) {
        logger.info(String.format("set param %s = > %s", paramName, value));
        HttpSessionUtils.setSessionAttribute(request, paramName, value);
    }

    @RequestMapping(value = "footer")
    public ModelAndView footer(HttpServletRequest request) {
        return new ModelAndView("footer");
    }

    @RequestMapping(value = "banner")
    public ModelAndView getBanner(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("banner");
        Person principal = HttpSessionUtils.getLoggedPrincipal(request);
        mav.addObject("principal", principal);
        return mav;
    }

    @RequestMapping(value = "statusbar")
    public ModelAndView getStatusbar(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("statusbar");
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        Long countNewMessage = messageService.countUnread(person);
        mav.addObject("countNewMessage", countNewMessage);
        return mav;
    }


}
