package src.main.java.crm;

import java.util.List;

public class ClientsList {
    private List<Clients> clientsList;

    public ClientsList() {
    }

    public ClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }


    public void setClientsList(List<Clients> clientsList) {
        this.clientsList = clientsList;
    }

    public List<Clients> getClientsList() {
        return clientsList;
    }

}