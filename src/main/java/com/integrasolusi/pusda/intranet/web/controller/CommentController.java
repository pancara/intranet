package com.integrasolusi.pusda.intranet.web.controller;


import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Comment;
import com.integrasolusi.pusda.intranet.persistence.Publication;
import com.integrasolusi.pusda.intranet.service.CommentService;
import com.integrasolusi.pusda.intranet.utils.CaptchaGenerator;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.utils.StringHelper;
import com.integrasolusi.pusda.intranet.web.form.CommentForm;
import com.integrasolusi.pusda.intranet.web.form.CommentSearchForm;
import com.integrasolusi.pusda.intranet.web.form.PublicationSearchForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 22, 2011
 * Time         : 4:45:51 PM
 */

@Controller
public class CommentController {
    public final static String CAPTCHA_COMMENT_KEY = "captcha_comment";
    private CommentService commentService;
    private CaptchaGenerator captchaGenerator;
    private PagingUtils pagingUtils;

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setCaptchaGenerator(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.GET)
    public ModelAndView viewComment(HttpServletRequest request, @ModelAttribute("commentForm") CommentForm form,
                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        generateCaptchaText(request);
        ModelAndView mav = new ModelAndView("comment");
        populateComments(mav, page);
        return mav;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ModelAndView submitComment(HttpServletRequest request, @ModelAttribute("commentForm") CommentForm form, Errors errors)
            throws InvocationTargetException, IllegalAccessException {
        if (validateComment(form, errors, request)) {
            Comment comment = new Comment();
            BeanUtils.copyProperties(comment, form);
            comment.setPostDate(new Date());
            commentService.save(comment);
            ModelAndView mav = new ModelAndView("redirect:comment.html");
            populateComments(mav, null);
            return mav;
        } else {
            generateCaptchaText(request);
            ModelAndView mav = new ModelAndView("comment");
            populateComments(mav, null);
            return mav;
        }
    }

    private void populateComments(ModelAndView mav, Integer page) {
        Long count = commentService.countAlls();
        int accessedPage = 1;
        if (page != null)
            accessedPage = Math.min(page, pagingUtils.calcPageCount(count.intValue()));
        mav.addObject("page", accessedPage);

        int start = pagingUtils.getStartRow(accessedPage, pagingUtils.getRowPerPage());
        mav.addObject("start", start);

        List<Comment> commentList = commentService.findAllsOrderBy("postDate", OrderDir.DESC, start, pagingUtils.getRowPerPage());
        mav.addObject("commentList", commentList);

        mav.addObject("pageList", pagingUtils.getPageList(accessedPage, count.intValue()));
    }

    private boolean validateComment(CommentForm form, Errors errors, HttpServletRequest request) {

        if (StringUtils.isEmpty(form.getName()))
            errors.reject("name.empty", "Nama belum diisi");

        if (StringUtils.isEmpty(form.getEmail()))
            errors.reject("email.empty", "Email belum diisi");

        if (StringUtils.isEmpty(form.getMessage()))
            errors.reject("message.empty", "Pesan belum diisi");

        if (StringUtils.isEmpty(form.getCaptcha())) {
            errors.reject("captcha.empty", "Isikan teks captcha yang ditampilkan");
        } else {
            String captchaText = (String) HttpSessionUtils.getSessionAttribute(request, CommentController.CAPTCHA_COMMENT_KEY);
            if (!StringUtils.equalsIgnoreCase(form.getCaptcha(), captchaText))
                errors.reject("captcha.not.valid", "Isikan teks captcha dengan benar");
        }

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/comment/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = (String) HttpSessionUtils.getSessionAttribute(request, CommentController.CAPTCHA_COMMENT_KEY);
        response.setContentType("image/jpg");
        captchaGenerator.generateCaptchaImage(captchaText, response.getOutputStream());
    }

    private String generateCaptchaText(HttpServletRequest request) {
        String captchaText = StringHelper.generateRandomText(4);
        HttpSessionUtils.setSessionAttribute(request, CommentController.CAPTCHA_COMMENT_KEY, captchaText);
        return captchaText;
    }

    @RequestMapping(value = "/comment/browse")
    public ModelAndView browseComments(HttpServletRequest request, @ModelAttribute("commentSearchForm") CommentSearchForm form) {
        ModelAndView mav = new ModelAndView("comment-browse");
        Long count = commentService.countByKeyword(form.getKeyword());
        mav.addObject("commentCount", count);

        int start = pagingUtils.getStartRow(form.getPage());
        mav.addObject("startRow", start);

        List<Comment> comments = commentService.findByKeyword(form.getKeyword(), start, pagingUtils.getRowPerPage());
        mav.addObject("comments", comments);

        mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));
        return mav;
    }

    @RequestMapping(value = "/comment/delete")
    public ModelAndView doAction(HttpServletRequest request, @ModelAttribute("commentSearchForm") CommentSearchForm form) throws ServletRequestBindingException, IOException {
        long[] ids = ServletRequestUtils.getLongParameters(request, "id");
        if (ids != null) {
            doRemove(ids);
        }

        return new ModelAndView(String.format("redirect:/comment/browse.html?page=%d&keyword=%s", form.getPage(), form.getKeyword()));
    }

    private void doRemove(long[] ids) throws IOException {
        for (Long id : ids) {
            commentService.removeById(id);
        }
    }
}
