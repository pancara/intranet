package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.json.PropertyFilterSerializerFactory;
import com.integrasolusi.pusda.intranet.persistence.DataStore;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.service.DataStoreService;
import com.integrasolusi.pusda.intranet.utils.ContentTypeUtils;
import com.integrasolusi.pusda.intranet.utils.DataStoreUtils;
import com.integrasolusi.pusda.intranet.web.form.DataStoreDeleteForm;
import com.integrasolusi.pusda.intranet.web.form.DataStoreForm;
import com.integrasolusi.pusda.intranet.web.form.SelectDataStoreFolderForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpRequestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.tika.mime.MediaTypeRegistry;
import org.apache.velocity.runtime.parser.node.MathUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import thredds.cataloggen.DatasetScanCatalogBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Programmer   : pancara
 * Date         : Jan 26, 2011
 * Time         : 2:19:35 AM
 */

@Controller
public class DataStoreController {
    private static Logger logger = Logger.getLogger(DataStoreController.class);
    private final static Integer FOLDER = 1;
    private final static Integer FILE = 2;

    private DataStoreService dataStoreService;

    @Autowired
    private void setDataStoreService(DataStoreService dataStoreService) {
        this.dataStoreService = dataStoreService;
    }

    @RequestMapping("/data-store")
    public ModelAndView getDataStore(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("data-store");
        DataStore root = dataStoreService.getRoot();
        mav.addObject("root", root);

        List<DataStore> nodes = dataStoreService.findByParent(root.getId());
        mav.addObject("nodes", nodes);

        return mav;
    }

    @RequestMapping("/ajax/data-store/{dataStoreID}")
    public ModelAndView getDataStore(HttpServletRequest request, @PathVariable("dataStoreID") Long dataStoreID) {
        ModelAndView mav = new ModelAndView("ajax/data-store");
        mav.addObject("dataStore", dataStoreService.findById(dataStoreID));
        return mav;
    }

    @RequestMapping("/data-store/table/{root}")
    public ModelAndView viewTable(HttpServletRequest request, @PathVariable("root") Long root) {
        ModelAndView mav = new ModelAndView("data-store-table");
        DataStore rootNode = dataStoreService.findById(root);
        mav.addObject("rootNode", rootNode);
        mav.addObject("children", dataStoreService.findByParent(rootNode.getId()));
        return mav;
    }

    @RequestMapping("/data-store/browse/{root}")
    public ModelAndView browseDataStoreTree(HttpServletRequest request, @PathVariable("root") Long rootID) {
        ModelAndView mav = new ModelAndView("data-store-browse");
        DataStore root = dataStoreService.findById(rootID);
        mav.addObject("root", root);
        mav.addObject("dataStoreTree", populateDataStoreTree(root));
        return mav;
    }

    @RequestMapping(value = "/data-store/{root}/entry/{parent}", method = RequestMethod.GET)
    public ModelAndView newDataStore(@PathVariable("root") Long rootID,
                                     @PathVariable("parent") Long parent,
                                     @ModelAttribute("dataStoreForm") DataStoreForm form) {
        ModelAndView mav = new ModelAndView("data-store-entry");
        DataStore root = dataStoreService.findById(rootID);
        mav.addObject("root", root);
        mav.addObject("storageTypes", populateStorageTypes());
        mav.addObject("parent", dataStoreService.findById(parent));
        return mav;
    }

    @RequestMapping(value = "/data-store/{root}/entry/{parent}", method = RequestMethod.POST)
    public ModelAndView submitNewDataStore(@PathVariable("root") Long rootID,
                                           @PathVariable("parent") Long parent,
                                           @ModelAttribute("dataStoreForm") DataStoreForm form, Errors errors) throws IOException {
        DataStore root = dataStoreService.findById(rootID);
        if (validateDataStoreForm(form, errors, false)) {
            DataStore dataStore = createNewDataStore(form);
            dataStore.setParent(dataStoreService.findById(parent));
            if (DataStoreUtils.isFolder(dataStore)) {
                dataStoreService.save(dataStore);
            } else {
                Integer type = DataStoreUtils.getDataStoreTypeByFileName(form.getFile().getOriginalFilename());
                dataStore.setStorageType(type);
                dataStore.setFilename(form.getFile().getOriginalFilename());
                dataStore.setSize(form.getFile().getSize());
                dataStoreService.save(dataStore, form.getFile().getInputStream());
            }
            return new ModelAndView(String.format("redirect:/data-store/browse/%d.html#%d", root.getId(), dataStore.getId()));
        } else {
            ModelAndView mav = new ModelAndView("data-store-entry");
            mav.addObject("root", root);
            mav.addObject("storageTypes", populateStorageTypes());
            mav.addObject("parent", dataStoreService.findById(parent));
            return mav;
        }
    }

