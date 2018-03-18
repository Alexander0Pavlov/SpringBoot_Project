package src.main.java.crm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service("clientsInfoService")
@Repository
@Transactional
public class ClientsInfoServiceImpl implements ClientsInfoService {

    @Autowired
    private ClientsInfoRepository clientsInfoRepository;


    @Override
    public ClientsInfo save(ClientsInfo clientsInfo) {
        return clientsInfoRepository.save(clientsInfo);
    }

    @Override
    public void delete(ClientsInfo clientsInfo) {
        clientsInfoRepository.delete(clientsInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientsInfo findClientsInfoById(Long id) {
        return clientsInfoRepository.findOne(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ClientsInfo> findClientsInfoByClientsId(Long testdataId) {
        return clientsInfoRepository.findClientsInfoByClientsId(testdataId);
    }

}