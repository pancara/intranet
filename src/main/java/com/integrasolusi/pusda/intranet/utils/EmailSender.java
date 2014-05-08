package com.integrasolusi.pusda.intranet.utils;

import com.integrasolusi.pusda.intranet.persistence.Person;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

/**
 * Programmer   : pancara
 * Date         : Feb 16, 2011
 * Time         : 10:57:02 AM
 */
public class EmailSender {
    private Logger logger = Logger.getLogger(EmailSender.class);
    private JavaMailSender mailSender;
    private TextGenerator textGenerator;
    private String replyAddress;

    EmailSenderRunner runner = new EmailSenderRunner();


    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTextGenerator(TextGenerator textGenerator) {
        this.textGenerator = textGenerator;
    }

    public void setReplyAddress(String replyAddress) {
        this.replyAddress = replyAddress;
    }

    public void sendConfirmationRegistrationEmail(Person person) {
        MimeMessagePreparator message = createConfirmRegistrationMessage(person);
        sendEmail(message);
    }

    public void sendApprovalInformation(Person person) {
        MimeMessagePreparator message = createApprovalMessage(person);
        sendEmail(message);
    }

    public void sendPasswordRecovery(Person person) {
        MimeMessagePreparator message = createPasswordRecoryMessage(person);
        sendEmail(message);
    }

    private void sendEmail(MimeMessagePreparator message) {
        runner.queueMessage(message);
        if (runner.terminated)
            runner.start();
    }

    private MimeMessagePreparator createConfirmRegistrationMessage(final Person person) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(javax.mail.internet.MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setSubject("Permohonan Registrasi");
                message.setSentDate(new Date());
                message.setFrom(replyAddress);
                message.setTo(person.getEmail());
                Map model = new HashMap();
                model.put("person", person);
                String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_REGISTRATION_CONFIRMATION, model);
                mimeMessage.setText(text, "UTF-8", "html");
            }
        };
        return preparator;
    }

    private MimeMessagePreparator createApprovalMessage(final Person person) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(javax.mail.internet.MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setSubject("Permohonan Registrasi");
                message.setSentDate(new Date());
                message.setFrom(replyAddress);
                message.setTo(person.getEmail());
                Map model = new HashMap();
                model.put("person", person);
                String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_APPROVAL_INFORMATION, model);
                mimeMessage.setText(text, "UTF-8", "html");
            }
        };
        return preparator;
    }


    private MimeMessagePreparator createPasswordRecoryMessage(final Person person) {
        logger.info("create password recovery email");
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(javax.mail.internet.MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setSubject("Password Recovery");
                message.setSentDate(new Date());
                message.setFrom(replyAddress);
                message.setTo(person.getEmail());
                Map model = new HashMap();
                model.put("person", person);
                String text = textGenerator.generateText(TextGenerator.NotifyType.NOTIFY_PASSWORD_RECOVERY, model);
                mimeMessage.setText(text, "UTF-8", "html");
            }
        };
        return preparator;
    }

    public void terminate() {
        runner.terminate();
    }

    private class EmailSenderRunner extends Thread {
        private Queue<MimeMessagePreparator> queue = new LinkedList<MimeMessagePreparator>();
        private boolean terminated = true;

        @Override
        public void run() {
            terminated = false;
            while (!terminated) {
                MimeMessagePreparator message = queue.poll();
                if (message != null) {
                    logger.info("sending email...");
                    mailSender.send(message);
                } else {
                    try {
                        Thread.currentThread().sleep(2000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void queueMessage(MimeMessagePreparator message) {
            queue.add(message);
        }

        public void terminate() {
            terminated = true;
        }
    }
}
