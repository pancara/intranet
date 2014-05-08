package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Gallery;
import com.integrasolusi.pusda.intranet.persistence.GalleryPicture;
import com.integrasolusi.pusda.intranet.service.GalleryPictureService;
import com.integrasolusi.pusda.intranet.service.GalleryService;
import com.integrasolusi.pusda.intranet.utils.ImageUtils;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.utils.PropertyHelper;
import com.integrasolusi.pusda.intranet.web.form.GalleryForm;
import com.integrasolusi.pusda.intranet.web.form.GalleryPictureForm;
import com.integrasolusi.pusda.intranet.web.form.SearchForm;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 7, 2011
 * Time         : 9:19:36 PM
 */

@Controller
public class GalleryController {
    public static final String GALLERY_LIST = "galleryList";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PAGE_LIST = "pageList";
    public static final String GALLERY_COUNT = "galleryCount";
    public static final String FIRST_ROW = "firstRow";

    private GalleryService galleryService;
    private GalleryPictureService galleryPictureService;
    private PagingUtils pagingUtils;
    private static final int ARRAY_COL = 8;

    @Autowired
    public void setGalleryService(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @Autowired
    public void setGalleryPictureService(GalleryPictureService galleryPictureService) {
        this.galleryPictureService = galleryPictureService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping("/gallery/list")
    public ModelAndView listGallery(@ModelAttribute("searchForm") SearchForm searchForm,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Long galleryCount = galleryService.countByKeyword(searchForm.getKeyword());
        page = Math.min(page, pagingUtils.calcPageCount(galleryCount.intValue()));

        int firstRow = pagingUtils.getStartRow(page);
        List<Gallery> galleryList = galleryService.findByKeyword(searchForm.getKeyword(), firstRow, pagingUtils.getRowPerPage());

        ModelAndView mav = new ModelAndView("gallery-list");
        mav.addObject(FIRST_ROW, firstRow);
        mav.addObject(CURRENT_PAGE, page);
        mav.addObject(GALLERY_COUNT, galleryCount);
        mav.addObject(PAGE_LIST, pagingUtils.getPageList(page, galleryCount.intValue()));
        mav.addObject(GALLERY_LIST, galleryList);
        return mav;
    }

    @RequestMapping(value = "gallery/picture/", method = RequestMethod.GET)
    public void galleryTitlePicture(HttpServletResponse response,
                                    @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                                    @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {
        getGalleryTitlePicture(response, null, width, height);
    }

    @RequestMapping(value = "gallery/title/{galleryID}", method = RequestMethod.GET)
    public void getGalleryTitlePicture(HttpServletResponse response,
                                       @PathVariable("galleryID") Long galleryID,
                                       @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                                       @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {

        BufferedImage image = galleryService.createGalleryFoto(galleryID);
        BufferedImage scaledImage = ImageUtils.resize(image, width, height, true);
        response.setContentType("image/jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
        encoder.encode(scaledImage);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "gallery/entry", method = RequestMethod.GET)
    public ModelAndView entryGallery(HttpServletRequest request, @ModelAttribute("galleryForm") GalleryForm form) {
        return new ModelAndView("gallery-entry");
    }

    @RequestMapping(value = "gallery/entry", method = RequestMethod.POST)
    public ModelAndView submitNewGallery(@ModelAttribute("galleryForm") GalleryForm form, Errors errors) throws IOException {
        validateGallery(form, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("gallery-entry");
        } else {
            saveGallery(form, new Gallery());
            return new ModelAndView("redirect:/gallery/list.html");
        }
    }

    @RequestMapping(value = "gallery/edit/{galleryID}", method = RequestMethod.GET)
    public ModelAndView editGallery(@PathVariable("galleryID") Long galleryID, @ModelAttribute("galleryForm") GalleryForm form) throws IOException {
        Gallery gallery = galleryService.findById(galleryID);
        PropertyHelper.copyProperties(gallery, form);
        return new ModelAndView("gallery-edit");
    }

    @RequestMapping(value = "gallery/edit/{galleryID}", method = RequestMethod.POST)
    public ModelAndView submitEdit(@PathVariable("galleryID") Long galleryID, @ModelAttribute("galleryForm") GalleryForm form, Errors errors) throws IOException {
        validateGallery(form, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("gallery-edit");
        } else {
            Gallery gallery = galleryService.findById(galleryID);
            saveGallery(form, gallery);
            return new ModelAndView("redirect:/gallery/list.html");
        }
    }

    private void saveGallery(GalleryForm form, Gallery gallery) throws IOException {
        PropertyHelper.copyProperties(form, gallery);
        if (form.getPicture().isEmpty())
            galleryService.save(gallery);
        else
            galleryService.save(gallery, form.getPicture().getInputStream());
    }

    @RequestMapping(value = "gallery/remove/{galleryID}")
    public ModelAndView removeGallery(@PathVariable("galleryID") Long galleryID, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws IOException {
        galleryService.removeById(galleryID);

        ModelAndView mav = new ModelAndView("redirect:/gallery/list.html?page=" + page);
        return mav;
    }

    private void validateGallery(GalleryForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getTitle()))
            errors.reject("title.empty", "Judul belum diisi.");

        if (StringUtils.isEmpty(form.getDescription()))
            errors.reject("description.empty", "Keterangan belum diisi.");

    }

    @RequestMapping(value = "gallery/picture/list/{galleryID}")
    public ModelAndView listGalleryPicture(@PathVariable("galleryID") Long galleryID, @ModelAttribute("galleryPictureForm") GalleryPictureForm form) throws IOException {
        ModelAndView mav = new ModelAndView("gallery-picture-list");

        Gallery gallery = galleryService.findById(galleryID);
        mav.addObject("gallery", gallery);

        List<GalleryPicture> pictureList = galleryPictureService.findByGallery(gallery);
        mav.addObject("pictureList", pictureList);
        return mav;
    }


    @RequestMapping(value = "gallery/picture/{galleryPictureID}")
    public void getGalleryPicture(HttpServletResponse response,
                                  @PathVariable("galleryPictureID") Long galleryPictureID,
                                  @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                                  @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {
        BufferedImage image = galleryPictureService.getPicture(galleryPictureID);

        BufferedImage scaledImage = ImageUtils.resize(image, width, height, true);
        response.setContentType("image/jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
        encoder.encode(scaledImage);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "ajax/gallery/picture/upload/{galleryID}", method = RequestMethod.POST)
    public ModelAndView ajaxUploadPicture(@PathVariable("galleryID") Long galleryID, @ModelAttribute("galleryPictureForm") GalleryPictureForm form, Errors errors) throws Exception {
        validateGalleryPicture(form, errors, true);
        ModelAndView mav = new ModelAndView("ajax/gallery-picture-upload");
        if (!errors.hasErrors()) {
            GalleryPicture picture = new GalleryPicture();
            saveGalleryPicture(form, picture, galleryID);
            mav.addObject("picture", picture);
        }
        return mav;
    }

    private void saveGalleryPicture(GalleryPictureForm form, GalleryPicture galleryPicture) throws IOException {
        saveGalleryPicture(form, galleryPicture, null);
    }

    private void saveGalleryPicture(GalleryPictureForm form, GalleryPicture galleryPicture, Long galleryID) throws IOException {
        BeanUtils.copyProperties(form, galleryPicture, new String[]{"id"});
        if (galleryID != null)
            galleryPicture.setGallery(galleryService.findById(galleryID));

        if (form.getPicture().isEmpty())
            galleryPictureService.save(galleryPicture, null);
        else {
            galleryPictureService.save(galleryPicture, form.getPicture().getInputStream());
        }
    }

    @RequestMapping(value = "gallery/picture/edit/{galleryPictureID}", method = RequestMethod.GET)
    public ModelAndView editGalleryPicture(@PathVariable("galleryPictureID") Long galleryPictureID, @ModelAttribute("galleryPictureForm") GalleryPictureForm form) throws IOException {
        GalleryPicture picture = galleryPictureService.findById(galleryPictureID);

        PropertyHelper.copyProperties(picture, form);

        return new ModelAndView("gallery-picture-edit");
    }

    @RequestMapping(value = "gallery/picture/edit/{galleryPictureID}", method = RequestMethod.POST)
    public ModelAndView submitPictureEdit(@PathVariable("galleryPictureID") Long galleryPictureID, @ModelAttribute("galleryPictureForm") GalleryPictureForm form, Errors errors) throws IOException {
        validateGalleryPicture(form, errors, false);
        if (errors.hasErrors()) {
            return new ModelAndView("gallery-picture-edit");
        } else {
            GalleryPicture picture = galleryPictureService.findById(galleryPictureID);
            saveGalleryPicture(form, picture);

            return new ModelAndView(String.format("redirect:/gallery/picture/list/%d.html", picture.getGallery().getId()));
        }
    }

    @RequestMapping(value = "gallery/picture/remove/{galleryPictureID}")
    public ModelAndView removeGalleryPicture(@PathVariable("galleryPictureID") Long galleryPictureID) throws IOException {
        GalleryPicture picture = galleryPictureService.findById(galleryPictureID);
        Gallery gallery = picture.getGallery();
        galleryPictureService.remove(picture);

        return new ModelAndView(String.format("redirect:/gallery/picture/list/%d.html", gallery.getId()));
    }

    private void validateGalleryPicture(GalleryPictureForm form, Errors errors, boolean newData) {
        if (StringUtils.isEmpty(form.getTitle()))
            errors.reject("title.empty", "Judul belum diisi.");

        if (StringUtils.isEmpty(form.getDescription()))
            errors.reject("description.empty", "Keterangan belum diisi.");

        if (newData) {
            if (form.getPicture().isEmpty())
                errors.reject("file.empty", "Tentukan file yang diupload.");
        }
    }

}
