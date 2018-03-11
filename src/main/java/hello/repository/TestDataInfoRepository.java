package src.main.java.hello;

import org.springframework.data.repository.PagingAndSortingRepository;



import java.util.List;
import java.util.Set;

public interface TestDataInfoRepository extends PagingAndSortingRepository<TestDataInfo, Long> {

    List<TestDataInfo> findTestDataInfoByTestDataId(Long testdataId);

}