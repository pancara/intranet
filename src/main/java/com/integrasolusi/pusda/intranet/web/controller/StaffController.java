package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Staff;
import com.integrasolusi.pusda.intranet.service.StaffService;
import com.integrasolusi.pusda.intranet.utils.ImageUtils;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.utils.PropertyHelper;
import com.integrasolusi.pusda.intranet.web.form.SearchForm;
import com.integrasolusi.pusda.intranet.web.form.StaffForm;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
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
 * Date         : Jan 5, 2011
 * Time         : 4:20:34 PM
 */

@Controller
public class StaffController {
    public static final String STAFF_LIST = "staffList";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PAGE_LIST = "pageList";
    public static final String STAFF_COUNT = "staffCount";
    public static final String FIRST_ROW = "firstRow";

    private StaffService staffService;
    private PagingUtils pagingUtils;

    @Autowired
    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping(value = "staff")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("staff/index");
        return mav;
    }

    @RequestMapping(value = "staff/personal/search", method = RequestMethod.POST)
    public ModelAndView searchStaff(@ModelAttribute("searchForm") SearchForm searchForm) {
        List<Staff> staffList = staffService.findStaffs(searchForm.getKeyword(), 0, pagingUtils.getRowPerPage());
        ModelAndView mav = new ModelAndView("staff/personal/search");
        int firstDataRow = 0;
        int currentPage = 1;
        Long staffCount = staffService.countStaff(searchForm.getKeyword());

        mav.addObject(FIRST_ROW, firstDataRow);
        mav.addObject(CURRENT_PAGE, currentPage);
        mav.addObject(PAGE_LIST, pagingUtils.getPageList(currentPage, staffCount.intValue()));
        mav.addObject(STAFF_LIST, staffList);
        mav.addObject(STAFF_COUNT, staffCount);
        return mav;
    }

    @RequestMapping(value = "staff/personal/search", method = RequestMethod.GET)
    public ModelAndView searchStaff(@ModelAttribute("searchForm") SearchForm searchForm,
                                  @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Long staffCount = staffService.countStaff(searchForm.getKeyword());
        page = Math.min(page, pagingUtils.calcPageCount(staffCount.intValue()));

        int firstDataRow = pagingUtils.getStartRow(page);
        List<Staff> staffList = staffService.findStaffs(searchForm.getKeyword(), firstDataRow, pagingUtils.getRowPerPage());

        ModelAndView mav = new ModelAndView("staff/personal/search");
        mav.addObject(FIRST_ROW, firstDataRow);
        mav.addObject(CURRENT_PAGE, page);
        mav.addObject(STAFF_COUNT, staffCount);
        mav.addObject(PAGE_LIST, pagingUtils.getPageList(page, staffCount.intValue()));
        mav.addObject(STAFF_LIST, staffList);
        return mav;
    }

    @RequestMapping(value = "staff/personal/foto/", method = RequestMethod.GET)
    public void staffPhoto(HttpServletResponse response,
                           @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                           @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {
        staffPhoto(response, null, width, height);
    }

    @RequestMapping(value = "staff/personal/foto/{staffID}", method = RequestMethod.GET)
    public void staffPhoto(HttpServletResponse response,
                           @PathVariable("staffID") Long staffID,
                           @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                           @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {

        BufferedImage image = staffService.createStaffFoto(staffID);
        BufferedImage scaledImage = ImageUtils.resize(image, width, height, true);
        response.setContentType("image/jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
        encoder.encode(scaledImage);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "staff/personal/entry", method = RequestMethod.GET)
    public ModelAndView entryStaff(HttpServletRequest request, @ModelAttribute("staffForm") StaffForm staffForm) {
        return new ModelAndView("staff/personal/entry");
    }

    @RequestMapping(value = "staff/personal/entry", method = RequestMethod.POST)
    public ModelAndView submitNewStaff(@ModelAttribute("staffForm") StaffForm staffForm, Errors errors) throws IOException {
        validateStaff(staffForm, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("staff/personal/entry");
        } else {
            Staff staff = new Staff();
            PropertyHelper.copyProperties(staffForm, staff);
            if (staffForm.getFoto().isEmpty()) {
                staffService.save(staff);
            } else {
                staffService.save(staff, staffForm.getFoto().getInputStream());
            }
            return new ModelAndView("redirect:/staff/personal/search.html");
        }
    }

    @RequestMapping(value = "staff/personal/edit/{staffID}", method = RequestMethod.GET)
    public ModelAndView editStaff(@PathVariable("staffID") Long staffID, @ModelAttribute("staffForm") StaffForm staffForm) throws IOException {
        Staff staff = staffService.findById(staffID);
        PropertyHelper.copyProperties(staff, staffForm);
        return new ModelAndView("staff/personal/edit");
    }

    @RequestMapping(value = "staff/personal/edit/{staffID}", method = RequestMethod.POST)
    public ModelAndView submitEdit(@PathVariable("staffID") Long staffID, @ModelAttribute("staffForm") StaffForm staffForm, Errors errors) throws IOException {
        validateStaff(staffForm, errors);
        if (errors.hasErrors()) {
            return new ModelAndView("staff/personal/edit");
        } else {
            Staff staff = staffService.findById(staffID);
            PropertyHelper.copyProperties(staffForm, staff);
            if (staffForm.getFoto().isEmpty()) {
                staffService.save(staff);
            } else {
                staffService.save(staff, staffForm.getFoto().getInputStream());
            }
            return new ModelAndView("redirect:/staff/personal/list.html");
        }
    }

    @RequestMapping(value = "staff/personal/remove/{staffID}")
    public ModelAndView removeStaff(@PathVariable("staffID") Long staffID,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page)
            throws IOException {
        staffService.removeById(staffID);
        return new ModelAndView("redirect:/staff/personal/list.html?page=" + page);
    }

    private void validateStaff(StaffForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getName()))
            errors.reject("name.empty", "Nama belum diisi.");

        if (StringUtils.isEmpty(form.getNip()))
            errors.reject("nip.empty", "NIP belum diisi.");
    }


}
