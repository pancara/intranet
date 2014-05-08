package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Account;
import com.integrasolusi.pusda.intranet.persistence.News;
import com.integrasolusi.pusda.intranet.persistence.Publication;
import com.integrasolusi.pusda.intranet.persistence.Slide;
import com.integrasolusi.pusda.intranet.service.AccountService;
import com.integrasolusi.pusda.intranet.service.NewsService;
import com.integrasolusi.pusda.intranet.service.PublicationService;
import com.integrasolusi.pusda.intranet.service.SlideService;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Dec 31, 2010
 * Time         : 6:53:58 PM
 */
@Controller
public class HomeController {
    private AccountService accountService;
    private PublicationService publicationService;
    private NewsService newsService;
    private SlideService slideService;

    @Autowired
    public void setUserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Autowired
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Autowired
    public void setSlideService(SlideService slideService) {
        this.slideService = slideService;
    }

    @RequestMapping(value = "home")
    public ModelAndView home(HttpServletRequest request) {
        if (HttpSessionUtils.getLoggedPrincipal(request) == null)
            return new ModelAndView("redirect:login.html");
        ModelAndView mav = new ModelAndView("home");
        List<Publication> publications = publicationService.findLatest(8);
        mav.addObject("publications", publications);

        List<Slide> slides = slideService.findByType(Slide.SLIDE_SECOND);
        mav.addObject("slides", slides);

        List<News> newses = newsService.findLatest(8);
        mav.addObject("newses", newses);
        return mav;
    }

    @RequestMapping(value = "contact")
    public ModelAndView contact(HttpServletRequest request) {
        return new ModelAndView("contact");
    }

    private Account getDefaultUser() {
        return accountService.findById(1L);
    }

}
