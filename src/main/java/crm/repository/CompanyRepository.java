package src.main.java.crm;

import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

    public Company findByName(String name);

    public Page<Company> findAllByOrderByIdAsc(Pageable pageable);


    @Query("SELECT DISTINCT com.email " +
            "FROM Company com " +
            "WHERE com.id IN (:companyIdList)")
    Set<String> selectCompanyEmailsByCompanyIdList(@Param("companyIdList") List<Long> companyIdList);


    @Query("SELECT DISTINCT ci.email " +
            "FROM Company com INNER JOIN com.clients cl INNER JOIN cl.clientsInfoSet ci " +
            "WHERE com.id IN (:companyIdList)")
    Set<String> selectClientsEmailsByCompanyIdList(@Param("companyIdList") List<Long> companyIdList);


    @Query("SELECT DISTINCT com.email " +
            "FROM Company com")
    Set<String> selectCompanyEmails();


    @Query("SELECT DISTINCT ci.email " +
            "FROM Company com INNER JOIN com.clients cl INNER JOIN cl.clientsInfoSet ci")
    Set<String> selectClientsEmails();

}