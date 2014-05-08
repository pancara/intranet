package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.AccountService;
import com.integrasolusi.pusda.intranet.service.RegistrationService;
import com.integrasolusi.pusda.intranet.utils.CaptchaGenerator;
import com.integrasolusi.pusda.intranet.utils.EmailSender;
import com.integrasolusi.pusda.intranet.utils.StringHelper;
import com.integrasolusi.pusda.intranet.web.form.RegistrationConfirmationForm;
import com.integrasolusi.pusda.intranet.web.form.RegistrationForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Jan 20, 2011
 * Time         : 7:45:14 AM
 */
@Controller
public class RegistrationController {
    public static final String CAPTCHA_REGISTRATION_KEY = "captcha.registration";
    public static final String CAPTCHA_REGISTRATION_CONFIRM_KEY = "captcha.registration.confirm";

    private CaptchaGenerator captchaGenerator;
    private RegistrationService registrationService;
    private AccountService accountService;
    private EmailSender emailSender;

    @Autowired
    public void setCaptchaGenerator(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
    }

    @Autowired
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Autowired
    public void setUserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @RequestMapping(value = "registration/entry", method = RequestMethod.GET)
    public ModelAndView viewForm(HttpServletRequest request, @ModelAttribute("registrationForm") RegistrationForm form, Errors errors) throws IOException {
        generateRegistrationCaptchaText(request); // overide captcha in session
        return new ModelAndView("registration-entry");
    }

    @RequestMapping(value = "registration/entry", method = RequestMethod.POST)
    public ModelAndView submitRegistration(@ModelAttribute("registrationForm") RegistrationForm form, Errors errors, HttpServletRequest request) throws IOException {
        String sessionCaptchaText = (String) HttpSessionUtils.getSessionAttribute(request, RegistrationController.CAPTCHA_REGISTRATION_KEY);
        generateRegistrationCaptchaText(request); // overide captcha in session

        if (StringUtils.isEmpty(sessionCaptchaText))
            return new ModelAndView("registration-entry");

        boolean isValid = validateRegistrationForm(form, sessionCaptchaText, errors);
        if (isValid) {
            Person person = createPerson(form);
            if (form.getProfilePicture().isEmpty()) {
                registrationService.saveRegistration(person, null);
            } else {
                registrationService.saveRegistration(person, form.getProfilePicture().getInputStream());
            }
            emailSender.sendConfirmationRegistrationEmail(person);
            return new ModelAndView("redirect:/registration/success.html");
        } else {
            return new ModelAndView("registration-entry");
        }
    }

    @RequestMapping(value = "registration/success")
    public ModelAndView registrationSuccess() {
        return new ModelAndView("registration-success");
    }

    @RequestMapping(value = "registration/confirm", method = RequestMethod.GET)
    public ModelAndView confirmRegistration(HttpServletRequest request, @ModelAttribute("confirmationForm") RegistrationConfirmationForm form) {
        generateConfirmationCaptchaText(request);
        ModelAndView mav = new ModelAndView("registration-confirm");
        return mav;
    }

    @RequestMapping(value = "registration/confirm", method = RequestMethod.POST)
    public ModelAndView doConfirmation(HttpServletRequest request,
                                       @ModelAttribute("confirmationForm") RegistrationConfirmationForm form) {
        String captchaValue = (String) HttpSessionUtils.getSessionAttribute(request, CAPTCHA_REGISTRATION_CONFIRM_KEY);
        generateConfirmationCaptchaText(request); // override captcha

        if (StringUtils.equalsIgnoreCase(captchaValue, form.getCaptcha())) {
            Account account = accountService.findById(form.getId());
            if (StringUtils.equals(account.getRegistrationToken(), form.getToken())) {
                account.setConfirmed(true);
                accountService.save(account);
                return new ModelAndView("registration-confirmed");
            }
        }
        return new ModelAndView("registration-confirm");
    }

    private Person createPerson(RegistrationForm form) {
        Account account = new Account();
        account.setUserID(form.getUserID());
        account.setPassword(form.getPassword());

        Person person = new Person();
        person.setName(form.getName());
        person.setAddress(form.getAddress());
        person.setOrganization(form.getOrganization());
        person.setPhone(form.getPhone());
        person.setEmail(form.getEmail());
        person.setAccount(account);
        return person;
    }

    private boolean validateRegistrationForm(RegistrationForm form, String captchaText, Errors errors) {
        if (StringUtils.isEmpty(form.getName()))
            errors.reject("name.empty", "Nama lengkap belum diisi");

        if (StringUtils.isEmpty(form.getAddress()))
            errors.reject("address.empty", "Alamat belum diisi");

        if (!EmailValidator.getInstance().isValid(form.getEmail()))
            errors.reject("email.not.valid", "Email tidak valid");

        if (StringUtils.isEmpty(form.getOrganization()))
            errors.reject("organization.empty", "Instansi belum diisi");

        if (!registrationService.isUserIdAvailable(form.getUserID()))
            errors.reject("userID.not.available", "Account ID tidak tersedia");

        if (!StringUtils.equalsIgnoreCase(form.getCaptcha(), captchaText))
            errors.reject("captcha.not.valid", "Captcha tidak valid");

        if (!StringUtils.equals(form.getPassword(), form.getConfirmation()))
            errors.reject("confirmation.not.match", "Konfirmasi password tidak sama");

        if ((form.getPassword() == null) || (form.getPassword().length() < 5))
            errors.reject("password.too.short", "Password minimal 5 karakter");


        return !errors.hasErrors();
    }

    @RequestMapping(value = "registration/captcha")
    public void captchaRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = (String) HttpSessionUtils.getSessionAttribute(request, RegistrationController.CAPTCHA_REGISTRATION_KEY);
        response.setContentType("image/jpg");
        captchaGenerator.generateCaptchaImage(captchaText, response.getOutputStream());
    }

    @RequestMapping(value = "registration/confirm/captcha")
    public void captchaConfirmation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = (String) HttpSessionUtils.getSessionAttribute(request, RegistrationController.CAPTCHA_REGISTRATION_CONFIRM_KEY);
        response.setContentType("image/jpg");
        captchaGenerator.generateCaptchaImage(captchaText, response.getOutputStream());
    }

    private String generateRegistrationCaptchaText(HttpServletRequest request) {
        String text = StringHelper.generateRandomText(4);
        HttpSessionUtils.setSessionAttribute(request, RegistrationController.CAPTCHA_REGISTRATION_KEY, text);
        return text;
    }

    private String generateConfirmationCaptchaText(HttpServletRequest request) {
        String text = StringHelper.generateRandomText(4);
        HttpSessionUtils.setSessionAttribute(request, RegistrationController.CAPTCHA_REGISTRATION_CONFIRM_KEY, text);
        return text;
    }

}
