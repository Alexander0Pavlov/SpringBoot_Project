package src.main.java.crm;

import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;
import java.util.Set;

public interface ClientsInfoRepository extends PagingAndSortingRepository<ClientsInfo, Long> {

    List<ClientsInfo> findClientsInfoByClientsId(Long clientsId);


}