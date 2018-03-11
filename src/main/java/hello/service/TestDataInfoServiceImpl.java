package src.main.java.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service("testDataInfoService")
@Repository
@Transactional
public class TestDataInfoServiceImpl implements TestDataInfoService {

    @Autowired
    private TestDataInfoRepository testDataInfoRepository;


    @Override
    public TestDataInfo save(TestDataInfo testDataInfo) {
        return testDataInfoRepository.save(testDataInfo);
    }

    @Override
    public void delete(TestDataInfo testDataInfo) {
        testDataInfoRepository.delete(testDataInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public TestDataInfo findTestDataInfoById(Long id) {
        return testDataInfoRepository.findOne(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<TestDataInfo> findTestDataInfoByTestDataId(Long testdataId) {
        return testDataInfoRepository.findTestDataInfoByTestDataId(testdataId);
    }

}