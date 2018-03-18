package src.main.java.crm;

import java.util.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmailMsgToCompanyIdList extends EmailMsg {

    @NotNull
    private List<Long> companyIdList;

    public List<Long> getCompanyIdList() {
        return companyIdList;
    }

    public void setCompanyIdList(List<Long> companyIdList) {
        this.companyIdList = companyIdList;
    }

}