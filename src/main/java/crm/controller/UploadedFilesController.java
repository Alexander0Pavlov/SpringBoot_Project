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

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class UploadedFilesController {


    @Autowired
    private UploadedFilesService uploadedFilesService;



    //http://localhost:8080/
    @RequestMapping(value = "/uploadedfiles", method = RequestMethod.POST,
             produces = "application/json; charset=UTF-8")
    @ResponseBody
    public UploadedFiles uploadFile(@RequestParam("file") MultipartFile file,
                                 @RequestParam("clientsId") Long clientsId) {

        return uploadedFilesService.save(file, clientsId);
    }



    @RequestMapping(value = "/uploadedfiles/{id}", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public UploadedFiles getFileInfo(@PathVariable Long id) {

        return uploadedFilesService.getFileInfo(id);
    }



    @RequestMapping(value = "/uploadedfiles/find/all", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<UploadedFiles> fileInfoAllByPage(
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit) {
        if (!(page > 0 && limit > 0))
            throw new BadRequestException("BAD_REQUEST");

        return uploadedFilesService.fileInfoAllByPage(page, limit);
    }



    @RequestMapping(value = "clients/{clientsId}/uploadedfiles", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Page<UploadedFiles> fileInfoByClientsIs(
            @PathVariable Long clientsId,
            @RequestParam(value = "p", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "l", required = false, defaultValue = "10") Integer limit) {
        if (!(page > 0 && limit > 0))
            throw new BadRequestException("BAD_REQUEST");

        return uploadedFilesService.findUploadedFilesByClientsId(clientsId, page, limit);
    }



    //http://localhost:8080/uploadedfiles/blob/3
    @RequestMapping(value = "uploadedfiles/blob/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {

        uploadedFilesService.downloadFile(id, request, response);
    }



    //curl -X DELETE -H 'Content-Type: application/json; charset=UTF-8' -H 'Accept: application/json; charset=UTF-8' -H 'Authorization: Basic VGhlT25lOlBjdw==' -i http://localhost:8080/uploadedfiles/11
    @RequestMapping(value = "/uploadedfiles/{id}", method = RequestMethod.DELETE,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public void deleteFile(@PathVariable Long id) {

        uploadedFilesService.deleteFile(id);
    }


}