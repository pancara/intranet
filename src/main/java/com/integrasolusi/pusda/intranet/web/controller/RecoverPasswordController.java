package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.AccountService;
import com.integrasolusi.pusda.intranet.service.PersonService;
import com.integrasolusi.pusda.intranet.service.RegistrationService;
import com.integrasolusi.pusda.intranet.utils.CaptchaGenerator;
import com.integrasolusi.pusda.intranet.utils.EmailSender;
import com.integrasolusi.pusda.intranet.utils.StringHelper;
import com.integrasolusi.pusda.intranet.web.form.RecoverPasswordForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import com.sun.xml.internal.ws.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 12:40:02 PM
 */
@Controller

public class RecoverPasswordController {
    private static final String CAPTCHA_RECOVER_PASSWORD_KEY = "captcha.recover.password";
    private RegistrationService registrationService;
    private AccountService accountService;
    private CaptchaGenerator captchaGenerator;
    private EmailSender emailSender;

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setCaptchaGenerator(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
    }

    @Autowired
    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.GET)
    public ModelAndView recoverPassword(HttpServletRequest request, @ModelAttribute("recoverPasswordForm") RecoverPasswordForm form) {
        generateCaptchaText(request);
        return new ModelAndView("recover-password");
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.POST)
    public ModelAndView recoverPassword(HttpServletRequest request, @ModelAttribute("recoverPasswordForm") RecoverPasswordForm form,
                                        Errors errors) {
        String sessionCaptcha = (String) HttpSessionUtils.getSessionAttribute(request, RecoverPasswordController.CAPTCHA_RECOVER_PASSWORD_KEY);
        if (validateForm(sessionCaptcha, form, errors)) {
            Account account = accountService.findByUserID(form.getUserID());
            Person person = accountService.getOwner(account);
            emailSender.sendPasswordRecovery(person);
            return new ModelAndView("redirect:/recover-password/done.html");
        } else {
            generateCaptchaText(request);
            return new ModelAndView("recover-password");
        }
    }

    @RequestMapping("/recover-password/done")
    public ModelAndView recoverPasswordDone(HttpServletRequest request) {
        return new ModelAndView("recover-password-done");
    }

    public boolean validateForm(String sessionCaptcha, RecoverPasswordForm form, Errors errors) {
        if (org.apache.commons.lang.StringUtils.isEmpty(form.getUserID())) {
            errors.reject("userID.empty", "User ID belum diisi.");
        }

        if (!org.apache.commons.lang.StringUtils.equalsIgnoreCase(sessionCaptcha, form.getCaptcha())) {
            errors.reject("captcha.not.valid", "Captcha tidak valid.");
        }

        Account account = accountService.findByUserID(form.getUserID());
        if (account == null) {
            errors.reject("account.not.found", "Account tidak ditemukan");
        }

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/recover-password-captcha")
    public void captchaRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = (String) HttpSessionUtils.getSessionAttribute(request, RecoverPasswordController.CAPTCHA_RECOVER_PASSWORD_KEY);
        response.setContentType("image/jpg");
        captchaGenerator.generateCaptchaImage(captchaText, response.getOutputStream());
    }

    private String generateCaptchaText(HttpServletRequest request) {
        String text = StringHelper.generateRandomText(4);
        HttpSessionUtils.setSessionAttribute(request, RecoverPasswordController.CAPTCHA_RECOVER_PASSWORD_KEY, text);
        return text;
    }
}
