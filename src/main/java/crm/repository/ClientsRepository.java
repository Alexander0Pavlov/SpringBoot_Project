package src.main.java.crm;

import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ClientsRepository extends PagingAndSortingRepository<Clients, Long> {

    @Query("SELECT c from Clients c LEFT JOIN c.clientsInfoSet ci " +
            "WHERE lower(c.name) = lower(:nameParam) AND " +
            "lower(ci.email) like lower(concat('%',:emailParam,'%')) " +
            "ORDER BY c.id")
    Page<Clients> findClientsByNameAndEmail(@Param("nameParam") String nameParam, @Param("emailParam") String emailParam,
                                              Pageable pageable);


    List<Clients> findClientsByCompanyId(Long companyId);

    Page<Clients> findAllByOrderByIdAsc(Pageable pageable);

}