package src.main.java.crm;

import src.main.java.crm.exceptions.NotFoundException;
import src.main.java.crm.exceptions.BadRequestException;


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
public class ClientsController {

    @Autowired
    private ClientsService clientsService;
    @Autowired
    private CompanyService companyService;

    
    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clients/find/all
    @RequestMapping(value = "/clients/find/all", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<Clients> findClientsAllByPage(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit) {
        if (!(page > 0 && limit > 0)) throw new BadRequestException("BAD_REQUEST");

        return clientsService.findClientsAllByPage(page, limit);
    }


    
    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i 'http://localhost:8080/clients?search=name,~,sA,surname,~,paV,lastmodifieddate,=,2018.03.18&sort=id,asc,lastmodifieddate,asc&phone=11&p=1&l=20'
    @RequestMapping(value = "/clients", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public PageReturnFormat<Clients> criteriaSearchClients(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "search", required = false, defaultValue = "") String[] searchParams,
            @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String[] sortByParams,
            @RequestParam(value = "phone", required = false, defaultValue = "") String phone)
            throws BadRequestException {

        if (!(page > 0 && limit > 0) || (searchParams.length % 3 != 0) || (sortByParams.length % 2 != 0))
            throw new BadRequestException("BAD_REQUEST");

        List<String> fieldnamesAvaliable = Lists.newArrayList("id", "name", "surname", "lastModifiedDate");

        PageReturnFormat<Clients> pageReturnFormat = clientsService.criteriaSearchClients(page, limit,
                searchParams, sortByParams, phone, fieldnamesAvaliable, Clients.class);

        return pageReturnFormat;
    }




    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i 'http://localhost:8080/clients/find/byNameAndEmail?name=Sasha&email=mac'
    @RequestMapping(value = "/clients/find/byNameAndEmail", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<Clients> findClientsByNameAndEmail(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit,
            @RequestParam(value = "name", required = true) String name,
            @RequestParam(value = "email", required = true) String email) {
        //при name==null MissingServletRequestParameterException ловится в общем перехватчике и выдаёт BAD_REQUEST

        if (!(page > 0 && limit > 0)) throw new BadRequestException("BAD_REQUEST");
        return clientsService.findClientsByNameAndEmail(name, email, page, limit);
    }


    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/company/1/clients --data '{"name":"Alex", "surname":"PAV"}'
    @RequestMapping(value = "/company/{companyId}/clients", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Clients create(@RequestBody @Valid Clients clients, @PathVariable Long companyId) throws BadRequestException {

        // добавляем привязку к компании
        Company company = companyService.findById(companyId);
        if (company == null) throw new BadRequestException("BAD_REQUEST");
        clients.setCompany(company);

        clientsService.save(clients);

        return clients;
    }


    //curl -X PUT -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clients/2 --data '{"name":"Alex", "surname":"PAV."}'
    @RequestMapping(value = "/clients/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Clients update(@RequestBody @Valid Clients clients, @PathVariable Long id)
            throws NotFoundException, BadRequestException {

        Clients clientsSave = clientsService.findClientsById(id);
        if (clientsSave == null) throw new NotFoundException("NOT_FOUND");

        // с @Valid проверки на нулл полей clients делает валидатор
        clientsSave.setName(clients.getName());
        clientsSave.setSurname(clients.getSurname());

        clientsService.save(clientsSave);
        return clientsSave;
    }


    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clients/1
    @RequestMapping(value = "/clients/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Clients findClientsById(@PathVariable Long id) throws NotFoundException {

        Clients clients = clientsService.findClientsById(id);
        if (clients == null) throw new NotFoundException("NOT_FOUND");

        return clients;
    }


    //curl -X DELETE -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clients/3
    @RequestMapping(value = "/clients/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Long id) throws NotFoundException {

        Clients clients = clientsService.findClientsById(id);

        if (clients == null)
            throw new NotFoundException("NOT_FOUND");
        else clientsService.delete(clients);

    }

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/company/1/clients
    @RequestMapping(value = "/company/{companyId}/clients", method = RequestMethod.GET)
    @ResponseBody
    public ClientsList listAllClientsByCompanyId(@PathVariable Long companyId) {

        ClientsList clientsList = new ClientsList(clientsService.findByCompanyId(companyId));

        return clientsList;
    }

}