package org.springframework.context.support;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

/**
 * Programmer   : pancara
 * Date         : Jan 11, 2011
 * Time         : 10:22:17 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/utils.xml"})
public class MessageSourceResourceBundleTest {
    @Autowired
    private ResourceBundleMessageSource resourceBundleMessageSource;

    @Test
    public void format() {
        String message = resourceBundleMessageSource.getMessage("computer", new String[]{"AMD", "Acer"}, "just computer", Locale.getDefault());
        System.out.println(message);
    }
}
