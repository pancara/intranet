package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.dao.DataStoreDao;
import com.integrasolusi.pusda.intranet.dao.filter.CompositeFilter;
import com.integrasolusi.pusda.intranet.dao.filter.Filter;
import com.integrasolusi.pusda.intranet.dao.filter.QueryOperator;
import com.integrasolusi.pusda.intranet.dao.filter.ValueFilter;
import com.integrasolusi.pusda.intranet.dao.generic.OrderDir;
import com.integrasolusi.pusda.intranet.persistence.DataStore;
import com.integrasolusi.pusda.intranet.repository.RepositoryUtils;
import com.integrasolusi.pusda.intranet.repository.jackrabbit.ContentRepositoryDao;
import com.integrasolusi.pusda.intranet.utils.DataStoreUtils;
import com.integrasolusi.pusda.intranet.web.utils.StreamBufferUtils;

import java.io.*;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 3, 2011
 * Time         : 5:48:44 PM
 */
public class DataStoreServiceImpl implements DataStoreService {
    private Integer temporaryIndex = 0;
    private DataStoreDao dataStoreDao;
    private ContentRepositoryDao contentRepositoryDao;
    private StreamBufferUtils streamBufferUtils;
    private Long rootID;

    public void setDataStoreDao(DataStoreDao dataStoreDao) {
        this.dataStoreDao = dataStoreDao;
    }

    public void setContentRepositoryDao(ContentRepositoryDao contentRepositoryDao) {
        this.contentRepositoryDao = contentRepositoryDao;
    }

    public Long getRootID() {
        return rootID;
    }

    public void setRootID(Long rootID) {
        this.rootID = rootID;
    }

    @Override
    public List<DataStore> findByParent(Long id) {
        DataStore parent = dataStoreDao.loadByID(id);
        Filter filter = new ValueFilter("parent", QueryOperator.EQUALS, parent, "parent");
        return dataStoreDao.findByFilterOrderBy(filter, "index", OrderDir.ASC);
    }

    @Override
    public DataStore getRoot() {
        Filter filter = new ValueFilter("parent", QueryOperator.IS, null, "parent");
        return dataStoreDao.findUniqueByFilter(filter);
    }

    @Override
    public DataStore findById(Long id) {
        return dataStoreDao.findById(id);
    }

    @Override
    public void save(DataStore dataStore) {
        dataStoreDao.save(dataStore);
    }

    @Override
    public void save(DataStore dataStore, InputStream inputStream) throws IOException {
        dataStoreDao.save(dataStore);
        if (inputStream != null) {
            String path = RepositoryUtils.getPath(RepositoryUtils.DATA_STORE, dataStore.getId());
            contentRepositoryDao.saveBinaryContent(path, inputStream);
        }
    }

    @Override
    public void remove(DataStore dataStore) {
        dataStoreDao.remove(dataStore);
        if (!DataStoreUtils.isFolder(dataStore)) {
            String path = RepositoryUtils.getPath(RepositoryUtils.DATA_STORE, dataStore.getId());
            contentRepositoryDao.removeBinaryContent(path);
        }
    }

    @Override
    public void normalizeChildIndex(DataStore parent) {
        Filter filter = new ValueFilter("parent", QueryOperator.EQUALS, parent, "parent");
        List<DataStore> children = dataStoreDao.findByFilterOrderBy(filter, "index", OrderDir.ASC);
        int index = 1;
        for (DataStore child : children) {
            child.setIndex(index);
            dataStoreDao.save(child);
            index++;
        }
    }

    private void swapOrder(DataStore dataStore_1, DataStore dataStore_2) {
        int temp = dataStore_1.getIndex();
        dataStore_1.setIndex(dataStore_2.getIndex());
        dataStore_2.setIndex(temp);

        dataStoreDao.save(dataStore_1);
        dataStoreDao.save(dataStore_2);
    }

    @Override
    public void moveUp(DataStore dataStore) {
        DataStore before = dataStoreDao.findBefore(dataStore);
        if (before != null)
            swapOrder(dataStore, before);
    }

    @Override
    public void moveDown(DataStore dataStore) {
        DataStore after = dataStoreDao.findAfter(dataStore);
        if (after != null)
            swapOrder(dataStore, after);
    }

    @Override
    public List<DataStore> findAllsFolders(String query) {
        CompositeFilter filter = new CompositeFilter(QueryOperator.AND);
        filter.add(new ValueFilter("storageType", QueryOperator.EQUALS, DataStore.FOLDER, "storageType"));
        filter.add(new ValueFilter("name", QueryOperator.LIKE, "%" + query + "%", "name"));

        return dataStoreDao.findByFilterOrderBy(filter, "name", OrderDir.ASC);
    }

    @Override
    public void copyContentToStream(Long id, OutputStream outputStream) throws Exception {
        OutputStream outBuffer = null;
        InputStream inBuffer = null;
        try {
            String path = RepositoryUtils.getPath(RepositoryUtils.DATA_STORE, id);
            contentRepositoryDao.copyBinaryContent(path, outputStream);
            outputStream.flush();
        } finally {
            if (outBuffer != null)
                outBuffer.close();

            if (inBuffer != null)
                inBuffer.close();

        }
    }

    @Override
    public void saveAndRemoveContent(DataStore dataStore) {
        dataStoreDao.save(dataStore);
        String path = RepositoryUtils.getPath(RepositoryUtils.DATA_STORE, dataStore.getId());
        contentRepositoryDao.removeBinaryContent(path);
    }

    @Override
    public List<DataStore> findByDataStoreForMenu() {
        return findByParent(rootID);
    }

    public void setStreamBufferUtils(StreamBufferUtils streamBufferUtils) {
        this.streamBufferUtils = streamBufferUtils;
    }
}
