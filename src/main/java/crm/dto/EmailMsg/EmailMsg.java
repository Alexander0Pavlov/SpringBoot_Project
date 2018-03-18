package src.main.java.crm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailMsg {


    @NotNull
    @Size(min=0, max=78)
    private String subject;
    @NotNull
    @Size(min=0, max=5000)
    private String text;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}