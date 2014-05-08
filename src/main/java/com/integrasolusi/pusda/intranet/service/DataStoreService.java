package com.integrasolusi.pusda.intranet.service;

import com.integrasolusi.pusda.intranet.persistence.DataStore;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 3, 2011
 * Time         : 5:48:36 PM
 */
public interface DataStoreService {
    List<DataStore> findByParent(Long id);

    DataStore getRoot();

    DataStore findById(Long id);

    void save(DataStore dataStore);

    void save(DataStore dataStore, InputStream inputStream) throws IOException;

    void remove(DataStore dataStore);

    void normalizeChildIndex(DataStore parent);

    void moveUp(DataStore dataStore);

    void moveDown(DataStore dataStore);

    List<DataStore> findAllsFolders(String query);

    void copyContentToStream(Long id, OutputStream outputStream) throws Exception;

    void saveAndRemoveContent(DataStore dataStore);

    List<DataStore> findByDataStoreForMenu();
}
