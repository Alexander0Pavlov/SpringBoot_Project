package src.main.java.crm;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(HasRole.DBA)
public interface EmailService {

    public void sendWithoutAttachment(String to, String subject, String text);

    public void sendWithAttachmentInside(String to, String subject, String text, boolean isHtmlText);
}