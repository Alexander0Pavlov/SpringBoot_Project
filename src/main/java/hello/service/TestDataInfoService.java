package src.main.java.hello;

import java.util.List;
import java.util.Set;

public interface TestDataInfoService {

    public TestDataInfo save(TestDataInfo testDataInfo);

    public void delete(TestDataInfo testDataInfo);

    public TestDataInfo findTestDataInfoById(Long id);

    public List<TestDataInfo> findTestDataInfoByTestDataId(Long testdataid);
}