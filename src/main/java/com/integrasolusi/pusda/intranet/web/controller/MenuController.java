package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.DataStore;
import com.integrasolusi.pusda.intranet.service.DataStoreService;
import com.integrasolusi.pusda.intranet.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 6, 2011
 * Time         : 11:33:33 AM
 */
@Controller
public class MenuController {
    private PersonService personService;
    private DataStoreService dataStoreService;

    @Autowired
    public void setDataStoreService(DataStoreService dataStoreService) {
        this.dataStoreService = dataStoreService;
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping(value = "menu")
    public ModelAndView getMenu(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("menu");
        List<DataStore> dataStores = dataStoreService.findByDataStoreForMenu();
        mav.addObject("dataStores", dataStores);
        return mav;
    }
}
