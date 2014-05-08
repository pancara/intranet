package com.integrasolusi.pusda.intranet.web.controller;

import com.integrasolusi.pusda.intranet.persistence.Document;
import com.integrasolusi.pusda.intranet.persistence.DocumentVersion;
import com.integrasolusi.pusda.intranet.persistence.Person;
import com.integrasolusi.pusda.intranet.persistence.Share;
import com.integrasolusi.pusda.intranet.service.*;
import com.integrasolusi.pusda.intranet.utils.ContentTypeUtils;
import com.integrasolusi.pusda.intranet.utils.PagingUtils;
import com.integrasolusi.pusda.intranet.utils.PropertyHelper;
import com.integrasolusi.pusda.intranet.web.form.DocumentForm;
import com.integrasolusi.pusda.intranet.web.form.RevisionForm;
import com.integrasolusi.pusda.intranet.web.form.SearchForm;
import com.integrasolusi.pusda.intranet.web.utils.HttpSessionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Jan 10, 2011
 * Time         : 6:13:37 PM
 */
@Controller
public class FileSharingController {
    private static Logger logger = Logger.getLogger(FileSharingController.class);

    private DocumentService documentService;

    private PersonService personService;

    private ShareService shareService;

    private DocumentVersionService documentVersionService;

    private MessageService messageService;

    private PagingUtils pagingUtils;

    @Autowired
    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setShareService(ShareService shareService) {
        this.shareService = shareService;
    }

