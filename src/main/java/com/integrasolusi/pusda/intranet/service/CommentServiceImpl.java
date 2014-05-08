package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.CommentDao;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.Comment;

import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 22, 2011
 * Time         : 4:54:53 PM
 */
public class CommentServiceImpl implements CommentService {
    private CommentDao commentDao;

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public void save(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public List<Comment> findAlls() {
        return commentDao.findAlls();
    }

    @Override
    public Long countAlls() {
        return commentDao.countAlls();
    }

    @Override
    public List<Comment> findAllsOrderBy(String orderProperty, OrderDir orderDir, int start, int count) {
        return commentDao.findAllsOrderBy(orderProperty, orderDir, start, count);
    }

    @Override
    public Long countByKeyword(String keyword) {
        String paramValue = "%" + keyword + "%";
        Filter filter = new ValueFilter("message", QueryOperator.LIKE, paramValue, "keyword");
        return commentDao.countByFilter(filter);
    }

    @Override
    public List<Comment> findByKeyword(String keyword, int start, int count) {
        String paramValue = "%" + keyword + "%";
        Filter filter = new ValueFilter("message", QueryOperator.LIKE, paramValue, "keyword");
        return commentDao.findByFilterOrderBy(filter, "id", OrderDir.DESC, start, count);
    }

    @Override
    public void removeById(Long id) {
        commentDao.removeById(id);
    }
}
