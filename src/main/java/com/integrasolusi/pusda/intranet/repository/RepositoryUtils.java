package com.integrasolusi.pusda.intranet.repository;

/**
 * Programmer   : pancara
 * Date         : Jan 6, 2011
 * Time         : 5:42:46 PM
 */
public class RepositoryUtils {
    public static final String AVATAR = "avatar";

    public static final String STAFF_FOTO = "staff/foto";
    public static final String GALLERY = "gallery";
    public static final String GALLERY_PICTURE = "gallery_picture";
    public static final String DOCUMENT = "document";

    public static final String SITE = "site_picture";

    public static final String DATA_STORE = "data_store";

    public static final String PUBLICATION_PICTURE = "publication_picture";

    public static final String PUBLICATION_ATTACHMENT = "publication_attachment";

    public static final String NEWS_PICTURE = "news_picture";

    public static final String SLIDE_PICTURE = "slide_picture";


    public static String getPath(String folder, Long id) {
        return String.format("/%s/%d", folder, id);
    }


}
