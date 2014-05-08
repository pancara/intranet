package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.AccountService;
import com.integrasolusi.pusda.intranet.service.PersonService;
import com.integrasolusi.pusda.intranet.utils.CaptchaGenerator;
import com.integrasolusi.pusda.intranet.web.form.ProfileForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Feb 9, 2011
 * Time         : 11:17:23 PM
 */

@Controller
public class ProfileController {
    private PersonService personService;
    private AccountService accountService;
    private CaptchaGenerator captchaGenerator;

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setUserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setCaptchaGenerator(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView viewProfile(HttpServletRequest request,
                                    @ModelAttribute("profileForm") ProfileForm form) throws ServletRequestBindingException {
        ModelAndView mav = new ModelAndView("profile");
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        person = personService.findById(person.getId());
        initializeForm(form, person);
        return mav;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ModelAndView submitPerson(HttpServletRequest request,
                                     @ModelAttribute("profileForm") ProfileForm form,
                                     Errors errors) throws ServletRequestBindingException, IOException {
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        person = personService.findById(person.getId());
        if (validateForm(person, form, errors)) {
            person.setName(form.getName());
            person.setAddress(form.getAddress());
            person.setOrganization(form.getOrganization());
            person.setEmail(form.getEmail());
            person.setPhone(form.getPhone());

            if (!StringUtils.isEmpty(form.getNewPassword()))
                person.getAccount().setPassword(form.getNewPassword());

            if (form.getFoto().isEmpty())
                personService.save(person);
            else
                personService.save(person, form.getFoto().getInputStream());

            return new ModelAndView("redirect:/profile/updated.html");
        }
        ModelAndView mav = new ModelAndView("profile");
        // set seletect tab in view
        mav.addObject("selectedTab", ServletRequestUtils.getStringParameter(request, "selectedTab"));
        return mav;
    }

    @RequestMapping(value = "/profile/updated")
    public ModelAndView profileUpdated() {
        return new ModelAndView("profile-updated");
    }

    private boolean validateForm(Person person, ProfileForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getName()))
            errors.reject("name.empty", "Nama lengkap belum diisi");

        if (StringUtils.isEmpty(form.getAddress()))
            errors.reject("address.empty", "Alamat belum diisi");

        if (!EmailValidator.getInstance().isValid(form.getEmail()))
            errors.reject("email.empty", "Email tidak valid");

        if (StringUtils.isEmpty(form.getOrganization()))
            errors.reject("organization.empty", "Instansi belum diisi");

        if (!StringUtils.isEmpty(form.getNewPassword())) {
            if (!StringUtils.equals(person.getAccount().getPassword(), form.getOldPassword()))
                errors.reject("oldpassword.not.match", "Password lama tidak sesuai");

            if (!StringUtils.equals(form.getNewPassword(), form.getConfirmation()))
                errors.reject("confirmation.not.match", "Konfirmasi password tidak sama");

            if (StringUtils.isEmpty(form.getNewPassword()) || (form.getNewPassword().length() < 5))
                errors.reject("password.too.short", "Password minimal 5 karakter");
        }

        return !errors.hasErrors();
    }


    private void initializeForm(ProfileForm form, Person person) {
        form.setUserID(person.getAccount().getUserID());

        form.setName(person.getName());
        form.setAddress(person.getAddress());
        form.setEmail(person.getEmail());
        form.setOrganization(person.getOrganization());
        form.setPhone(person.getPhone());
    }


}
