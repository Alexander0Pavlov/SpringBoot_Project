package src.main.java.crm;



import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

//  для @ManyToOne на один из параметров, чтобы не было стек оверфлоу, когда пытается инициализировать переменные
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//^3 для "меточек" времени и их вывода
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.*;
import javax.persistence.Temporal; // аннотация, что конвертит Date в TIMESTAMP БД
import javax.persistence.TemporalType; // ENUM для @Temporal , может быть DATE , TIME , TIMESTAMP
//^3

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Audited
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="company")
public class Company {

    private Long id;

    private Set<Clients> clients;

    @NotNull
    @Size(min = 0, max = 255)
    private String name;
    @NotNull
    @Size(min = 0, max = 255)
    private String phone;
    @NotNull
    @Size(min = 0, max = 255)
    private String email;
    @NotNull
    @Size(min = 0, max = 255)
    private String address;
    @NotNull
    @Size(min = 0, max = 255)
    private String postAddress;
    @NotNull
    @Size(min = 0, max = 255)
    private String legalAddress;
    @NotNull
    @Size(min = 0, max = 255)
    private String inn;
    @NotNull
    @Size(min = 0, max = 255)
    private String kpp;


    //^^^2 поля куда пишутся данные о создании/обновлении
    @JsonProperty(access = Access.READ_ONLY)
    @CreatedBy
    private String createdBy;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    @CreatedDate
    private Date createdDate;

    @JsonProperty(access = Access.READ_ONLY)
    @LastModifiedBy
    private String lastModifiedBy;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    @LastModifiedDate
    private Date lastModifiedDate;
    // ^^^2





    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    public Set<Clients> getClients() {
        return clients;
    }

    public void setClients(Set<Clients> clients) {
        this.clients = clients;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "postAddress")
    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    @Column(name = "legalAddress")
    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    @Column(name = "inn")
    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    @Column(name = "kpp")
    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }


    //^^^^3 для отслеживания даты-времени модифицирования
    //ALTER TABLE testdata ADD COLUMN created_by varchar(255);
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    //ALTER TABLE testdata ADD COLUMN created_date timestamp;
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "LAST_MODIFIED_BY")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Column(name = "LAST_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    //^^^^3


}
