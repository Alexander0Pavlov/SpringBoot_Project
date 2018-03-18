package src.main.java.crm;

import src.main.java.crm.exceptions.BadRequestException;
import src.main.java.crm.exceptions.NotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.data.domain.Page;



@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/company/find/all
    @RequestMapping(value = "/company/find/all", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<Company> findAllByPage(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit) {
        if (!(page > 0 && limit > 0)) throw new BadRequestException("BAD_REQUEST");

        return companyService.findAllByPage(page, limit);
    }

    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -i http://localhost:8080/company/ --data '{"name":"Эрствак","phone":"2222","email":"alexander.erstvak@gmail.com","address":"2222","postAddress":"2222","legalAddress":"2222","inn":"2222","kpp":"2222"}'
    @RequestMapping(value = "/company", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Company create(@RequestBody @Valid Company company, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<ObjectError> err = bindingResult.getAllErrors();
        for (ObjectError er : err) System.out.println(er);
        throw new BadRequestException("BAD_REQUEST");}

        companyService.save(company);

        return company;
    }

    //curl -X PUT -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/company/1 --data '{"name":"ЗЭНКО","phone":"111","email":"alexander.erstvak@gmail.com","address":"2222","postAddress":"2222","legalAddress":"2222","inn":"2222","kpp":"2222"}'
    @RequestMapping(value = "/company/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Company update(@RequestBody @Valid Company company, @PathVariable Long id) {

        Company companySave = companyService.findById(id);
        if (companySave == null) throw new NotFoundException("NOT_FOUND");

        // с @Valid проверки на нулл полей company делает валидатор
        companySave.setName(company.getName());
        companySave.setPhone(company.getPhone());
        companySave.setEmail(company.getEmail());
        companySave.setAddress(company.getAddress());
        companySave.setPostAddress(company.getPostAddress());
        companySave.setLegalAddress(company.getLegalAddress());
        companySave.setInn(company.getInn());
        companySave.setKpp(company.getKpp());

        companyService.save(companySave);

        return companySave;
    }


    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/company/1
    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Company findCompanyById(@PathVariable Long id) throws NotFoundException {

        Company company = companyService.findById(id);
        if (company == null) throw new NotFoundException("NOT_FOUND");

        return company;
    }


    //curl -X DELETE -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/company/3
    @RequestMapping(value = "/company/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Long id) throws NotFoundException {

        Company company = companyService.findById(id);

        if (company == null)
            throw new NotFoundException("NOT_FOUND");
        else companyService.delete(company);

    }

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i 'http://localhost:8080/company?p=1&l=5&search=name,~,зЭн,email,~,erst,lastModifiedDate,=,2018.03.18,inn,=,2222&sort=id,asc'
    @RequestMapping(value = "/company", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public PageReturnFormat<Company> criteriaSearchClients(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "search", required = false, defaultValue = "") String[] searchParams,
            @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String[] sortByParams)
            throws BadRequestException {

        if (!(page > 0 && limit > 0) || (searchParams.length % 3 != 0) || (sortByParams.length % 2 != 0))
            throw new BadRequestException("BAD_REQUEST");

        List<String> fieldnamesAvaliable = Lists.newArrayList("id", "name", "phone", "email","inn","kpp", "lastModifiedDate");

        PageReturnFormat<Company> pageReturnFormat = companyService.criteriaSearchCompany(page, limit,
                searchParams, sortByParams, null, fieldnamesAvaliable, Company.class);

        return pageReturnFormat;
    }

}