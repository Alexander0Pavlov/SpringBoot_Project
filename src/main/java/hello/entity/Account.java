package src.main.java.hello;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


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



@Audited
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="account")
public class Account {

    private Long id;
    @NotNull
    @Size(min=2, max=255)
    private String username;
    @NotNull
    @Size(min=3, max=255)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;
    @NotNull
    private boolean isEnabled;
    @NotNull
    private String role;

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

    public Account(){}

    public Account(String username, String password, boolean isEnabled, String role) {
        this.username = username;
        this.password = password;
        this.isEnabled = isEnabled;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Column(name = "isEnabled")
    public boolean getIsEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }


    @Column(name = "role")
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }



    //^^^^3 для отслеживания даты-времени модифицирования
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


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