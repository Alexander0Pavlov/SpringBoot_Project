package src.main.java.hello;

import src.main.java.hello.exceptions.NotFoundException;
import src.main.java.hello.exceptions.BadRequestException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import com.google.common.collect.Lists;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

import javax.validation.Valid;



@Controller
public class TestDataController {

    @Autowired
    private TestDataService testDataService;


    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i 'http://localhost:8080/testdata/find/all?l=4'
    @RequestMapping(value = "/testdata/find/all", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<TestData> findTestDataAllByPage(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit) {
        if (!(page > 0 && limit > 0)) throw new BadRequestException("BAD_REQUEST");

        return testDataService.findTestDataAllByPage(page, limit);
    }


    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i 'http://localhost:8080/testdata?search=name,~,сАш,lastmodifieddate,=,2018.02.27&sort=id,asc,lastmodifieddate,asc&phone=12'
    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i 'http://localhost:8080/testdatasearch?l=30&p=1&search=name,~,ЙЁ'
    @RequestMapping(value = "/testdata", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public PageReturnFormat<TestData> criteriaSearchTestData(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "search", required = false, defaultValue = "") String[] searchParams,
            @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String[] sortByParams,
            @RequestParam(value = "phone", required = false, defaultValue = "") String phone)
            throws BadRequestException {

        if (!(page > 0 && limit > 0) || (searchParams.length % 3 != 0) || (sortByParams.length % 2 != 0))
            throw new BadRequestException("BAD_REQUEST");

        List<String> fieldnamesAvaliable = Lists.newArrayList("id", "name", "company", "lastModifiedDate");

        PageReturnFormat<TestData> pageReturnFormat = testDataService.criteriaSearchTestData(page, limit,
                searchParams, sortByParams, phone, fieldnamesAvaliable, TestData.class);

        return pageReturnFormat;
    }


    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i 'http://localhost:8080/testdata/find/byNameAndEmail?name=СаШа&email=bCd&p=1&l=2'
    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i 'http://localhost:8080/testdata/find/byNameAndEmail?name=Саша&email='
    @RequestMapping(value = "/testdata/find/byNameAndEmail", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<TestData> findTestDataByNameAndEmail(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "email", required = true) String email) {
        //при name==null MissingServletRequestParameterException ловится в общем перехватчике и выдаёт BAD_REQUEST

        if (!(page > 0 && limit > 0)) throw new BadRequestException("BAD_REQUEST");
        return testDataService.findTestDataByNameAndEmail(name, email, page, limit);
    }


    // curl -H "Content-Type: application/json; charset=UTF-8" -X POST -d '{"name":"Sasha", "company":"abcd"}' http://localhost:8080/testdata
    @RequestMapping(value = "/testdata", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public TestData create(@RequestBody @Valid TestData testData) throws BadRequestException {

        testDataService.save(testData);

        return testData;
    }


    //curl -H "Content-Type: application/json" -X PUT -d '{"name":"SashaUPD5", "company":"abcd", "id":5}' http://localhost:8080/testdata/5
    @RequestMapping(value = "/testdata/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public TestData update(@RequestBody @Valid TestData testData, @PathVariable Long id)
            throws NotFoundException, BadRequestException {

        TestData testDataSave = testDataService.findTestDataById(id);
        if (testDataSave == null) throw new NotFoundException("NOT_FOUND");

        // с @Valid проверки на нулл полей testData делает валидатор
        testDataSave.setName(testData.getName());
        testDataSave.setCompany(testData.getCompany());

        testDataService.save(testDataSave);
        return testDataSave;
    }

    //curl -H "Content-Type: application/json" -X GET http://localhost:8080/testdata/3
    @RequestMapping(value = "/testdata/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TestData findTestDataById(@PathVariable Long id) throws NotFoundException {

        TestData testData = testDataService.findTestDataById(id);
        if (testData == null) throw new NotFoundException("NOT_FOUND");

        return testData;
    }


    //curl -H "Content-Type: application/json" -X DELETE http://localhost:8080/testdata/6
    @RequestMapping(value = "/testdata/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Long id) throws NotFoundException {

        TestData testData = testDataService.findTestDataById(id);

        if (testData == null)
            throw new NotFoundException("NOT_FOUND");
        else testDataService.delete(testData);

    }


}