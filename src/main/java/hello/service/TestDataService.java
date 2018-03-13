package src.main.java.hello;

import java.util.List;

import src.main.java.hello.exceptions.BadRequestException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(HasRole.DBA_URW)
public interface TestDataService {

    TestData save(TestData testData);

    void delete(TestData testData);

    @PreAuthorize(HasRole.DBA_URW_UR)
    TestData findTestDataById(Long id);

    @PreAuthorize(HasRole.DBA_URW_UR)
    Page<TestData> findTestDataAllByPage(Integer page, Integer limit);

    @PreAuthorize(HasRole.DBA_URW_UR)
    PageReturnFormat<TestData> criteriaSearchTestData(Integer page, Integer limit, String[] searchParams,
                                                      String[] sortByParams, String phone,
                                                      List<String> fieldnamesAvaliable, Class ClassV) throws BadRequestException;
    @PreAuthorize(HasRole.DBA_URW_UR)
    Page<TestData> findTestDataByNameAndEmail(String name, String email, Integer page, Integer limit);

}