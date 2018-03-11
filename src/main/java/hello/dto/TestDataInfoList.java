package src.main.java.hello;

import java.util.List;

public class TestDataInfoList {
    private List<TestDataInfo> testDataInfoList;

    public TestDataInfoList() {
    }

    public TestDataInfoList(List<TestDataInfo> testDataInfoList) {
        this.testDataInfoList = testDataInfoList;
    }


    public void setTestDataInfoList(List<TestDataInfo> testDataInfoList) {
        this.testDataInfoList = testDataInfoList;
    }

    public List<TestDataInfo> getTestDataInfoList() {
        return testDataInfoList;
    }

}