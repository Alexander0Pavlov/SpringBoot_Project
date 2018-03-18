package src.main.java.crm;

import java.util.List;

import src.main.java.crm.exceptions.BadRequestException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(HasRole.DBA_URW)
public interface ClientsService {

    Clients save(Clients clients);

    void delete(Clients clients);

    @PreAuthorize(HasRole.DBA_URW_UR)
    Clients findClientsById(Long id);

    @PreAuthorize(HasRole.DBA_URW_UR)
    Page<Clients> findClientsAllByPage(Integer page, Integer limit);

    @PreAuthorize(HasRole.DBA_URW_UR)
    List<Clients> findByCompanyId(Long companyId);

    @PreAuthorize(HasRole.DBA_URW_UR)
    PageReturnFormat<Clients> criteriaSearchClients(Integer page, Integer limit, String[] searchParams,
                                                      String[] sortByParams, String phone,
                                                      List<String> fieldnamesAvaliable, Class ClassV) throws BadRequestException;
    @PreAuthorize(HasRole.DBA_URW_UR)
    Page<Clients> findClientsByNameAndEmail(String name, String email, Integer page, Integer limit);

}