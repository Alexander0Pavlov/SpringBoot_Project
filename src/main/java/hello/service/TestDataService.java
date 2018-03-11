package src.main.java.hello;

import java.util.List;

import src.main.java.hello.exceptions.BadRequestException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;




public interface TestDataService {
    TestData save(TestData testData);

    void delete(TestData testData);


    TestData findTestDataById(Long id);


    Page<TestData> findTestDataAllByPage(Integer page, Integer limit);


    PageReturnFormat<TestData> criteriaSearchTestData(Integer page, Integer limit, String[] searchParams,
                                                      String[] sortByParams, String phone,
                                                      List<String> fieldnamesAvaliable, Class ClassV) throws BadRequestException;

    Page<TestData> findTestDataByNameAndEmail(String name, String email, Integer page, Integer limit);

}