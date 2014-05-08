package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.Publication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 17, 2011
 * Time         : 9:40:01 AM
 */
public interface PublicationService {

    Long countByKeyword(String keyword);

    Long countByKeywordAndPublished(String keyword);

    List<Publication> findByKeyword(String keyword, int start, int count);

    List<Publication> findByKeywordAndPublished(String keyword, int start, int rowPerPage);

    Publication findById(Long id);

    Publication findNext(Publication publication);

    Publication findPrevious(Publication publication);

    void save(Publication publication) throws IOException;

    void save(Publication publication, InputStream pictureStream, InputStream attachmentStream) throws IOException;

    void saveWithPictureOnly(Publication publication, InputStream is) throws IOException;

    void saveWithAttachmentOnly(Publication publication, InputStream is) throws IOException;

    void removeById(Long id);

    List<Publication> findLatest(int count);

    BufferedImage getPicture(Long publicationID) throws IOException;

    void writeAttachmentContent(Long id, OutputStream outputStream);

}
