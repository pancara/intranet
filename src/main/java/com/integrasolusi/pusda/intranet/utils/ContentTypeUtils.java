package com.integrasolusi.pusda.intranet.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/**
 * Programmer   : pancara
 * Date         : 3/23/11
 * Time         : 12:41 AM
 */
public class ContentTypeUtils {
    private static final String DEFAULT_EXT = "bin";

    private static Logger logger = Logger.getLogger(ContentTypeUtils.class);
    private static Properties contentTypes = null;
    private static ClassPathResource resource = new ClassPathResource("com/integrasolusi/pusda/intranet/utils/content_type.properties");

    public static String getTypeByFilenameExt(String extension) {

        if (contentTypes == null)
            contentTypes = loadContentTypeProperties();

        if (extension == null)
            extension = DEFAULT_EXT;
        return (String) contentTypes.get(extension);
    }

    public static String encodeFilename(String filename) {
        return StringUtils.replace(filename, " ", "%20");
    }

    private static Properties loadContentTypeProperties() {
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
        } catch (Exception e) {
            logger.info("load content type resource failed", e);
        }
        return properties;
    }
}
