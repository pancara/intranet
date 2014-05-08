package com.integrasolusi.pusda.intranet.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 4:37:57 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/utils.xml"})
public class CaptchaGeneratorTest {
    @Autowired
    private CaptchaGenerator captchaGenerator;

    @Test
    public void generateCaptcha() throws IOException {
        FileOutputStream os = new FileOutputStream(new File("E:/test/pusda-intranet/captcha.jpg"));
        captchaGenerator.generateCaptchaImage("1234", os);
        os.close();
    }
}
