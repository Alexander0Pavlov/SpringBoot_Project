package src.main.java.crm;



import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//  для @ManyToOne на один из параметров, чтобы не было стек оверфлоу, когда пытается инициализировать переменные
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.envers.Audited;


import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//^3 для "меточек" времени и их вывода
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Temporal; // аннотация, что конвертит Date в TIMESTAMP БД
import javax.persistence.TemporalType; // ENUM для @Temporal , может быть DATE , TIME , TIMESTAMP
//^3

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name="clientsinfo")
public class ClientsInfo {

    private Long id;
    @NotNull
    @Size(min = 0, max = 255)
    private String phone;
    @NotNull
    @Size(min = 0, max = 255)
    private String email;
    private Clients clients;

    //^^^^1 для отслеживания даты-времени модифицирования
    @JsonProperty(access = Access.READ_ONLY)
    @LastModifiedBy
    private String lastModifiedBy;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    @LastModifiedDate
    private Date lastModifiedDate;
    //^^^^1


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "phone")
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Column(name = "email")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @ManyToOne
    @JoinColumn(name = "testdata_id")
    @JsonIgnore
    public Clients getClients() {
        return this.clients;
    }

    public void setClients(Clients clients) {
        this.clients = clients;
    }


    //^^^^2 для отслеживания даты-времени модифицирования
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
    //^^^^2

}

