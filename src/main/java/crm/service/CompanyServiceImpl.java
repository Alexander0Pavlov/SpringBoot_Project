package src.main.java.crm;

import src.main.java.crm.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Service("companyService")
@Repository
@Transactional
public class CompanyServiceImpl implements  CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ServiceSearch serviceSearch;


    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public void delete(Company company) {
        companyRepository.delete(company);
    }

    @Override
    @Transactional(readOnly = true)
    public  Company findById(Long id) {
        return companyRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Company findByName(String name){return companyRepository.findByName(name);}

    @Override
    @Transactional(readOnly = true)
    public Page<Company> findAllByPage(Integer page, Integer limit) {
        if ( page == null || limit == null || !(page>0 && limit>0) )
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");


        return companyRepository.findAllByOrderByIdAsc(new PageRequest(page-1, limit));
    }



    @Override
    @Transactional(readOnly=true)
    public PageReturnFormat<Company> criteriaSearchCompany(Integer page , Integer limit, String[] searchParams,
                                                           String[] sortByParams, String nullvalue, List<String> fieldnamesAvaliable,
                                                           Class ClassV) throws BadRequestException {

        PageReturnFormat<Company> pageReturnFormat = serviceSearch.criteriaSearch(
                page , limit, searchParams, sortByParams, null, fieldnamesAvaliable, Company.class);

        return pageReturnFormat;
    }



    public Set<String> selectCompanyEmailsByCompanyIdList(List<Long> companyIdList) {
        return companyRepository.selectCompanyEmailsByCompanyIdList(companyIdList);
    }


    public Set<String> selectClientsEmailsByCompanyIdList(List<Long> companyIdList) {
        return companyRepository.selectClientsEmailsByCompanyIdList(companyIdList);
    }

    public Set<String> selectCompanyEmails() {
        return companyRepository.selectCompanyEmails();
    }

    public Set<String> selectClientsEmails() {
        return companyRepository.selectClientsEmails();
    }

}