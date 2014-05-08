package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.News;
import com.integrasolusi.pusda.intranet.service.NewsService;
import com.integrasolusi.pusda.intranet.utils.ImageUtils;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.web.form.NewsForm;
import com.integrasolusi.pusda.intranet.web.form.NewsSearchForm;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NewsController {
    private static final String ACTION_PUBLISH = "publish";
    private static final String ACTION_REMOVE = "remove";

    private NewsService newsService;
    private PagingUtils pagingUtils;

    @Autowired
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping("/news/list")
    public ModelAndView listNews(HttpServletRequest request, @ModelAttribute("newsSearchForm") NewsSearchForm form) {
        ModelAndView mav = new ModelAndView("news-list");
        Long count = newsService.countByKeyword(form.getKeyword());
        mav.addObject("newsCount", count);

        int start = pagingUtils.getStartRow(form.getPage());
        mav.addObject("startRow", start);

        List<News> newses = newsService.findByKeyword(form.getKeyword(),
                start, pagingUtils.getRowPerPage());
        mav.addObject("newses", newses);

        mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));
        return mav;
    }

    @RequestMapping(value = "/news/action")
    public ModelAndView doAction(HttpServletRequest request, @ModelAttribute("newsSearchForm") NewsSearchForm form) throws ServletRequestBindingException, IOException {
        long[] ids = ServletRequestUtils.getLongParameters(request, "id");
        if (ids != null) {
            String action = ServletRequestUtils.getStringParameter(request, "action");
            if (StringUtils.equals(ACTION_PUBLISH, action))
                doPublish(ids);
            else if (StringUtils.equals(ACTION_REMOVE, action))
                doRemove(ids);
        }

        return new ModelAndView("redirect:/news/list.html");
    }

    private void doPublish(long[] ids) throws IOException {
        for (Long id : ids) {
            News news = newsService.findById(id);
            news.setPublished(true);
            newsService.save(news);
        }
    }

    private void doRemove(long[] ids) {
        for (Long id : ids)
            newsService.removeById(id);
    }

    @RequestMapping(value = "/news/entry", method = RequestMethod.GET)
    public ModelAndView entryNews(HttpServletRequest request, @ModelAttribute("newsForm") NewsForm form) {
        return new ModelAndView("news-entry");
    }

    @RequestMapping(value = "/news/entry", method = RequestMethod.POST)
    public ModelAndView entryNews(HttpServletRequest request, @ModelAttribute("newsForm") NewsForm form, Errors errors) throws IOException {
        if (validateNews(form, errors)) {
            News news = new News();
            news.setTitle(form.getTitle());
            news.setContent(form.getText());
            news.setPublished(form.getPublished());
            if (form.getPicture().isEmpty()) {
                newsService.save(news);
            } else {
                newsService.save(news, form.getPicture().getInputStream());
            }
            return new ModelAndView("redirect:/news/list.html");
        }
        return new ModelAndView("news-entry");
    }

    @RequestMapping(value = "/news/edit/{newsID}", method = RequestMethod.GET)
    public ModelAndView editNews(HttpServletRequest request, @PathVariable("newsID") Long newsID,
                                 @ModelAttribute("newsForm") NewsForm form) {
        ModelAndView mav = new ModelAndView("news-edit");
        News news = newsService.findById(newsID);
        mav.addObject("news", news);

        form.setTitle(news.getTitle());
        form.setText(news.getContent());
        form.setPublished(news.getPublished());
        return mav;
    }

    @RequestMapping(value = "/news/edit/{newsID}", method = RequestMethod.POST)
    public ModelAndView editNews(HttpServletRequest request, @PathVariable("newsID") Long newsID,
                                 @ModelAttribute("newsForm") NewsForm form, Errors errors) throws IOException {
        if (validateNews(form, errors)) {
            News news = newsService.findById(newsID);
            news.setTitle(form.getTitle());
            news.setContent(form.getText());
            news.setPublished(form.getPublished());
            if (form.getPicture() == null || form.getPicture().isEmpty()) {
                newsService.save(news);
            } else {
                newsService.save(news, form.getPicture().getInputStream());
            }
            return new ModelAndView("redirect:/news/list.html");
        }
        ModelAndView mav = new ModelAndView("news-edit");
        News news = newsService.findById(newsID);
        mav.addObject("news", news);
        return mav;
    }

    private boolean validateNews(NewsForm form, Errors errors) {
        if (StringUtils.isEmpty(form.getTitle()))
            errors.reject("title.empty", "Judul belum diisi");

        if (StringUtils.isEmpty(form.getText()))
            errors.reject("content.empty", "Content berita belum diisi");

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/news/read/{newsID}", method = RequestMethod.GET)
    public ModelAndView readNews(HttpServletRequest request, @PathVariable("newsID") Long newsID) {
        ModelAndView mav = new ModelAndView("news-read");
        News news = newsService.findById(newsID);
        mav.addObject("news", news);

        News next = newsService.findNext(news);
        mav.addObject("next", next);

        News previous = newsService.findPrevious(news);
        mav.addObject("previous", previous);
        return mav;
    }

    @RequestMapping(value = "/news/picture/{newsID}")
    public void getNewsPicture(HttpServletResponse response,
                               @PathVariable("newsID") Long newsID,
                               @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                               @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {
        BufferedImage image = newsService.getPicture(newsID);

        BufferedImage scaledImage = ImageUtils.resize(image, width, height,true);
        response.setContentType("image/jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
        encoder.encode(scaledImage);
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/news/browse")
    public ModelAndView browseNews(HttpServletRequest request, @ModelAttribute("newsSearchForm") NewsSearchForm form) {
        ModelAndView mav = new ModelAndView("news-browse");
        Long count = newsService.countByKeywordAndPublished(form.getKeyword());
        mav.addObject("newsCount", count);

        int start = pagingUtils.getStartRow(form.getPage());
        mav.addObject("startRow", start);

        List<News> newses = newsService.findByKeywordAndPublished(form.getKeyword(),
                start, pagingUtils.getRowPerPage());
        mav.addObject("newses", newses);

        mav.addObject("pages", pagingUtils.getPageList(form.getPage(), count.intValue()));
        return mav;

    }
}