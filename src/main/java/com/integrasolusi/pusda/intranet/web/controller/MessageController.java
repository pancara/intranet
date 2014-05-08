package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.persistence.Message;
import com.integrasolusi.pusda.intranet.persistence.MessageLog;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.MessageLogService;
import com.integrasolusi.pusda.intranet.service.MessageService;
import com.integrasolusi.pusda.intranet.service.PersonService;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.web.form.MessageForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 16, 2011
 * Time         : 11:08:37 AM
 */
@Controller
public class MessageController {
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MessageController.class);
    private MessageService messageService;
    private MessageLogService messageLogService;
    private PersonService personService;
    private PagingUtils pagingUtils;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setMessageLogService(MessageLogService messageLogService) {
        this.messageLogService = messageLogService;
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping("/message/list")
    public ModelAndView listMessage(HttpServletRequest request,
                                    @RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "page", required = false) Integer page) {
        ModelAndView mav = new ModelAndView("message-list");
        if (keyword == null) keyword = "";
        mav.addObject("keyword", keyword);

        if (page == null)
            page = 1;
        mav.addObject("page", page);


        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        Long messageCount = messageService.countMessage(person, keyword);
        mav.addObject("messageCount", messageCount);

        Long unreadMessageCount = messageService.countUnread(person);
        mav.addObject("unreadMessageCount", unreadMessageCount);

        List<Integer> pages = pagingUtils.getPageList(page, messageCount.intValue());
        mav.addObject("pages", pages);

        int start = pagingUtils.getStartRow(page);
        mav.addObject("start", start);
        mav.addObject("messages", messageService.getMessages(person, keyword, start, pagingUtils.getRowPerPage()));
        return mav;
    }


    @RequestMapping(value = "/message/read/{messageID}", method = RequestMethod.GET)
    public ModelAndView readMessage(HttpServletRequest request,
                                    @PathVariable("messageID") Long messageID,
                                    @ModelAttribute("messageForm") MessageForm form) {
        ModelAndView mav = new ModelAndView("message-read");
        Message message = messageService.findById(messageID);
        messageService.updateHasRead(message);
        mav.addObject("message", message);
        List<Long> recipients = new LinkedList<Long>();
        recipients.add(message.getSender().getId());
        form.setRecipients(recipients);
        return mav;
    }

    @RequestMapping(value = "/message/read/{messageID}", method = RequestMethod.POST)
    public ModelAndView responseMessage(HttpServletRequest request,
                                        @PathVariable("messageID") Long messageID,
                                        @ModelAttribute("messageForm") MessageForm form,
                                        Errors errors) {
        Message original = messageService.findById(messageID);
        if (validateMessageForm(form, errors)) {
            Message message = new Message();
            Person sender = HttpSessionUtils.getLoggedPrincipal(request);
            MessageLog log = messageService.send(sender, form.getContent(), form.getRecipients());

            return new ModelAndView(String.format("redirect:/message/sent/%d.html", log.getId()));
        } else {
            ModelAndView mav = new ModelAndView("message-read");
            mav.addObject("message", original);
            return mav;
        }

    }

    @RequestMapping(value = "/message/new", method = RequestMethod.GET)
    public ModelAndView newMessage(HttpServletRequest request, @ModelAttribute("messageForm") MessageForm form) {
        ModelAndView mav = new ModelAndView("message-new");
        return mav;
    }

    @RequestMapping(value = "/message/new", method = RequestMethod.POST)
    public ModelAndView submitNewMessage(HttpServletRequest request,
                                         @ModelAttribute("messageForm") MessageForm form,
                                         Errors errors) {
        if (validateMessageForm(form, errors)) {
            Person sender = HttpSessionUtils.getLoggedPrincipal(request);
            MessageLog log = messageService.send(sender, form.getContent(), form.getRecipients());
            return new ModelAndView(String.format("redirect:/message/sent/%d.html", log.getId()));
        } else {
            ModelAndView mav = new ModelAndView("message-new");
            if (form.getRecipients().size() > 0) {
                Filter filter = new ValueFilter("id", QueryOperator.IN, form.getRecipients(), "ids");
                List<Person> recipients = personService.findByFilter(filter);
                mav.addObject("recipients", recipients);
            }
            return mav;
        }
    }

    @RequestMapping(value = "/message/sent/{logID}")
    public ModelAndView showSendSuccess(@PathVariable("logID") Long logID) {
        ModelAndView mav = new ModelAndView("message-sent");
        MessageLog log = messageLogService.findById(logID);
        List<Message> messages = messageService.findByLog(log);
        mav.addObject("messages", messages);
        return mav;
    }

    private boolean validateMessageForm(MessageForm form, Errors errors) {
        if ((form.getRecipients() == null) || (form.getRecipients().size() == 0))
            errors.reject("recipient.empty", "Tujuan belum diisi");

        if (StringUtils.isEmpty(form.getContent()))
            errors.reject("content.empty", "Pesan belum diisi");

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/message/remove", method = RequestMethod.POST)
    public ModelAndView removeMessage(HttpServletRequest request, @RequestParam(value = "messageID", required = false) Long[] messageIDs) {
        if (messageIDs != null) {
            Person person = HttpSessionUtils.getLoggedPrincipal(request);
            for (Long id : messageIDs) {
                Message message = messageService.findById(id);
                if (person.getId().equals(message.getRecipient().getId())) {
                    messageService.remove(message);
                }
            }
        }
        return new ModelAndView("redirect:/message/list.html");
    }
}
