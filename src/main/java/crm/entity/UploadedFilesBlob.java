package src.main.java.crm;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.OneToOne;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access; // READ_ONLY , READ_WRITE, WRITE_ONLY


import java.util.Date;
import javax.persistence.Temporal; // аннотация, что конвертит Date в TIMESTAMP БД
import javax.persistence.TemporalType; // ENUM для @Temporal , может быть DATE , TIME , TIMESTAMP
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import org.hibernate.envers.Audited;

import org.hibernate.annotations.Type;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;


@Audited
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="uploaded_files_blob")
public class UploadedFilesBlob {

    private Long id;
    @NotNull
    private byte[] blob;
    @NotNull
    private UploadedFiles uploadedFiles;



    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name="blob")
    @Type(type="org.hibernate.type.BinaryType")
    @JsonIgnore
    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval=true)
    @JoinColumn(name="uploaded_file_id")
    public UploadedFiles getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(UploadedFiles uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
}