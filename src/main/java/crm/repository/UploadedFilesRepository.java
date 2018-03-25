package src.main.java.crm;

import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UploadedFilesRepository extends PagingAndSortingRepository<UploadedFiles, Long> {

    Page<UploadedFiles> findAllByOrderByIdAsc(Pageable pageable);

    Page<UploadedFiles> findUploadedFilesByClientsIdOrderByIdAsc(Long clientsId, Pageable pageable);


}