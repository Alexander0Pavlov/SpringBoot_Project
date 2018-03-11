package src.main.java.hello;

import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TestDataRepository extends PagingAndSortingRepository<TestData, Long> {

    @Query("SELECT t from TestData t LEFT JOIN t.testDataInfoSet ti " +
            "WHERE lower(t.name) = lower(:nameParam) AND " +
            "lower(ti.email) like lower(concat('%',:emailParam,'%')) " +
            "ORDER BY t.id")
    Page<TestData> findTestDataByNameAndEmail(@Param("nameParam") String nameParam, @Param("emailParam") String emailParam,
                                              Pageable pageable);



    Page<TestData> findAllByOrderByIdAsc(Pageable pageable);

}