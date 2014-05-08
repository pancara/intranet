package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.StaffDao;
import com.integrasolusi.pusda.intranet.persistence.Staff;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 5, 2011
 * Time         : 4:19:19 PM
 */
public class StaffServiceImpl implements StaffService {
    private StaffDao staffDao;
    private ContentRepositoryDao contentRepositoryDao;

    public void setStaffDao(StaffDao staffDao) {
        this.staffDao = staffDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    @Override
    public List<Staff> findAlls(int start, int count) {
        return staffDao.findAlls(start, count);
    }

    @Override
    public List<Staff> findStaffs(String keyword, int start, int count) {
        return staffDao.findByProperty("name", keyword, start, count);
    }

    @Override
    public Long countStaff(String keyword) {
        return staffDao.countByProperty("name", keyword);
    }

    @Override
    public void save(Staff staff) throws IOException {
        save(staff, null);
    }

    @Override
    public void save(Staff staff, java.io.InputStream is) throws IOException {
        staffDao.save(staff);
        if (is != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.STAFF_FOTO, staff.getId());
            contentRepositoryDao.saveBinaryContent(path, is);
        }
    }

    @Override
    public Staff findById(Long id) {
        return staffDao.findById(id);
    }

    @Override
    public void removeById(Long id) {
        staffDao.removeById(id);
        String path = RepositoryUtils.getPath(RepositoryUtils.STAFF_FOTO, id);
        contentRepositoryDao.removeBinaryContent(path);
    }

    @Override
    public BufferedImage createStaffFoto(Long id) throws IOException {
        if (id != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.STAFF_FOTO, id);
            if (contentRepositoryDao.isPathExist(path))
                return contentRepositoryDao.getBufferedImage(path);
        }
        return getDefaultStaffFoto();
    }

    private BufferedImage getDefaultStaffFoto() throws IOException {
        return ImageIO.read(new ClassPathResource("/images/staff-unavailable.jpg").getInputStream());
    }

}
