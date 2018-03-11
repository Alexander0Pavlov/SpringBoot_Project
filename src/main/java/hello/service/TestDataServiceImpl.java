package src.main.java.hello;

import src.main.java.hello.exceptions.InternalServerErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.google.common.collect.Lists;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import src.main.java.hello.exceptions.BadRequestException;


@Service("testDataService")
@Repository
@Transactional
public class TestDataServiceImpl implements TestDataService {

    @Autowired
    private TestDataRepository testDataRepository;

    @Autowired
    private TestDataServiceSearch testDataServiceSearch;



    @Override
    public TestData save(TestData testData) {
        return testDataRepository.save(testData);
    }

    @Override
    public void delete(TestData testData) {
        testDataRepository.delete(testData);
    }


    @Override
    @Transactional(readOnly=true)
    public TestData findTestDataById(Long id) {
        return testDataRepository.findOne(id);
    }


    @Override
    @Transactional(readOnly=true)
    public Page<TestData> findTestDataAllByPage(Integer page, Integer limit) {
        if ( page == null || limit == null || !(page>0 && limit>0) )
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");

        return testDataRepository.findAllByOrderByIdAsc(new PageRequest(page-1, limit));
    }


    @Override
    @Transactional(readOnly=true)
    public Page<TestData> findTestDataByNameAndEmail(String name, String email, Integer page, Integer limit) {
        if (name == null || email == null)
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");
        if ( page == null || limit == null || !(page>0 && limit>0) )
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");

        return testDataRepository.findTestDataByNameAndEmail(name, email, new PageRequest(page-1, limit));
    }


    @Override
    @Transactional(readOnly=true)
    public PageReturnFormat<TestData> criteriaSearchTestData(Integer page , Integer limit, String[] searchParams,
                                                           String[] sortByParams, String phone, List<String> fieldnamesAvaliable,
                                                              Class ClassV) throws BadRequestException {

        PageReturnFormat<TestData> pageReturnFormat = testDataServiceSearch.criteriaSearchTestData(
                page , limit, searchParams, sortByParams, phone, fieldnamesAvaliable, TestData.class);

        return pageReturnFormat;
    }


}