package src.main.java.crm;

import src.main.java.crm.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.google.common.collect.Lists;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.FileCopyUtils;
import java.io.OutputStream;
import java.net.URLEncoder;


@Service("uploadedFilesService")
@Repository
@Transactional
public class UploadedFilesServiceImpl implements UploadedFilesService {

    @Autowired
    private UploadedFilesRepository uploadedFilesRepository;
    @Autowired
    private UploadedFilesBlobRepository uploadedFilesBlobRepository;
    @Autowired
    private ClientsService clientsService;



    @Override
    public UploadedFiles save(MultipartFile file, Long clientsId) {

        Clients client = clientsService.findClientsById(clientsId);
        if (client == null)
            throw new NotFoundException("NOT_FOUND");

        if (file.getSize() > 20 * 1024 * 1024)
            throw new BadRequestException("BAD_REQUEST");
        System.out.println("Size checked");
        UploadedFiles uploadedFile = new UploadedFiles();

        if (!file.isEmpty()) {

            try {
                uploadedFile.setName(file.getOriginalFilename());
                uploadedFile.setContentLength(Long.valueOf(file.getSize()).intValue());
                uploadedFile.setContentType(file.getContentType());
                uploadedFile.setClients(client);
                uploadedFile = uploadedFilesRepository.save(uploadedFile);

                UploadedFilesBlob uploadedFilesBlob = new UploadedFilesBlob();
                byte[] blob = file.getBytes();
                uploadedFilesBlob.setBlob(blob);
                uploadedFilesBlob.setUploadedFiles(uploadedFile);
                uploadedFilesBlob = uploadedFilesBlobRepository.save(uploadedFilesBlob);

            } catch (Exception e) {
                throw new BadRequestException("BAD_REQUEST");
            }
        }
        return uploadedFile;
    }



    @Override
    @Transactional(readOnly = true)
    public Page<UploadedFiles> fileInfoAllByPage(Integer page, Integer limit) {
        return uploadedFilesRepository.findAllByOrderByIdAsc(new PageRequest(page - 1, limit));
    }


    @Override
    @Transactional(readOnly = true)
    public Page<UploadedFiles> findUploadedFilesByClientsId(Long clientsId, Integer page, Integer limit) {
        if (!(page > 0 && limit > 0))
            throw new BadRequestException("BAD_REQUEST");

        if (clientsService.findClientsById(clientsId) == null)
            throw new NotFoundException("NOT_FOUND");

        return uploadedFilesRepository.findUploadedFilesByClientsIdOrderByIdAsc(clientsId, new PageRequest(page - 1, limit));
    }


    @Override
    @Transactional(readOnly = true)
    public void downloadFile(Long id, HttpServletRequest request, HttpServletResponse response) {
        if (!(id > 0))
            throw new BadRequestException("BAD_REQUEST");
        UploadedFiles uploadedFile = uploadedFilesRepository.findOne(id);
        if (uploadedFile == null)
            throw new NotFoundException("NOT_FOUND");
        UploadedFilesBlob uploadedFileBlob = uploadedFilesBlobRepository.findUploadedFilesBlobByUploadedFilesId(id);
        if (uploadedFileBlob == null)
            throw new NotFoundException("NOT_FOUND");

        try {
            if (uploadedFile.getContentType().contains("image"))
                response.setHeader("Content-Disposition", "inline;filename=\"" + URLEncoder.encode(uploadedFile.getName(), "utf-8") + "\";");
            else
                response.setHeader("Content-Disposition", "attachment;filename=\"" + URLEncoder.encode(uploadedFile.getName(), "utf-8") + "\";");
        } catch (Exception e) {
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");
        }

        try {
            OutputStream out = response.getOutputStream();
            response.setContentType(uploadedFile.getContentType());
            response.setContentLength(uploadedFile.getContentLength());
            FileCopyUtils.copy(uploadedFileBlob.getBlob(), out);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR");
        }
    }



    @Override
    public void deleteFile(Long id) {
        if (!(id > 0))
            throw new BadRequestException("BAD_REQUEST");

        UploadedFilesBlob uploadedFileBlob = uploadedFilesBlobRepository.findUploadedFilesBlobByUploadedFilesId(id);
        if (uploadedFileBlob == null)
            throw new NotFoundException("NOT_FOUND");

        uploadedFilesBlobRepository.delete(uploadedFileBlob);
        //orphanRemoval=true -> связанная запись с инфо о файле в UploadedFiles будет удалёна.
    }


    @Override
    @Transactional(readOnly = true)
    public UploadedFiles getFileInfo(Long id){
        if (!(id > 0))
            throw new BadRequestException("BAD_REQUEST");

        UploadedFiles uploadedFile = uploadedFilesRepository.findOne(id);
        if (uploadedFile == null)
            throw new NotFoundException("NOT_FOUND");
        return uploadedFile;
    }

}



