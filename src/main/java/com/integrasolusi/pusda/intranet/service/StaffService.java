package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Staff;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 10:19:44 AM
 */
public interface StaffService {
    List<Staff> findAlls(int start, int count);

    List<Staff> findStaffs(String keyword, int start, int count);

    Long countStaff(String keyword);

    void save(Staff staff) throws IOException;

    void save(Staff staff, java.io.InputStream is) throws IOException;

    Staff findById(Long id);

    void removeById(Long id);

    BufferedImage createStaffFoto(Long id) throws IOException;

}