    @RequestMapping(value = "/data-store/{root}/edit/{dataStore}", method = RequestMethod.GET)
    public ModelAndView editDataStore(@ModelAttribute("dataStoreForm") DataStoreForm form,
                                      @PathVariable("root") Long rootID,
                                      @PathVariable("dataStore") Long dataStoreID) {
        ModelAndView mav = new ModelAndView("data-store-edit");
        if (rootID != null) {
            DataStore root = dataStoreService.findById(rootID);
            mav.addObject("root", root);
        }

        DataStore dataStore = dataStoreService.findById(dataStoreID);
        mav.addObject("dataStore", dataStore);

        form.setName(dataStore.getName());
        form.setDescription(dataStore.getDescription());
        if (DataStoreUtils.isFolder(dataStore))
            form.setStorageType(DataStoreController.FOLDER);
        else
            form.setStorageType(DataStoreController.FILE);

        mav.addObject("storageTypes", populateStorageTypes());
        return mav;
    }

    @RequestMapping(value = "/data-store/{root}/edit/{dataStore}", method = RequestMethod.POST)
    public ModelAndView submitEditDataStore(@PathVariable("root") Long rootID,
                                            @PathVariable("dataStore") Long dataStoreID,
                                            @ModelAttribute("dataStoreForm") DataStoreForm form,
                                            Errors errors) throws IOException {
        DataStore root = dataStoreService.findById(rootID);
        if (validateDataStoreForm(form, errors, true)) {
            DataStore dataStore = dataStoreService.findById(dataStoreID);
            dataStore.setName(form.getName());
            dataStore.setDescription(form.getDescription());
            if (FOLDER.equals(form.getStorageType())) {
                dataStore.setStorageType(DataStore.FOLDER);
                dataStoreService.saveAndRemoveContent(dataStore);
            } else {
                if (form.getFile() != null && !form.getFile().isEmpty()) {
                    Integer type = DataStoreUtils.getDataStoreTypeByFileName(form.getFile().getOriginalFilename());
                    dataStore.setStorageType(type);
                    dataStore.setFilename(form.getFile().getOriginalFilename());
                    dataStore.setSize(form.getFile().getSize());
                    dataStoreService.save(dataStore, form.getFile().getInputStream());
                } else {
                    dataStoreService.save(dataStore);
                }
            }
            if (rootID != null)
                return new ModelAndView(String.format("redirect:/data-store/browse/%d.html#%d", root.getId(), dataStore.getId()));
            else
                return new ModelAndView("redirect:/data-store.html");
        } else {
            ModelAndView mav = new ModelAndView("data-store-edit");
            if (rootID != null) {
                mav.addObject("root", root);
            }
            mav.addObject("storageTypes", populateStorageTypes());
            mav.addObject("dataStore", dataStoreService.findById(dataStoreID));
            return mav;
        }
    }

