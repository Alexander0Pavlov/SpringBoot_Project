package src.main.java.crm;

import java.util.List;

public class ClientsInfoList {
    private List<ClientsInfo> clientsInfoList;

    public ClientsInfoList() {
    }

    public ClientsInfoList(List<ClientsInfo> clientsInfoList) {
        this.clientsInfoList = clientsInfoList;
    }


    public void setClientsInfoList(List<ClientsInfo> clientsInfoList) {
        this.clientsInfoList = clientsInfoList;
    }

    public List<ClientsInfo> getClientsInfoList() {
        return clientsInfoList;
    }

}