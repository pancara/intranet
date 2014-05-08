package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.persistence.Slide;
import com.integrasolusi.pusda.intranet.service.AccountService;
import com.integrasolusi.pusda.intranet.service.SlideService;
import com.integrasolusi.pusda.intranet.utils.CaptchaGenerator;
import com.integrasolusi.pusda.intranet.utils.StringHelper;
import com.integrasolusi.pusda.intranet.web.form.LoginForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:14:55 AM
 */

@Controller
public class LoginController {
    public static final String CAPTCHA_LOGIN_KEY = "captcha.login";

    private CaptchaGenerator captchaGenerator;
    private AccountService accountService;
    private SlideService slideService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView get(@ModelAttribute("login") LoginForm form, HttpServletRequest request) {

        // overide captcha in session
        generateCaptchaText(request);
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        if (person != null) {
            return new ModelAndView("redirect:logout.html");
        } else {
            ModelAndView mav = new ModelAndView("login");
            List<Slide> slides = slideService.findByType(Slide.SLIDE_FIRST);
            mav.addObject("slides", slides);
            return mav;
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView authenticate(@ModelAttribute("login") LoginForm loginForm, Errors errors, HttpServletRequest request) {
        String sessionCaptchaText = (String) HttpSessionUtils.getSessionAttribute(request, LoginController.CAPTCHA_LOGIN_KEY);
        generateCaptchaText(request); // generate new captcha and store in session

        // check captcha was generated
        if (StringUtils.isEmpty(sessionCaptchaText))
            return new ModelAndView("login");

        // check captcha
        if (!StringUtils.equalsIgnoreCase(loginForm.getCaptcha(), sessionCaptchaText)) {
            errors.reject("captcha.not.valid", "Captcha tidak valid");
            ModelAndView mav = new ModelAndView("login");
            List<Slide> slides = slideService.findByType(Slide.SLIDE_FIRST);
            mav.addObject("slides", slides);
            return mav;
        }

        Account account = accountService.authenticate(loginForm.getUserID(), loginForm.getPassword());
        if (account != null) {
            if (BooleanUtils.isFalse(account.getConfirmed())) {
                return new ModelAndView("login-account-not-confirmed");
            }

            if (BooleanUtils.isFalse(account.getApproved())) {
                return new ModelAndView("login-account-not-approved");
            }

            Person person = accountService.getOwner(account);
            HttpSessionUtils.setSessionAttribute(request, HttpSessionUtils.CURRENT_PRINCIPAL, person);

            return new ModelAndView("redirect:home.html");
        }

        errors.reject("password.not.valid", "Password tidak valid");

        ModelAndView mav = new ModelAndView("login");
        List<Slide> slides = slideService.findByType(Slide.SLIDE_FIRST);
        mav.addObject("slides", slides);
        return mav;
    }


    @RequestMapping(value = "/login/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = (String) HttpSessionUtils.getSessionAttribute(request, LoginController.CAPTCHA_LOGIN_KEY);
        response.setContentType("image/jpg");
        captchaGenerator.generateCaptchaImage(captchaText, response.getOutputStream());
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSessionUtils.invalidateSession(request);
        return new ModelAndView("logout");
    }


    private String generateCaptchaText(HttpServletRequest request) {
        String captchaText = StringHelper.generateRandomText(4);
        HttpSessionUtils.setSessionAttribute(request, LoginController.CAPTCHA_LOGIN_KEY, captchaText);
        return captchaText;
    }

    @Autowired
    public void setCaptchaGenerator(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setSlideService(SlideService slideService) {
        this.slideService = slideService;
    }
}
