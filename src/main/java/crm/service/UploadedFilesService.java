package src.main.java.crm;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PreAuthorize(HasRole.DBA_URW)
public interface UploadedFilesService {

    public UploadedFiles save(MultipartFile file, Long clientsId);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public Page<UploadedFiles> fileInfoAllByPage(Integer page, Integer limit);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public Page<UploadedFiles> findUploadedFilesByClientsId(Long clientsId, Integer page, Integer limit);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public void downloadFile(Long id, HttpServletRequest request, HttpServletResponse response);

    public void deleteFile(Long id);

    @PreAuthorize(HasRole.DBA_URW_UR)
    public UploadedFiles getFileInfo(Long id);

}