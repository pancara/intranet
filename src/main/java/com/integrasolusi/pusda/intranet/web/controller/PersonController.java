package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.json.PropertyFilterSerializerFactory;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.PersonService;
import com.integrasolusi.pusda.intranet.utils.ImageUtils;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 8:45:06 AM
 */
@Controller
public class PersonController {
    private PersonService personService;
    private PagingUtils pagingUtils;

    @Autowired
    public void setUserService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping("/ajax/person/select/share/{documentID}")
    public ModelAndView searchPerson(@PathVariable("documentID") Long documentID,
                                     @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Long personCount = personService.countForShare(keyword, documentID);
        page = pagingUtils.calcPageCount(personCount.intValue());

        int firstRow = pagingUtils.getStartRow(page);

//        Integer count = ApplicationConfig.getInstance().getDataPerPage();
        Integer count = 500;
        List<Person> personList = personService.findForShare(keyword, documentID, firstRow, count);

        ModelAndView mav = new ModelAndView("ajax/person-select");
        mav.addObject("firstRow", firstRow);
        mav.addObject("currentPage", page);
        mav.addObject("personCount", personCount);
        mav.addObject("pageList", pagingUtils.getPageList(page, personCount.intValue()));
        mav.addObject("personList", personList);
        return mav;
    }

    @RequestMapping("/json/person/search")
    public MappingJacksonJsonView searchPerson(HttpServletRequest request,
                                               @RequestParam(value = "q", required = false, defaultValue = "") String keyword,
                                               @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
                                               @RequestParam(value = "s", required = false, defaultValue = "10") Integer pageSize) {
        MappingJacksonJsonView view = new MappingJacksonJsonView();
        view.setObjectMapper(createPersonObjectMapper());

        Filter filter = new ValueFilter("name", QueryOperator.LIKE, "%" + keyword + "%", "name");
        Long total = personService.countByFilter(filter);
        view.addStaticAttribute("total", total);
        int start = pagingUtils.getStartRow(page, pageSize);
        view.addStaticAttribute("results", personService.findByFilter(filter, start, pageSize));
        return view;
    }

    private ObjectMapper createPersonObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(new PropertyFilterSerializerFactory(Person.class, new String[]{"id", "name", "email", "address", "phone"}));
        mapper.getSerializationConfig().setSerializationView(Person.class);
        return mapper;
    }

    @RequestMapping("/person/avatar/{personID}")
    public void avatarPerson(HttpServletResponse response,
                             @PathVariable("personID") Long personID,
                             @RequestParam(value = "width", required = false, defaultValue = "-1") Integer width,
                             @RequestParam(value = "height", required = false, defaultValue = "-1") Integer height) throws IOException {

        BufferedImage image = personService.createAvatar(personID);
        BufferedImage scaledImage = ImageUtils.resize(image, width, height,true);
        response.setContentType("image/jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
        encoder.encode(scaledImage);
        response.getOutputStream().flush();
    }

    @RequestMapping("/ajax/person/{personID}")
    public ModelAndView getPerson(@PathVariable("personID") Long personID) {
        ModelAndView mav = new ModelAndView("ajax/person");
        Person person = personService.findById(personID);
        mav.addObject("person", person);
        return mav;
    }


}
