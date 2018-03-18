package src.main.java.crm;

import src.main.java.crm.exceptions.InternalServerErrorException;
import src.main.java.crm.exceptions.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.google.common.collect.Lists;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;




@Service("clientsService")
@Repository
@Transactional
public class ClientsServiceImpl implements ClientsService {

    @Autowired
    private ClientsRepository clientsRepository;

    @Autowired
    private ServiceSearch serviceSearch;



    @Override
    public Clients save(Clients clients) {
        return clientsRepository.save(clients);
    }

    @Override
    public void delete(Clients clients) {
        clientsRepository.delete(clients);
    }


    @Override
    @Transactional(readOnly=true)
    public Clients findClientsById(Long id) {
        return clientsRepository.findOne(id);
    }


    @Override
    @Transactional(readOnly=true)
    public Page<Clients> findClientsAllByPage(Integer page, Integer limit) {
        if ( page == null || limit == null || !(page>0 && limit>0) )
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");

        return clientsRepository.findAllByOrderByIdAsc(new PageRequest(page-1, limit));
    }

    @Override
    @Transactional(readOnly=true)
    public List<Clients> findByCompanyId(Long companyId) {
        return clientsRepository.findClientsByCompanyId(companyId);
    }

    @Override
    @Transactional(readOnly=true)
    public Page<Clients> findClientsByNameAndEmail(String name, String email, Integer page, Integer limit) {
        if (name == null || email == null)
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");
        if ( page == null || limit == null || !(page>0 && limit>0) )
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");

        return clientsRepository.findClientsByNameAndEmail(name, email, new PageRequest(page-1, limit));
    }


    @Override
    @Transactional(readOnly=true)
    public PageReturnFormat<Clients> criteriaSearchClients(Integer page , Integer limit, String[] searchParams,
                                                           String[] sortByParams, String phone, List<String> fieldnamesAvaliable,
                                                              Class ClassV) throws BadRequestException {

        PageReturnFormat<Clients> pageReturnFormat = serviceSearch.criteriaSearch(
                page , limit, searchParams, sortByParams, phone, fieldnamesAvaliable, Clients.class);

        return pageReturnFormat;
    }


}