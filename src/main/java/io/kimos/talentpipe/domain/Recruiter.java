package io.kimos.talentpipe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Recruiter.
 */
@Entity
@Table(name = "recruiter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "recruiter")
public class Recruiter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "tax_id", nullable = false)
    private String taxId;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "phone_prefix", nullable = false)
    private String phonePrefix;

    @Column(name = "street")
    private String street;

    @Column(name = "jhi_number")
    private Integer number;

    @Min(value = 0)
    @Max(value = 400)
    @Column(name = "floor")
    private Integer floor;

    @Column(name = "apartment")
    private String apartment;

    @ManyToOne()
    @JsonIgnoreProperties("")
    private City city;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Sector sector;

    @OneToOne(optional = false)
    @JsonIgnore
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Recruiter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public Recruiter lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Recruiter email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaxId() {
        return taxId;
    }

    public Recruiter taxId(String taxId) {
        this.taxId = taxId;
        return this;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Recruiter phone(String phone) {
        this.phoneNumber = phone;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public Recruiter street(String street) {
        this.street = street;
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public Recruiter number(Integer number) {
        this.number = number;
        return this;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getFloor() {
        return floor;
    }

    public Recruiter floor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getApartment() {
        return apartment;
    }

    public Recruiter apartment(String apartment) {
        this.apartment = apartment;
        return this;
    }

    public Recruiter phonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
        return this;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }

    public void setPhonePrefix(String phonePrefix) {
        this.phonePrefix = phonePrefix;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public City getCity() {
        return city;
    }

    public Recruiter city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Sector getSector() {
        return sector;
    }

    public Recruiter sector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recruiter recruiter = (Recruiter) o;
        if (recruiter.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recruiter.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Recruiter{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", taxId='" + getTaxId() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", street='" + getStreet() + "'" +
            ", number=" + getNumber() +
            ", floor=" + getFloor() +
            ", apartment='" + getApartment() + "'" +
            "}";
    }
}
