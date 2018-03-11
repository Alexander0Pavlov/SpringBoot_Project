package src.main.java.hello;

import src.main.java.hello.exceptions.BadRequestException;
import src.main.java.hello.exceptions.NotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Set;

import javax.validation.Valid;

@Controller
public class TestDataInfoController {

    @Autowired
    private TestDataInfoService testDataInfoService;
    @Autowired
    private TestDataService testDataService;


    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i http://localhost:8080/testdata/1/info --data '{"phone":"129","email":"bcd"}'
    @RequestMapping(value = "/testdata/{id}/info", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public TestDataInfo create(@RequestBody @Valid TestDataInfo testDataInfo, @PathVariable Long id) throws BadRequestException {

        TestData testData = testDataService.findTestDataById(id);
        if (testData == null) throw new BadRequestException("BAD_REQUEST");

        testDataInfo.setTestData(testData);

        testDataInfoService.save(testDataInfo);

        return testDataInfo;
    }

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i http://localhost:8080/testdata/1/info
    @RequestMapping(value = "/testdata/{id}/info", method = RequestMethod.GET)
    @ResponseBody
    public TestDataInfoList listAllTestDataInfoByTestDataId(@PathVariable Long id) {

        TestDataInfoList testDataInfoList = new TestDataInfoList(testDataInfoService.findTestDataInfoByTestDataId(id));

        return testDataInfoList;
    }

    //curl -X DELETE -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i http://localhost:8080/testdata/1/info/8
    @RequestMapping(value = "/testdata/{id}/info/{infoId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Long id, @PathVariable Long infoId) throws NotFoundException, BadRequestException {

        TestDataInfo testDataInfo = testDataInfoService.findTestDataInfoById(infoId);
        if (testDataInfo == null) throw new NotFoundException("NOT_FOUND");

        if (testDataInfo.getTestData() != testDataService.findTestDataById(id))
            throw new BadRequestException("BAD_REQUEST");
        // чекаем на то, удаляем ли мы инфо(infoId) принадлежащее именно TestData {id}

        testDataInfoService.delete(testDataInfo);
    }

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i http://localhost:8080/testdata/2/info/9
    @RequestMapping(value = "/testdata/{id}/info/{infoId}", method = RequestMethod.GET)
    @ResponseBody
    public TestDataInfo findTestDataInfoById(@PathVariable Long id, @PathVariable Long infoId)
            throws NotFoundException, BadRequestException {

        TestDataInfo testDataInfo = testDataInfoService.findTestDataInfoById(infoId);
        if (testDataInfo == null) throw new NotFoundException("NOT_FOUND");

        if (testDataInfo.getTestData() != testDataService.findTestDataById(id))
            throw new BadRequestException("BAD_REQUEST");
        // чекаем на то, принадлежит ли инфо(infoId)  именно TestData {id}
        return testDataInfo;
    }


    //curl -X PUT -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i http://localhost:8080/testdata/1/info/6 --data '{"id":6,"phone":"129","email":"bcdefg"}'
    @RequestMapping(value = "/testdata/{id}/info/{infoId}", method = RequestMethod.PUT)
    @ResponseBody
    public TestDataInfo update(@RequestBody @Valid TestDataInfo testDataInfo, @PathVariable Long id, @PathVariable Long infoId)
            throws NotFoundException, BadRequestException {


        TestDataInfo testDataInfoSave = testDataInfoService.findTestDataInfoById(infoId);
        if (testDataInfoSave == null)
            throw new NotFoundException("NOT_FOUND");
        if (testDataInfoSave.getTestData() != testDataService.findTestDataById(id))
            throw new BadRequestException("BAD_REQUEST");

        // с @Valid проверки на нулл полей testDataInfo делает валидатор
        testDataInfoSave.setPhone(testDataInfo.getPhone());
        testDataInfoSave.setEmail(testDataInfo.getEmail());

        testDataInfoService.save(testDataInfoSave);

        return testDataInfoSave;


    }

}