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


import java.util.List;
import java.util.Set;

import javax.validation.Valid;


@Controller
public class ClientsInfoController {

    @Autowired
    private ClientsInfoService clientsInfoService;
    @Autowired
    private ClientsService clientsService;


    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clients/1/clientsinfo --data '{"phone":"1111", "email":"macrooptica.snab@gmail.com"}'
    @RequestMapping(value = "/clients/{clientsId}/clientsinfo", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8",
            produces = "application/json; charset=UTF-8")
    @ResponseBody
    public ClientsInfo create(@RequestBody @Valid ClientsInfo clientsInfo, @PathVariable Long clientsId) throws BadRequestException {

        Clients clients = clientsService.findClientsById(clientsId);
        if (clients == null) throw new BadRequestException("BAD_REQUEST");

        clientsInfo.setClients(clients);

        clientsInfoService.save(clientsInfo);

        return clientsInfo;
    }

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clientsinfo/1
    @RequestMapping(value = "/clients/{clientsId}/clientsinfo", method = RequestMethod.GET)
    @ResponseBody
    public ClientsInfoList listAllClientsInfoByClientsId(@PathVariable Long clientsId) {

        ClientsInfoList clientsInfoList = new ClientsInfoList(clientsInfoService.findClientsInfoByClientsId(clientsId));

        return clientsInfoList;
    }

    //curl -X DELETE -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clientsinfo/2
    @RequestMapping(value = "/clientsinfo/{infoId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(/*@PathVariable Long id,*/ @PathVariable Long infoId) throws NotFoundException, BadRequestException {

        ClientsInfo clientsInfo = clientsInfoService.findClientsInfoById(infoId);
        if (clientsInfo == null) throw new NotFoundException("NOT_FOUND");

        clientsInfoService.delete(clientsInfo);
    }

    //curl -X GET -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clientsinfo/2
    @RequestMapping(value = "/clientsinfo/{infoId}", method = RequestMethod.GET)
    @ResponseBody
    public ClientsInfo findClientsInfoById(/*@PathVariable Long id,*/ @PathVariable Long infoId)
            throws NotFoundException, BadRequestException {

        ClientsInfo clientsInfo = clientsInfoService.findClientsInfoById(infoId);
        if (clientsInfo == null) throw new NotFoundException("NOT_FOUND");

        return clientsInfo;
    }


    //curl -X PUT -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/clientsinfo/2 --data '{"phone":"4444", "email":"macrooptica.snab@gmail.com"}'
    @RequestMapping(value = "/clientsinfo/{infoId}", method = RequestMethod.PUT)
    @ResponseBody
    public ClientsInfo update(@RequestBody @Valid ClientsInfo clientsInfo,/* @PathVariable Long id,*/ @PathVariable Long infoId)
            throws NotFoundException, BadRequestException {


        ClientsInfo clientsInfoSave = clientsInfoService.findClientsInfoById(infoId);
        if (clientsInfoSave == null)
            throw new NotFoundException("NOT_FOUND");

        // с @Valid проверки на нулл полей clientsInfo делает валидатор
        clientsInfoSave.setPhone(clientsInfo.getPhone());
        clientsInfoSave.setEmail(clientsInfo.getEmail());

        clientsInfoService.save(clientsInfoSave);

        return clientsInfoSave;


    }

}