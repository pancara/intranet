package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Slide;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 27, 2011
 * Time         : 4:36:27 PM
 */
public interface SlideService {
    List<Slide> findByType(Integer type);

    Slide findById(Long slideID);

    void writeContent(Long slideID, OutputStream os);

    void save(Slide slide) throws IOException;

    void save(Slide slide, InputStream is) throws IOException;

    void remove(Slide slide);
}
