package src.main.java.crm;

import src.main.java.crm.exceptions.*;

import java.util.List;
import org.springframework.data.domain.Page;


import org.springframework.security.access.prepost.PreAuthorize;

import java.util.*;

@PreAuthorize(HasRole.DBA_URW)
public interface CompanyService {

    public Company save(Company company);

    public void delete(Company company);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public Company findById(Long id);
    @PreAuthorize(HasRole.DBA_URW_UR)
    public Company findByName(String name);
    @PreAuthorize(HasRole.DBA_URW_UR)
    public Page<Company> findAllByPage(Integer page, Integer limit);

    @PreAuthorize(HasRole.DBA_URW_UR)
    PageReturnFormat<Company> criteriaSearchCompany(Integer page, Integer limit, String[] searchParams,
                                                    String[] sortByParams, String nullvalue,
                                                    List<String> fieldnamesAvaliable, Class ClassV) throws BadRequestException;


    @PreAuthorize(HasRole.DBA_URW_UR)
    public Set<String> selectCompanyEmailsByCompanyIdList(List<Long> companyIdList);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public Set<String> selectClientsEmailsByCompanyIdList(List<Long> companyIdList);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public Set<String> selectCompanyEmails();

    @PreAuthorize(HasRole.DBA_URW_UR)
    public Set<String> selectClientsEmails();

}