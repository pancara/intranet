package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.AccountService;
import com.integrasolusi.pusda.intranet.service.PersonService;
import com.integrasolusi.pusda.intranet.utils.EmailSender;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.utils.RoleUtils;
import com.integrasolusi.pusda.intranet.web.form.AccountStatusForm;
import com.integrasolusi.pusda.intranet.web.form.UserManageForm;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 15, 2011
 * Time         : 12:51:34 PM
 */
@Controller
public class UserManagementController {
    private final static String ACTION_APPROVE = "approve";
    private final static String ACTION_DISABLED = "disabled";

    private PersonService personService;
    private AccountService accountService;
    private EmailSender emailSender;
    private PagingUtils pagingUtils;
    private RoleUtils roleUtils;


    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Autowired
    public void setRoleUtils(RoleUtils roleUtils) {
        this.roleUtils = roleUtils;
    }

    @ModelAttribute(value = "roles")
    public java.util.Map<Integer, String> getRoles() {
        return roleUtils.getRoleNames();
    }

    @RequestMapping("/user/list")
    public ModelAndView listUser(HttpServletRequest request, @ModelAttribute("userManageForm") UserManageForm form) {
        ModelAndView mav = new ModelAndView("user-list");
        if (BooleanUtils.isFalse(form.getUseAdvancedFilter())) {
            Long count = personService.countPersonWithAccount(form.getKeyword());
            mav.addObject("userCount", count);

            int start = pagingUtils.getStartRow(form.getPage());
            mav.addObject("startRow", start);

            List<Person> users = personService.findPersonWithAccount(form.getKeyword(), start, pagingUtils.getRowPerPage());
            mav.addObject("users", users);

            mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));

        } else {
            Long count = personService.countPersonWithAccount(form.getKeyword(),
                    form.getAdvanceFilter().getConfirmed(),
                    form.getAdvanceFilter().getApproved(),
                    form.getAdvanceFilter().getActive());
            mav.addObject("userCount", count);

            int start = pagingUtils.getStartRow(form.getPage());
            mav.addObject("startRow", start);

            List<Person> users = personService.findPersonWithAccount(form.getKeyword(),
                    form.getAdvanceFilter().getConfirmed(),
                    form.getAdvanceFilter().getApproved(),
                    form.getAdvanceFilter().getActive(),
                    start, pagingUtils.getRowPerPage());
            mav.addObject("users", users);

            mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));
        }
        return mav;
    }


    @RequestMapping(value = "/user/edit/{personID}", method = RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request, @PathVariable("personID") Long personID,
                                 @ModelAttribute("accountStatusForm") AccountStatusForm form) {
        ModelAndView mav = new ModelAndView("user-edit");
        Person person = personService.findById(personID);
        mav.addObject("person", person);
        try {
            BeanUtils.copyProperties(form, person.getAccount());
        } catch (Exception e) {
        }

        return mav;
    }

    @RequestMapping(value = "/user/edit/{personID}", method = RequestMethod.POST)
    public ModelAndView submitEditUser(HttpServletRequest request, @PathVariable("personID") Long personID,
                                       @ModelAttribute("accountStatusForm") AccountStatusForm form,
                                       Errors errors) {

        Person person = personService.findById(personID);
        Account account = person.getAccount();
        account.setConfirmed(form.getConfirmed());
        account.setApproved(form.getApproved());
        account.setActive(form.getActive());
        account.setRole(form.getRole());

        accountService.save(account);

        return new ModelAndView("redirect:/user/list.html");
    }


    @RequestMapping(value = "/user/action")
    public ModelAndView doAction(HttpServletRequest request, @ModelAttribute("userManageForm") UserManageForm form) throws ServletRequestBindingException {
        long[] ids = ServletRequestUtils.getLongParameters(request, "id");
        if (ids != null) {
            String action = ServletRequestUtils.getStringParameter(request, "action");
            if (StringUtils.equals(ACTION_APPROVE, action))
                approvePersonIds(ids);
            else if (StringUtils.equals(ACTION_DISABLED, action))
                disabledPersonAccount(ids);
        }

        return listUser(request, form);
    }

    private void approvePersonIds(long[] ids) {
        for (Long id : ids) {
            Person person = personService.findById(id);
            Account account = person.getAccount();
            account.setApproved(true);
            account.setActive(true);
            accountService.save(account);
            emailSender.sendApprovalInformation(person);
        }
    }

    private void disabledPersonAccount(long[] ids) {
        for (Long id : ids) {
            Person person = personService.findById(id);
            Account account = person.getAccount();
            account.setApproved(true);
            account.setActive(false);
            accountService.save(account);
        }
    }
}
