package src.main.java.crm;



import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;


import java.util.Date;

import javax.persistence.Temporal; // аннотация, что конвертит Date в TIMESTAMP БД
import javax.persistence.TemporalType; // ENUM для @Temporal , может быть DATE , TIME , TIMESTAMP

import com.fasterxml.jackson.annotation.JsonFormat; // работает с типом данных java.util.Date;
// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ") //https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html



// ^^^1 для прослушки сущности
// также для аудита важно не забыть файл конфигурации (JpaAuditingConfig.java)
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Transient; //не отображаем поле на БД

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
// ^^^1


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access; // READ_ONLY , READ_WRITE, WRITE_ONLY


import java.util.Set;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;



import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited; //просто не сохраняет поле в таблице аудита и не пишет новую ревизию, если изменения по этому полю.


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="clients")
public class Clients {


    private Long id;
    @NotNull
    @Size(min = 0, max = 255)
    private String name;

    @NotNull
    @Size(min = 0, max = 255)
    private String surname;

    private Company company;
    private Set<ClientsInfo> clientsInfoSet;


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
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    //@Column(name = "company")
    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


    @OneToMany(mappedBy = "clients", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @NotAudited
    public Set<ClientsInfo> getClientsInfoSet() {
        return clientsInfoSet;
    }

    public void setClientsInfoSet(Set<ClientsInfo> clientsInfoSet) {
        this.clientsInfoSet = clientsInfoSet;
    }

    public void addClientsInfo(ClientsInfo clientsInfo) {
        clientsInfo.setClients(this);
        getClientsInfoSet().add(clientsInfo);
    }

    public void removeClientsInfo(ClientsInfo clientsInfo) {
        getClientsInfoSet().remove(clientsInfo);
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


    @Override
    public String toString() {
        return id + " " + name + " " + company;
    }
}