package com.integrasolusi.pusda.intranet.utils;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

/**
 * Programmer   : pancara
 * Date         : Jan 19, 2011
 * Time         : 9:06:32 PM
 */
public class TextGenerator {
    public static enum NotifyType {
        NOTIFY_REGISTRATION_CONFIRMATION,
        NOTIFY_APPROVAL_INFORMATION,
        NOTIFY_DOCUMENT_CHANGE,
        NOTIFY_DOCUMENT_REVISION,
        NOTIFY_DOCUMENT_SET_SHARE,
        NOTIFY_PASSWORD_RECOVERY
    }

    private VelocityEngine velocityEngine;

    private String registrationConfirmationTextTemplate;
    private String approvalInformationTextTemplate;
    private String notificationDocumentChangeTemplate;
    private String notifificationDocumentRevisionTemplate;
    private String notificationPasswordRecoveryTemplate;
    private String notificationSetShare;

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setRegistrationConfirmationTextTemplate(String template) {
        this.registrationConfirmationTextTemplate = template;
    }

    public void setApprovalInformationTextTemplate(String template) {
        this.approvalInformationTextTemplate = template;
    }

    public void setNotificationDocumentChangeTemplate(String template) {
        this.notificationDocumentChangeTemplate = template;
    }

    public void setNotifificationDocumentRevisionTemplate(String template) {
        this.notifificationDocumentRevisionTemplate = template;
    }

    public void setNotificationPasswordRecoveryTemplate(String notificationPasswordRecoveryTemplate) {
        this.notificationPasswordRecoveryTemplate = notificationPasswordRecoveryTemplate;
    }

    public void setNotificationSetShare(String notificationSetShare) {
        this.notificationSetShare = notificationSetShare;
    }

    private String applyTemplate(String templateName, Map model) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateName, model);
    }

    public String generateText(NotifyType type, Map model) {
        switch (type) {
            case NOTIFY_REGISTRATION_CONFIRMATION:
                return applyTemplate(registrationConfirmationTextTemplate, model);

            case NOTIFY_APPROVAL_INFORMATION:
                return applyTemplate(approvalInformationTextTemplate, model);

            case NOTIFY_DOCUMENT_CHANGE:
                return applyTemplate(notificationDocumentChangeTemplate, model);

            case NOTIFY_DOCUMENT_REVISION:
                return applyTemplate(notifificationDocumentRevisionTemplate, model);

            case NOTIFY_PASSWORD_RECOVERY:
                return applyTemplate(notificationPasswordRecoveryTemplate, model);

            case NOTIFY_DOCUMENT_SET_SHARE:
                return applyTemplate(notificationSetShare, model);
        }
        return null;
    }


}
