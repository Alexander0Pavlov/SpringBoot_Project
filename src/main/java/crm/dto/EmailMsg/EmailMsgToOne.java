package src.main.java.crm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailMsgToOne extends EmailMsg {

    @NotNull
    @Size(min=6, max=254)
    private String to;


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


}