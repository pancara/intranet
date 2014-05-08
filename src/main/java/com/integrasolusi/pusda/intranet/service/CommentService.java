package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Comment;
import com.integrasolusi.pusda.intranet.persistence.Publication;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 22, 2011
 * Time         : 4:54:42 PM
 */
public interface CommentService {
    void save(Comment comment);



    List<Comment> findAlls();

    Long countAlls();

    List<Comment> findAllsOrderBy(String orderProperty, OrderDir orderDir, int start, int count);

    Long countByKeyword(String keyword);

    List<Comment> findByKeyword(String keyword, int start, int count);

    void removeById(Long id);
}
