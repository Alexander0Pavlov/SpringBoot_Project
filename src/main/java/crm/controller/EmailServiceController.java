package src.main.java.crm;

import src.main.java.crm.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.*;



@Controller
public class EmailServiceController {

    @Autowired
    EmailServiceImpl emailServiceImpl;

    @Autowired
    CompanyService companyService;


    //curl -X POST -H 'Accept: application/json; charset=UTF-8' -H 'Content-Type: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/email/send --data '{"to":"alexpavlovspring@gmail.com","subject":"Hi form Spring","text":"Hello Sasha"}'
    @RequestMapping(value = "/email/send", method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public EmailMsgToOne sendEmailByEmailSubjectText(@RequestBody @Valid EmailMsgToOne emailMsgToOne) {

        emailServiceImpl.sendWithoutAttachment(emailMsgToOne.getTo(),
                emailMsgToOne.getSubject(), emailMsgToOne.getText());

        return emailMsgToOne;
    }


    //curl -X POST -H 'Accept: application/json; charset=UTF-8' -H 'Content-Type: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/email/send/asHtmlText --data '{"to":"alexpavlovspring@gmail.com","subject":"Spring Котик","text":"<html><body><p>Текст над котиком.</p><img src='\''https://vk.com/images/stickers/75/128.png'\''><p>Текст под котиком.</p></body></html>"}'
    //curl -X POST -H 'Accept: application/json; charset=UTF-8' -H 'Content-Type: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/email/send/asHtmlText --data '{"to":"alexpavlovspring@gmail.com","subject":"Spring Котик","text":"<html><head><meta charset=\"utf-8\"></head><body><p>Текст над котиком.</p><img src='\''https://vk.com/images/stickers/75/128.png'\''><p>Текст под котиком.</p></body></html>"}'
    @RequestMapping(value = "/email/send/asHtmlText", method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public EmailMsgToOne sendEmailAsHtmlText(@RequestBody @Valid EmailMsgToOne emailMsgToOne) {

        emailServiceImpl.sendWithAttachmentInside(emailMsgToOne.getTo(),
                emailMsgToOne.getSubject(), emailMsgToOne.getText(), true);

        return emailMsgToOne;
    }


    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/email/send/asHtmlText/byCompanyList --data '{"companyIdList":[1,2],"subject":"Spring Котик","text":"<html><body><p>Текст над котиком.</p><img src='\''https://vk.com/images/stickers/75/128.png'\''><p>Текст под котиком.</p></body></html>"}'
    @RequestMapping(value = "/email/send/asHtmlText/byCompanyList", method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public EmailMsgToCompanyIdList sendEmailAsHtmlText(@RequestBody @Valid EmailMsgToCompanyIdList emailMsgToCompanyIdList) {

        Set<String> emailSet = new TreeSet<String> (
                companyService.selectCompanyEmailsByCompanyIdList(emailMsgToCompanyIdList.getCompanyIdList()) );
        emailSet.addAll(
                companyService.selectClientsEmailsByCompanyIdList(emailMsgToCompanyIdList.getCompanyIdList()));

        emailSet.forEach(System.out::println);


        emailSet.forEach(i -> emailServiceImpl.sendWithAttachmentInside(i,
                emailMsgToCompanyIdList.getSubject(),
                emailMsgToCompanyIdList.getText(), true) );


        return emailMsgToCompanyIdList;
    }


    //curl -X POST -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VVJXOlBjdw==' -i http://localhost:8080/email/send/asHtmlText/byCompanyList --data '{"companyIdList":[1,2],"subject":"Spring Котик","text":"<html><body><p>Текст над котиком.</p><img src='\''https://vk.com/images/stickers/75/128.png'\''><p>Текст под котиком.</p></body></html>"}'
    @RequestMapping(value = "/email/send/asHtmlText/toAllCompanies", method = RequestMethod.POST,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public EmailMsg sendEmailAsHtmlText(@RequestBody @Valid EmailMsg emailMsg) {

        Set<String> emailSet = new TreeSet<String>(companyService.selectCompanyEmails());
        emailSet.addAll(companyService.selectClientsEmails());

        emailSet.forEach(System.out::println);


        emailSet.forEach(i -> emailServiceImpl.sendWithAttachmentInside(i,
                emailMsg.getSubject(),
                emailMsg.getText(), true));


        return emailMsg;
    }

}