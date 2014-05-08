package com.integrasolusi.pusda.intranet.utils;

import com.integrasolusi.pusda.intranet.persistence.DataStore;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Programmer   : pancara
 * Date         : Feb 6, 2011
 * Time         : 9:13:05 AM
 */
public class DataStoreUtils {
    public static String[] docFileExt = {"doc", "docx"};
    public static String[] pptFileExt = {"ppt", "pptx"};
    public static String[] pdfFileExt = {"pdf", "pdfx"};
    public static String[] spreadSheetFileExt = {"xls", "xlsx"};
    public static String[] pictureFileExt = {"jpg", "jpeg", "gif", "bmp", "png", "tiff"};

    public static boolean isFolder(DataStore dataStore) {
        return DataStore.FOLDER.equals(dataStore.getStorageType());
    }

    public static Integer getDataStoreTypeByFileName(String filename) {
        String ext = FilenameUtils.getExtension(filename);

        if (arrayContains(docFileExt, ext)) {
            return DataStore.DOC;
        } else if (arrayContains(pptFileExt, ext)) {
            return DataStore.PRESENTATION;
        } else if (arrayContains(spreadSheetFileExt, ext)) {
            return DataStore.SPREADSHEET;
        } else if (arrayContains(pdfFileExt, ext)) {
            return DataStore.PDF;
        } else if (arrayContains(pictureFileExt, ext)) {
            return DataStore.PICTURE;
        } else {
            return DataStore.FILE;
        }
    }

    private static boolean arrayContains(String[] array, String ext) {
        for (String s : array)
            if (StringUtils.equalsIgnoreCase(s, ext))
                return true;
        return false;
    }

}
