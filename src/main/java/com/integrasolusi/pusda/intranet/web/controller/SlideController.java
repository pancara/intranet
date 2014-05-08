package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.DataStore;
import com.integrasolusi.pusda.intranet.persistence.Site;
import com.integrasolusi.pusda.intranet.persistence.Slide;
import com.integrasolusi.pusda.intranet.service.SlideService;
import com.integrasolusi.pusda.intranet.web.form.SiteForm;
import com.integrasolusi.pusda.intranet.web.form.SlideForm;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 27, 2011
 * Time         : 4:35:55 PM
 */
@Controller
public class SlideController {
    private SlideService slideService;

    @Autowired
    public void setSlideService(SlideService slideService) {
        this.slideService = slideService;
    }

    @RequestMapping(value = "/slide/list/{type}", method = RequestMethod.GET)
    public ModelAndView listSlide(HttpServletRequest request, @PathVariable("type") Integer type) {
        ModelAndView mav = new ModelAndView("slide-list");
        mav.addObject("type", type);
        List<Slide> slides = slideService.findByType(type);
        mav.addObject("slides", slides);
        return mav;
    }

    @RequestMapping(value = "/slide/picture/{slideID}", method = RequestMethod.GET)
    public void downloadDocument(@PathVariable("slideID") Long slideID, HttpServletResponse response) throws IOException {
        Slide slide = slideService.findById(slideID);
        response.setHeader("Content-Disposition:", String.format("attachment; filename = %s", slide.getFilename()));
        slideService.writeContent(slideID, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/slide/{type}/new", method = RequestMethod.GET)
    public ModelAndView newSlide(HttpServletRequest request,
                                 @PathVariable("type") Integer type,
                                 @ModelAttribute("slideForm") SlideForm form) {
        ModelAndView mav = new ModelAndView("slide-new");
        mav.addObject("type", type);
        return mav;
    }

    @RequestMapping(value = "/slide/{type}/new", method = RequestMethod.POST)
    public ModelAndView submitNewSlide(HttpServletRequest request,
                                       @PathVariable("type") Integer type,
                                       @ModelAttribute("slideForm") SlideForm form,
                                       Errors errors) throws IOException {
        if (validateSlide(form, errors)) {
            Slide slide = new Slide();
            slide.setDescription(form.getDescription());
            slide.setType(type);

            if ((form == null) || (form.getPicture().isEmpty()))
                slideService.save(slide);
            else {
                slide.setFilename(form.getPicture().getOriginalFilename());
                slideService.save(slide, form.getPicture().getInputStream());
            }

            ModelAndView mav = new ModelAndView(String.format("redirect:/slide/list/%d.html", type));
            mav.addObject("type", type);
            return mav;

        } else {
            ModelAndView mav = new ModelAndView("slide-new");
            mav.addObject("type", type);
            return mav;
        }
    }

    @RequestMapping(value = "/slide/{type}/edit/{slideID}", method = RequestMethod.GET)
    public ModelAndView editSlide(HttpServletRequest request,
                                  @PathVariable("type") Long type,
                                  @PathVariable("slideID") Long slideID,
                                  @ModelAttribute("slideForm") SlideForm form) {
        ModelAndView mav = new ModelAndView("slide-edit");
        Slide slide = slideService.findById(slideID);
        form.setDescription(slide.getDescription());

        mav.addObject("type", type);
        mav.addObject("slide", slide);
        return mav;
    }

    @RequestMapping(value = "/slide/{type}/edit/{slideID}", method = RequestMethod.POST)
    public ModelAndView editSlide(HttpServletRequest request,
                                  @PathVariable("type") Long type,
                                  @PathVariable("slideID") Long slideID,
                                  @ModelAttribute("slideForm") SlideForm form,
                                  Errors errors) throws IOException {
        Slide slide = slideService.findById(slideID);
        if (validateSlide(form, errors)) {
            slide.setDescription(form.getDescription());
            if ((form == null) || (form.getPicture().isEmpty()))
                slideService.save(slide);
            else {
                slide.setFilename(form.getPicture().getOriginalFilename());
                slideService.save(slide, form.getPicture().getInputStream());
            }

            ModelAndView mav = new ModelAndView(String.format("redirect:/slide/list/%d.html", type));
            mav.addObject("type", type);
            return mav;
        } else {
            ModelAndView mav = new ModelAndView("slide-edit");
            mav.addObject("type", type);
            mav.addObject("slide", slide);
            return mav;
        }
    }

    public boolean validateSlide(SlideForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getDescription()))
            errors.reject("description.empty", "Keterangan belum diisi.");

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/slide/{type}/remove", method = RequestMethod.POST)
    public ModelAndView removeSite(HttpServletRequest request,
                                   @PathVariable("type") Integer type,
                                   @RequestParam("slideID") Long[] slideIDs) throws IOException {
        for (Long id : slideIDs) {
            Slide slide = slideService.findById(id);
            slideService.remove(slide);
        }

        return new ModelAndView(String.format("redirect:/slide/list/%d.html", type));
    }
}
