package src.main.java.crm;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(HasRole.DBA_URW)
public interface ClientsInfoService {

    public ClientsInfo save(ClientsInfo clientsInfo);

    public void delete(ClientsInfo clientsInfo);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public ClientsInfo findClientsInfoById(Long id);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public List<ClientsInfo> findClientsInfoByClientsId(Long clientsId);
}