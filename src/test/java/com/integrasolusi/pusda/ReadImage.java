package com.integrasolusi.pusda;

import com.integrasolusi.pusda.intranet.utils.StreamHelper;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * Programmer   : pancara
 * Date         : Jan 6, 2011
 * Time         : 8:54:04 PM
 */
public class ReadImage {

    @Test
    public void readImageInClassPath() throws IOException {
        ClassPathResource resource = new ClassPathResource("/images/staff-unavailable.jpg");
        InputStream is = resource.getInputStream();
        OutputStream os = new FileOutputStream("E:/pusda/data/copy_from_resource.jpg");
        StreamHelper.getInstance().copy(is, os);
        is.close();
        os.close();
    }
}
