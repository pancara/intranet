package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Publication;
import com.integrasolusi.pusda.intranet.service.PublicationService;
import com.integrasolusi.pusda.intranet.utils.ImageUtils;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.web.form.PublicationForm;
import com.integrasolusi.pusda.intranet.web.form.PublicationSearchForm;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:41:32 AM
 */

@Controller
public class PublicationController {
    private static Logger logger = Logger.getLogger(PublicationController.class);

    private static final String ACTION_PUBLISH = "publish";
    private static final String ACTION_REMOVE = "remove";

    private PublicationService publicationService;
    private PagingUtils pagingUtils;

    @Autowired
    public void setPublicationService(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping("/publication/list")
    public ModelAndView listPublication(HttpServletRequest request, @ModelAttribute("publicationSearchForm") PublicationSearchForm form) {
        ModelAndView mav = new ModelAndView("publication-list");
        Long count = publicationService.countByKeyword(form.getKeyword());
        mav.addObject("publicationCount", count);

        int start = pagingUtils.getStartRow(form.getPage());
        mav.addObject("startRow", start);

        List<Publication> publications = publicationService.findByKeyword(form.getKeyword(),
                start, pagingUtils.getRowPerPage());
        mav.addObject("publications", publications);

        mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));
        return mav;
    }

    @RequestMapping(value = "/publication/action")
    public ModelAndView doAction(HttpServletRequest request, @ModelAttribute("publicationSearchForm") PublicationSearchForm form) throws ServletRequestBindingException, IOException {
        long[] ids = ServletRequestUtils.getLongParameters(request, "id");
        if (ids != null) {
            String action = ServletRequestUtils.getStringParameter(request, "action");
            if (StringUtils.equals(ACTION_PUBLISH, action))
                doPublish(ids);
            else if (StringUtils.equals(ACTION_REMOVE, action))
                doRemove(ids);
        }

        return new ModelAndView("redirect:/publication/list.html");
    }

    private void doPublish(long[] ids) throws IOException {
        for (Long id : ids) {
            Publication publication = publicationService.findById(id);
            publication.setPublished(true);
            publicationService.save(publication);
        }
    }

    private void doRemove(long[] ids) {
        for (Long id : ids)
            publicationService.removeById(id);
    }

    @RequestMapping(value = "/publication/entry", method = RequestMethod.GET)
    public ModelAndView entryPublication(HttpServletRequest request, @ModelAttribute("publicationForm") PublicationForm form) {
        return new ModelAndView("publication-entry");
    }

    @RequestMapping(value = "/publication/entry", method = RequestMethod.POST)
    public ModelAndView entryPublication(HttpServletRequest request, @ModelAttribute("publicationForm") PublicationForm form, Errors errors) throws IOException {

        if (validatePublicationForm(form, errors)) {
            Publication publication = new Publication();
            publication.setTitle(form.getTitle());
            publication.setContent(form.getText());
            publication.setPublished(form.getPublished());
            try {
                savePublication(form, publication);
            } catch (Exception e) {
                logger.info("error saving publication", e);
                return new ModelAndView("publication-entry");
            }

            return new ModelAndView("redirect:/publication/list.html");
        }
        return new ModelAndView("publication-entry");
    }

    @RequestMapping(value = "/publication/edit/{publicationID}", method = RequestMethod.GET)
    public ModelAndView editPublication(HttpServletRequest request, @PathVariable("publicationID") Long publicationID,
                                        @ModelAttribute("publicationForm") PublicationForm form) {
        ModelAndView mav = new ModelAndView("publication-edit");
        Publication publication = publicationService.findById(publicationID);
        mav.addObject("publication", publication);

        form.setTitle(publication.getTitle());
        form.setText(publication.getContent());
        form.setPublished(publication.getPublished());
        return mav;
    }

    @RequestMapping(value = "/publication/edit/{publicationID}", method = RequestMethod.POST)
    public ModelAndView editPublication(HttpServletRequest request, @PathVariable("publicationID") Long publicationID,
                                        @ModelAttribute("publicationForm") PublicationForm form, Errors errors) throws IOException {
        Publication publication = publicationService.findById(publicationID);
        if (validatePublicationForm(form, errors)) {
            publication.setTitle(form.getTitle());
            publication.setContent(form.getText());
            publication.setPublished(form.getPublished());
            try {
                savePublication(form, publication);
            } catch (Exception e) {
                logger.info("error saving publication.", e);

                ModelAndView mav = new ModelAndView("publication-edit");
                mav.addObject("publication", publication);
                return mav;
            }
            return new ModelAndView("redirect:/publication/list.html");
        }
        ModelAndView mav = new ModelAndView("publication-edit");
        mav.addObject("publication", publication);
        return mav;
    }

    private void savePublication(PublicationForm form, Publication publication) throws IOException {
        boolean picture_empty = (form.getPicture() == null || form.getPicture().isEmpty());
        boolean attachment_empty = (form.getAttachment() == null || form.getAttachment().isEmpty());
        if (!picture_empty && !attachment_empty) {
            publication.setAttachmentFilename(form.getAttachment().getOriginalFilename());
            publicationService.save(publication, form.getPicture().getInputStream(), form.getAttachment().getInputStream());
        } else if (!picture_empty) {
            publicationService.saveWithPictureOnly(publication, form.getAttachment().getInputStream());
        } else if (!attachment_empty) {
            publication.setAttachmentFilename(form.getAttachment().getOriginalFilename());
            publicationService.saveWithAttachmentOnly(publication, form.getAttachment().getInputStream());
        } else {
            publicationService.save(publication);
        }
    }

    private boolean validatePublicationForm(PublicationForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getTitle()))
            errors.reject("title.empty", "Judul belum diisi");

        if (StringUtils.isEmpty(form.getText()))
            errors.reject("content.empty", "Content pengumuman belum diisi");

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/publication/read/{publicationID}", method = RequestMethod.GET)
    public ModelAndView readPublication(HttpServletRequest request, @PathVariable("publicationID") Long publicationID) {
        ModelAndView mav = new ModelAndView("publication-read");
        Publication publication = publicationService.findById(publicationID);
        mav.addObject("publication", publication);

        Publication next = publicationService.findNext(publication);
        mav.addObject("next", next);

        Publication previous = publicationService.findPrevious(publication);
        mav.addObject("previous", previous);
        return mav;
    }

    @RequestMapping(value = "/publication/picture/{publicationID}")
    public void getPublicationPicture(HttpServletResponse response,
                                      @PathVariable("publicationID") Long publicationID,
                                      @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                                      @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {
        BufferedImage image = publicationService.getPicture(publicationID);

        BufferedImage scaledImage = ImageUtils.resize(image, width, height,true);
        response.setContentType("image/jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
        encoder.encode(scaledImage);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/publication/attachment/{publicationID}")
    public void getPublicationAttachment(HttpServletResponse response,
                                         @PathVariable("publicationID") Long publicationID) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        Publication publication = publicationService.findById(publicationID);

        String encodedFilename = response.encodeURL(publication.getAttachmentFilename());
        response.setHeader("Content-Disposition:", String.format("attachment;filename=%s\n", encodedFilename));
        publicationService.writeAttachmentContent(publicationID, response.getOutputStream());
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/publication/browse")
    public ModelAndView browsePublication(HttpServletRequest request, @ModelAttribute("publicationSearchForm") PublicationSearchForm form) {
        ModelAndView mav = new ModelAndView("publication-browse");
        Long count = publicationService.countByKeywordAndPublished(form.getKeyword());
        mav.addObject("publicationCount", count);

        int start = pagingUtils.getStartRow(form.getPage());
        mav.addObject("startRow", start);

        List<Publication> publications = publicationService.findByKeywordAndPublished(form.getKeyword(),
                start, pagingUtils.getRowPerPage());
        mav.addObject("publications", publications);

        mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));
        return mav;
    }

}