    @Autowired
    public void setDocumentVersionService(DocumentVersionService documentVersionService) {
        this.documentVersionService = documentVersionService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setPagingUtils(PagingUtils pagingUtils) {
        this.pagingUtils = pagingUtils;
    }

    @RequestMapping("/file-sharing/list")
    public ModelAndView listDocument(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        String keyword = "%" + searchForm.getKeyword() + "%";
        Long documentCount = documentService.countByPersonAndKeyword(person, keyword);
        page = Math.min(page, pagingUtils.calcPageCount(documentCount.intValue()));

        int start = pagingUtils.getStartRow(page);
        List<Document> documentList = documentService.findByPersonAndKeyword(person, keyword, start, pagingUtils.getRowPerPage());

        ModelAndView mav = new ModelAndView("file-sharing-list");
        mav.addObject("firstRow", start);
        mav.addObject("currentPage", page);
        mav.addObject("documentCount", documentCount);
        mav.addObject("pageList", pagingUtils.getPageList(page, documentCount.intValue()));
        mav.addObject("documentList", documentList);

        return mav;
    }

    @RequestMapping(value = "/file-sharing/entry", method = RequestMethod.GET)
    public ModelAndView entryDocument(@ModelAttribute("documentForm") DocumentForm form) {
        return new ModelAndView("file-sharing-entry");
    }

    @RequestMapping(value = "/file-sharing/entry", method = RequestMethod.POST)
    public ModelAndView submitNewDocument(HttpServletRequest request, @ModelAttribute("documentForm") DocumentForm form, Errors errors) throws IOException {
        validateDocument(form, errors, true);
        if (errors.hasErrors()) {
            return new ModelAndView("file-sharing-entry");
        } else {
            saveNewDocument(form, new Document(), request);
            return new ModelAndView("redirect:/file-sharing/list.html");
        }
    }

    @RequestMapping(value = "/file-sharing/edit/{documentID}", method = RequestMethod.GET)
    public ModelAndView editDocument(@PathVariable("documentID") Long documentID, @ModelAttribute("documentForm") DocumentForm form) {
        Document document = documentService.findById(documentID);
        PropertyHelper.copyProperties(document, form);

        return new ModelAndView("file-sharing-edit");
    }

    @RequestMapping(value = "/file-sharing/edit/{documentID}", method = RequestMethod.POST)
    public ModelAndView submitEdit(HttpServletRequest request, @PathVariable("documentID") Long documentID,
                                   @ModelAttribute("documentForm") DocumentForm form, Errors errors) throws IOException {
        validateDocument(form, errors, false);
        if (errors.hasErrors()) {
            return new ModelAndView("file-sharing-edit");
        } else {
            // save old attribute
            Document oldDoc = new Document();
            Document document = documentService.findById(documentID);
            BeanUtils.copyProperties(document, oldDoc);

            saveEditDocument(form, document, request);

            if (BooleanUtils.isTrue(form.getSendNotification())) {
                List<Share> shares = shareService.findByDocument(document);
                List<Long> recipients = new LinkedList<Long>();
                for (Share share : shares)
                    recipients.add(share.getId());
                messageService.notifyDocumentChange(document.getOwner(), oldDoc, document, recipients);
            }
            return new ModelAndView("redirect:/file-sharing/list.html");
        }
    }

    @RequestMapping(value = "/file-sharing/download/{documentID}")
    public void downloadDocument(@PathVariable("documentID") Long documentID,
                                 HttpServletResponse response) throws IOException {
        Document document = documentService.findById(documentID);
        DocumentVersion latestVersion = documentVersionService.findLatest(document);
        if (latestVersion != null) {
            String fileUrl = StringUtils.replace(latestVersion.getFilename(), " ", "_");
            response.setHeader("Content-Disposition", String.format("attachment;filename=%s", fileUrl));
            documentService.writeDocumentContent(latestVersion, response.getOutputStream());
        }
        response.getOutputStream().flush();
    }

    private void validateDocument(DocumentForm form, Errors errors, boolean newData) {
        if (StringUtils.isEmpty(form.getName()))
            errors.reject("title.empty", "Judul belum diisi.");

        if (StringUtils.isEmpty(form.getDescription()))
            errors.reject("description.empty", "Keterangan belum diisi.");

        if (newData) {
            if (form.getDocument().isEmpty())
                errors.reject("file.empty", "Tentukan file yang diupload.");
        }

    }

    private void saveEditDocument(DocumentForm form, Document document, HttpServletRequest request) throws IOException {
        document.setName(form.getName());
        document.setDescription(form.getDescription());
        documentService.save(document);
    }

    private void saveNewDocument(DocumentForm form, Document document, HttpServletRequest request) throws IOException {
        BeanUtils.copyProperties(form, document, new String[]{"id"});
        document.setSubmitDate(new Date());
        document.setFilename(form.getDocument().getOriginalFilename());
        document.setSize(form.getDocument().getSize());
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        if (person != null)
            document.setOwner(person);
        documentService.save(document);

        DocumentVersion version = new DocumentVersion();
        version.setCommitter(document.getOwner());
        version.setCommitDate(document.getSubmitDate());
        version.setDescription("-");
        version.setFilename(form.getDocument().getOriginalFilename());
        version.setSize(form.getDocument().getSize());

        documentVersionService.save(document, version, form.getDocument().getInputStream());
    }

    @RequestMapping(value = "/file-sharing/remove/{documentID}")
    public ModelAndView removeDocument(@PathVariable("documentID") Long documentID, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) throws IOException {
        documentService.removeById(documentID);
        ModelAndView mav = new ModelAndView("redirect:/file-sharing/list.html?page=" + page);
        return mav;
    }

    @RequestMapping(value = "/file-sharing/manage/{documentID}")
    public ModelAndView manageDocument(HttpServletRequest request,
                                       @PathVariable("documentID") Long documentID) {
        ModelAndView mav = new ModelAndView("file-sharing-manage");

        Document document = documentService.findById(documentID);
        mav.addObject("document", document);

        List<DocumentVersion> versions = documentService.getVersions(document);
        mav.addObject("versions", versions);

        List<Share> shares = shareService.findByDocument(document);
        mav.addObject("shares", shares);
        Person person = HttpSessionUtils.getLoggedPrincipal(request);
        if (!person.getId().equals(document.getOwner().getId())) {
            Share share = shareService.findShareForPerson(document, person);
            mav.addObject("shareObject", share);

        }
        return mav;
    }

    @RequestMapping(value = "/ajax/file-sharing/share/{documentID}")
    public ModelAndView shareDocument(@PathVariable("documentID") Long documentID, @RequestParam(value = "persons", required = false) Long[] persons,
                                      @RequestParam(value = "canModify", required = false, defaultValue = "false") Boolean canModify) {
        ModelAndView mav = new ModelAndView("ajax/file-sharing-share-new");
        Document document = documentService.findById(documentID);
        Long countOldShare = shareService.countByDocument(document);
        mav.addObject("count_old_share", countOldShare);

        List<Share> newShares = new LinkedList<Share>();
        if (persons != null) {
            for (Long personID : persons) {
                Person person = personService.findById(personID);
                try {
                    Boolean canSubmitRevision = (canModify != null) && canModify.booleanValue();
                    Share share = shareService.shareTo(document, person, canSubmitRevision);
                    newShares.add(share);
                } catch (Exception e) {
                }
            }
        }

        messageService.notifyDocumentSetShare(document, newShares);

        mav.addObject("new_shares", newShares);
        return mav;
    }

    @RequestMapping(value = "/file-sharing/share/delete/{documentID}", method = RequestMethod.POST)
    public ModelAndView deleteShares(HttpServletRequest request,
                                     @PathVariable("documentID") Long documentID,
                                     @RequestParam(value = "shares", required = false) Long[] shares) {
        if (shares != null)
            for (Long share : shares)
                shareService.removeById(share);
        return new ModelAndView(String.format("redirect:/file-sharing/manage/%d.html", documentID));

    }

    @RequestMapping(value = "/file-sharing/revision/{documentID}", method = RequestMethod.GET)
    public ModelAndView formRevision(HttpServletRequest request,
                                     @PathVariable("documentID") Long documentID,
                                     @ModelAttribute("revisionForm") RevisionForm form) {
        ModelAndView mav = new ModelAndView("file-sharing-revision");
        Document document = documentService.findById(documentID);
        mav.addObject("document", document);
        mav.addObject("versions", documentService.getVersions(document));
        return mav;
    }

    @RequestMapping(value = "/file-sharing/revision/{documentID}", method = RequestMethod.POST)
    public ModelAndView submitRevision(HttpServletRequest request,
                                       @PathVariable("documentID") Long documentID,
                                       @ModelAttribute("revisionForm") RevisionForm form,
                                       Errors errors) throws IOException {
        if (validateRevisionForm(form, errors)) {
            Document document = documentService.findById(documentID);
            Person person = HttpSessionUtils.getLoggedPrincipal(request);
            DocumentVersion version = createVersionObject(person, form);

            documentVersionService.save(document, version, form.getDocument().getInputStream());

            // send notification for revision
            messageService.notifyDocumentRevision(version);

            String path = String.format("redirect:/file-sharing/revision/success/%d.html", version.getId());
            return new ModelAndView(path);
        } else {
            ModelAndView mav = new ModelAndView("file-sharing-revision");
            Document document = documentService.findById(documentID);
            mav.addObject("document", document);
            mav.addObject("versions", documentService.getVersions(document));
            return mav;
        }
    }


    @RequestMapping(value = "/file-sharing/revision/success/{versionId}")
    public ModelAndView submitRevision(HttpServletRequest request, @PathVariable("versionId") Long versionId) {
        DocumentVersion version = documentVersionService.findById(versionId);
        ModelAndView mav = new ModelAndView("file-sharing-revision-success");
        mav.addObject("version", version);
        return mav;
    }

    private DocumentVersion createVersionObject(Person commiter, RevisionForm form) {
        DocumentVersion version = new DocumentVersion();
        version.setCommitter(commiter);
        version.setCommitDate(new Date());
        version.setDescription(form.getDescription());
        version.setFilename(form.getDocument().getOriginalFilename());
        version.setSize(form.getDocument().getSize());
        return version;
    }

    private boolean validateRevisionForm(RevisionForm form, Errors errors) {
        if ((form.getDocument() == null) || (form.getDocument().isEmpty()))
            errors.reject("document.empty", "Dokumen belum diisi");

        return !errors.hasErrors();
    }

    @RequestMapping(value = "/file-sharing/download/version/{versionID}")
    public void downloadDocumentByVersion(@PathVariable("versionID") Long versionID,
                                          HttpServletResponse response) throws IOException {
        DocumentVersion version = documentVersionService.findById(versionID);
        if (version != null) {
            String fileUrl = StringUtils.replace(version.getFilename(), " ", "_");
            response.setHeader("Content-Disposition:", String.format("attachment;filename=%s", fileUrl));
            documentService.writeDocumentContent(version, response.getOutputStream());
        }
        response.getOutputStream().flush();
    }

    @RequestMapping(value = "/file-sharing/remove/version", method = RequestMethod.POST)
    public MappingJacksonJsonView removeVersion(HttpServletRequest request, @RequestParam("versionID") Long versionID) {
        MappingJacksonJsonView jsonView = new MappingJacksonJsonView();
        try {
            documentVersionService.removeById(versionID);
            jsonView.addStaticAttribute("result", true);
        } catch (Exception e) {
            jsonView.addStaticAttribute("result", false);
        }
        return jsonView;
    }
}

