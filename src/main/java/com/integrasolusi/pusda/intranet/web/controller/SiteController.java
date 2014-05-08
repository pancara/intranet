package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Site;
import com.integrasolusi.pusda.intranet.service.SiteService;
import com.integrasolusi.pusda.intranet.web.form.SiteForm;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 2:44:18 PM
 */
@Controller
public class SiteController {
    private SiteService siteService;

    @Autowired
    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    @RequestMapping("/site/view")
    public ModelAndView viewSites(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("site-view");
        List<Site> siteList = siteService.findViewable();
        mav.addObject("siteList", siteList);
        return mav;
    }

    @RequestMapping("/site/list")
    public ModelAndView listSite(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("site-list");
        List<Site> sites = siteService.findAlls();
        mav.addObject("sites", sites);
        return mav;
    }

    @RequestMapping(value = "/site/new", method = RequestMethod.GET)
    public ModelAndView newSite(HttpServletRequest request,
                                @ModelAttribute("siteForm") SiteForm form) {
        ModelAndView mav = new ModelAndView("site-new");
        return mav;
    }

    @RequestMapping(value = "/site/new", method = RequestMethod.POST)
    public ModelAndView submitNewsite(HttpServletRequest request,
                                      @ModelAttribute("siteForm") SiteForm form,
                                      Errors errors) throws IOException {
        if (validateSite(form, errors)) {
            Site site = new Site();
            site.setName(form.getName());
            site.setUrl(form.getUrl());
            site.setActive(form.getActive());
            site.setIndex(siteService.getNewIndex() + 1);
            if ((form == null) || (form.getPicture().isEmpty()))
                siteService.save(site);
            else
                siteService.save(site, form.getPicture().getInputStream());

            return new ModelAndView("redirect:/site/list.html");

        } else {
            return new ModelAndView("site-new");
        }
    }

    @RequestMapping(value = "/site/edit/{siteID}", method = RequestMethod.GET)
    public ModelAndView newSite(HttpServletRequest request,
                                @PathVariable("siteID") Long siteID,
                                @ModelAttribute("siteForm") SiteForm form) {
        ModelAndView mav = new ModelAndView("site-edit");
        Site site = siteService.findById(siteID);
        form.setName(site.getName());
        form.setUrl(site.getUrl());
        form.setActive(site.getActive());

        mav.addObject("site", site);
        return mav;
    }

    @RequestMapping(value = "/site/edit/{siteID}", method = RequestMethod.POST)
    public ModelAndView submitEditSite(HttpServletRequest request,
                                       @PathVariable("siteID") Long siteID,
                                       @ModelAttribute("siteForm") SiteForm form,
                                       Errors errors) throws IOException {
        Site site = siteService.findById(siteID);
        if (validateSite(form, errors)) {
            site.setName(form.getName());
            site.setUrl(form.getUrl());
            site.setActive(form.getActive());
            if ((form == null) || (form.getPicture().isEmpty()))
                siteService.save(site);
            else
                siteService.save(site, form.getPicture().getInputStream());

            return new ModelAndView("redirect:/site/list.html");

        }

        ModelAndView mav = new ModelAndView("site-edit");
        mav.addObject("site", site);
        return mav;
    }

    public boolean validateSite(SiteForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getName()))
            errors.reject("name.empty", "Nama belum diisi.");

        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(form.getUrl()))
            errors.reject("url.not.valid", "URL tidak valid");

        return !errors.hasErrors();
    }


    @RequestMapping(value = "/site/remove", method = RequestMethod.POST)
    public ModelAndView removeSite(HttpServletRequest request, @RequestParam("siteID") Long[] siteIDs) throws IOException {
        for (Long siteID : siteIDs) {
            Site site = siteService.findById(siteID);
            siteService.remove(site);
        }

        return new ModelAndView("redirect:/site/list.html");
    }


    @RequestMapping("/site/picture/{siteID}")
    public void getSitePicture(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("siteID") Long siteID) throws IOException {
        Site site = siteService.findById(siteID);
        response.setHeader("Content-Disposition:", String.format("attachment; filename = %s", site.getFilename()));
        siteService.writePictureContent(siteID, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @RequestMapping("/json/site/moveup")
    public MappingJacksonJsonView moveUpSite(HttpServletRequest request, @RequestParam("id") Long id) {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        Site site = siteService.findById(id);
        siteService.moveUp(site);
        jsonView.addStaticAttribute("result", true);
        return jsonView;
    }

    @RequestMapping("/json/site/movedown")
    public MappingJacksonJsonView moveDownSite(HttpServletRequest request, @RequestParam("id") Long id) {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        Site site = siteService.findById(id);
        siteService.moveDown(site);
        jsonView.addStaticAttribute("result", true);
        return jsonView;
    }
}
