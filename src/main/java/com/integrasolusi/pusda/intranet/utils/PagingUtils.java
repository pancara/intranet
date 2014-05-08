package com.integrasolusi.pusda.intranet.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 5, 2011
 * Time         : 5:46:37 PM
 */
public class PagingUtils {
    private int rowPerPage = 10;
    private int displayedPageCount = 8;

    public int getRowPerPage() {
        return this.rowPerPage;
    }

    public void setRowPerPage(int value) {
        this.rowPerPage = value;
    }

    public int getDisplayedPageCount() {
        return displayedPageCount;
    }

    public void setDisplayedPageCount(int displayedPageCount) {
        this.displayedPageCount = displayedPageCount;
    }

    public int calcPageCount(int count) {
        return (int) Math.ceil((double) count / (double) this.rowPerPage);
    }

    public int calcPageCount(int count, int rowPerPage) {
        return (int) Math.ceil((double) count / (double) rowPerPage);
    }

    public int getStartRow(int page, int rowPerPage) {
        return (page - 1) * rowPerPage;
    }

    public int getStartRow(int page) {
        return (page - 1) * this.rowPerPage;
    }

    public List<Integer> getPageList(int current, int pageCount, int dataCount, int dataPerPage) {
        int totalPage = calcPageCount(dataCount, dataPerPage);

        List<Integer> pages = new LinkedList<Integer>();
        int start = (int) (current - (double) pageCount / 2);
        int end = start + pageCount - 1;
        if (start < 1) {
            end = end - start;
            start = 1;
        }
        if (end > totalPage)
            end = totalPage;

        for (int i = start; i <= end; i++)
            pages.add(i);
        return pages;
    }


    public List<Integer> getPageList(int currentPage, int rowCount) {
        return getPageList(currentPage,
                displayedPageCount,
                rowCount,
                rowPerPage);
    }

}