    private boolean validateDataStoreForm(DataStoreForm form, Errors errors, boolean isUpdate) {
        if (StringUtils.isEmpty(form.getName())) {
            errors.reject("name.empty", "Nama belum diisi");
        }

        if (StringUtils.isEmpty(form.getDescription())) {
            errors.reject("description.empty", "Keterangan belum diisi");
        }

        if (!isUpdate) {
            if (DataStore.FILE.equals(form.getStorageType())) {
                if (form.getFile().isEmpty()) {
                    errors.reject("file.empty", "File belum diisi");
                }
            }
        }

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/data-store/{root}/delete/{dataStore}", method = RequestMethod.GET)
    public ModelAndView deleteDataStore(@PathVariable("root") Long rootID,
                                        @PathVariable("dataStore") Long dataStoreID,
                                        @ModelAttribute("dataStoreDeleteForm") DataStoreDeleteForm form,
                                        HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("data-store-delete");
        DataStore root = dataStoreService.findById(rootID);
        mav.addObject("root", root);

        DataStore dataStore = dataStoreService.findById(dataStoreID);
        mav.addObject("dataStore", dataStore);
        return mav;
    }

    @RequestMapping(value = "/data-store/{root}/delete/{dataStore}", method = RequestMethod.POST)
    public ModelAndView submitDeleteDataStore(@PathVariable("root") Long rootID,
                                              @PathVariable("dataStore") Long dataStoreID,
                                              HttpServletRequest request,
                                              @ModelAttribute("dataStoreDeleteForm") DataStoreDeleteForm form,
                                              Errors errors) {
        if (HttpRequestUtils.isCancelRequest(request, "cancel"))
            return new ModelAndView("redirect:/data-store.html");

        DataStore root = dataStoreService.findById(rootID);

        DataStore dataStore = dataStoreService.findById(dataStoreID);
        if (form.isDeleteTree()) {
            deleteDataStoreTree(dataStore);
        } else {
            if (dataStore.getChildrenCount() > 0) {
                ModelAndView mav = new ModelAndView("data-store-delete");
                mav.addObject("root", root);
                mav.addObject("dataStore", dataStore);
                errors.reject("delete.fail", "Penghapusan gagal. Data store masih berisi folder/file.");
                return mav;
            }
            dataStoreService.remove(dataStore);
        }
        return new ModelAndView(String.format("redirect:/data-store/browse/%d.html", root.getId()));
    }

    @RequestMapping(value = "/data-store/{root}/sort/{parent}", method = RequestMethod.GET)
    public ModelAndView sortDataStore(@PathVariable("root") Long rootID,
                                      @PathVariable("parent") Long parentID,
                                      HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("data-store-sort");

        DataStore root = dataStoreService.findById(rootID);
        mav.addObject("root", root);
        DataStore parent = dataStoreService.findById(parentID);
        dataStoreService.normalizeChildIndex(parent);
        mav.addObject("parent", parent);
        mav.addObject("children", dataStoreService.findByParent(parentID));
        return mav;
    }

    @RequestMapping(value = "/ajax/data-store/moveup/{dataStoreID}", method = RequestMethod.GET)
    public ModelAndView moveUp(@PathVariable("dataStoreID") Long dataStoreID,
                               HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("ajax/data-store-moveup");
        DataStore dataStore = dataStoreService.findById(dataStoreID);
        dataStoreService.moveUp(dataStore);
        return mav;
    }

    @RequestMapping(value = "/ajax/data-store/movedown/{dataStoreID}", method = RequestMethod.GET)
    public ModelAndView moveDown(@PathVariable("dataStoreID") Long dataStoreID,
                                 HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("ajax/data-store-movedown");
        DataStore dataStore = dataStoreService.findById(dataStoreID);
        dataStoreService.moveDown(dataStore);
        return mav;
    }

    @RequestMapping(value = "/data-store/{root}/move/{dataStore}", method = RequestMethod.GET)
    public ModelAndView moveDataStore(HttpServletRequest request,
                                      @PathVariable("root") Long rootID,
                                      @PathVariable("dataStore") Long dataStoreID,
                                      @ModelAttribute("selectDataStoreFolderForm") SelectDataStoreFolderForm form) {
        ModelAndView mav = new ModelAndView("data-store-move");

        DataStore root = dataStoreService.findById(rootID);
        mav.addObject("root", root);

        DataStore dataStore = dataStoreService.findById(dataStoreID);
        mav.addObject("dataStore", dataStore);
        return mav;
    }

    @RequestMapping(value = "/data-store/{root}/move/{dataStore}", method = RequestMethod.POST)
    public ModelAndView submitMoveDataStore(HttpServletRequest request,
                                            @PathVariable("root") Long rootID,
                                            @PathVariable("dataStore") Long dataStoreID,
                                            @ModelAttribute("selectDataStoreFolderForm") SelectDataStoreFolderForm form,
                                            Errors errors) {
        DataStore dataStore = dataStoreService.findById(dataStoreID);
        if (validateSelectDataStoreFolderForm(dataStore, form, errors)) {
            DataStore root = dataStoreService.findById(rootID);
            if (form.getFolder() != null) {
                DataStore folder = dataStoreService.findById(form.getFolder());
                dataStore.setParent(folder);
            } else {
                dataStore.setParent(null);
            }
            dataStoreService.save(dataStore);
            return new ModelAndView(String.format("redirect:/data-store/browse/%d.html#%d", root.getId(), dataStore.getId()));
        } else {
            ModelAndView mav = new ModelAndView("data-store-move");
            DataStore root = dataStoreService.findById(rootID);
            mav.addObject("root", root);
            mav.addObject("dataStore", dataStore);
            return mav;
        }
    }

    private boolean validateSelectDataStoreFolderForm(DataStore dataStore, SelectDataStoreFolderForm form, Errors errors) {
        if (form.getFolder() == null) {
            errors.reject("folder.empty", "Tentukan folder.");
        } else {
            if (isCircular(dataStore, form.getFolder()))
                errors.reject("folder.circular", "Folder membentuk hubungan sirkular");
        }
        return !errors.hasErrors();
    }

    @RequestMapping(value = "/data-store/download/{dataStore}", method = RequestMethod.GET)
    public void downloadDocument(@PathVariable("dataStore") Long dataStoreID, HttpServletResponse response) throws Exception {
        DataStore dataStore = dataStoreService.findById(dataStoreID);
        response.setContentType(ContentTypeUtils.getTypeByFilenameExt(FilenameUtils.getExtension(dataStore.getFilename())));

        String fileUrl = StringUtils.replace(dataStore.getFilename(), " ", "_");
        response.addHeader("Content-Disposition", String.format("attachment;filename=%s", fileUrl));
        if ((dataStore.getSize() != null) && (dataStore.getSize().intValue() > 0))
            response.addHeader("Content-Length", String.valueOf(dataStore.getSize()));

        try {
            dataStoreService.copyContentToStream(dataStoreID, response.getOutputStream());
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @RequestMapping(value = "/json/data-store/folder/list", method = RequestMethod.GET)
    public MappingJacksonJsonView listFolder(HttpServletRequest request,
                                             @RequestParam("q") String query) throws IOException {
        MappingJacksonJsonView view = new MappingJacksonJsonView();
        view.setObjectMapper(createDataStoreMapper());

        view.addStaticAttribute("results", dataStoreService.findAllsFolders(query));
        return view;
    }

    private ObjectMapper createDataStoreMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializerFactory(new PropertyFilterSerializerFactory(DataStore.class,
                new String[]{"id", "name", "description"}));
        mapper.getSerializationConfig().setSerializationView(Person.class);
        return mapper;
    }

    private boolean isCircular(DataStore dataStore, Long folderID) {
        if (dataStore.getId().equals(folderID)) return true;
        DataStore folder = dataStoreService.findById(folderID);
        if (folder.getParent() == null)
            return false;
        return isCircular(dataStore, folder.getParent().getId());

    }

    private void deleteDataStoreTree(DataStore dataStore) {
        List<DataStore> children = dataStoreService.findByParent(dataStore.getId());
        for (DataStore child : children)
            deleteDataStoreTree(child);
        dataStoreService.remove(dataStore);
    }

    private DataStore createNewDataStore(DataStoreForm form) {
        DataStore dataStore = new DataStore();
        dataStore.setName(form.getName());
        dataStore.setDescription(form.getDescription());
        dataStore.setSubmitDate(new Date());
        dataStore.setStorageType(form.getStorageType());
        return dataStore;
    }

    private List<DataStore> populateDataStoreTree() {
        return populateDataStoreTree(null);
    }

    private List<DataStore> populateDataStoreTree(DataStore root) {
        if (root == null) {
            root = dataStoreService.getRoot();
        }

        List<DataStore> rootNodes = new LinkedList<DataStore>();
        rootNodes.add(root);
        populateChildren(rootNodes);
        return rootNodes;
    }

    private void populateChildren(List<DataStore> nodes) {
        for (DataStore node : nodes) {
            List<DataStore> children = dataStoreService.findByParent(node.getId());
            node.setChildren(children);
            populateChildren(children);
        }
    }

    private Map<Integer, String> populateStorageTypes() {
        Map<Integer, String> storageTypes = new HashMap<Integer, String>();
        storageTypes.put(DataStoreController.FOLDER, "Folder");
        storageTypes.put(DataStoreController.FILE, "File");
        return storageTypes;
    }
}
