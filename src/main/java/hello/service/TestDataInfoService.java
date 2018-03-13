package src.main.java.hello;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(HasRole.DBA_URW)
public interface TestDataInfoService {

    public TestDataInfo save(TestDataInfo testDataInfo);

    public void delete(TestDataInfo testDataInfo);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public TestDataInfo findTestDataInfoById(Long id);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public List<TestDataInfo> findTestDataInfoByTestDataId(Long testdataid);
}