package io.kimos.talentpipe.service.impl;

import io.kimos.talentpipe.domain.Company;
import io.kimos.talentpipe.domain.User;
import io.kimos.talentpipe.repository.CompanyRepository;
import io.kimos.talentpipe.repository.search.CompanySearchRepository;
import io.kimos.talentpipe.service.CompanyService;
import io.kimos.talentpipe.service.UserService;
import io.kimos.talentpipe.web.rest.errors.CompanyNotFoundException;
import io.kimos.talentpipe.web.rest.errors.UserNotFoundException;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Company.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    private final CompanySearchRepository companySearchRepository;

    private final UserService userService;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanySearchRepository companySearchRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.companySearchRepository = companySearchRepository;
        this.userService = userService;
    }

    /**
     * Save a company.
     *
     * @param company the entity to save
     * @return the persisted entity
     */
    @Override
    @Transactional
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        Company result = companyRepository.save(company.getId() == null ? prepareToCreate(company) : prepareToUpdate(company));
        companySearchRepository.save(result);
        return result;
    }

    private Company prepareToUpdate(Company company) {
        Company persistedCompany = this.findOne(company.getId()).orElseThrow(CompanyNotFoundException::new);
        persistedCompany.setCity(company.getCity());
        persistedCompany.setCompanyType(company.getCompanyType());
        persistedCompany.setSector(company.getSector());

        persistedCompany.setTaxName(company.getTaxName());
        persistedCompany.setTaxId(company.getTaxId());
        persistedCompany.setEmail(company.getEmail());
        persistedCompany.setPhoneNumber(company.getPhoneNumber());
        persistedCompany.setName(company.getName());
        persistedCompany.setStreet(company.getStreet());
        persistedCompany.setNumber(company.getNumber());
        persistedCompany.setFloor(company.getFloor());
        persistedCompany.setApartment(company.getApartment());
        persistedCompany.setPostalCode(company.getPostalCode());

        persistedCompany.setPhonePrefix(company.getPhonePrefix());
        persistedCompany.setPhoneNumber(company.getPhoneNumber());
        persistedCompany.setContactLastName(company.getContactLastName());
        persistedCompany.setContactName(company.getContactName());
        persistedCompany.setLastUpdateDate(Instant.now());
        return persistedCompany;
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable);
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        Optional<Company> company = companyRepository.findById(id);
        return initializeCompany(company);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
        companySearchRepository.deleteById(id);
    }

    /**
     * Search for the company corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Company> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Companies for query {}", query);
        return companySearchRepository.search(queryStringQuery(query), pageable);
    }

    @Override
    @Transactional
    public Company createCompany(Company company, String password) {
        User user = new User();
        user.setAcceptTermsOfService(true);
        user.setLogin(company.getEmail());
        user.setFirstName(company.getContactName());
        user.setLastName(company.getContactLastName());
        user.setEmail(company.getEmail());
        user.setPassword(password);
        user = userService.registerCompanyUserAdministrator(user);
        company.setMainUser(user);
        return this.save(company);
    }

    @Override
    @Transactional
    public Optional<Company> findByCurrentUserLogin(String login) {
        Optional<Company> company = companyRepository.findCompanyByMainUser_Login(login);
        return initializeCompany(company);
    }

    @NotNull
    @Transactional
    private Optional<Company> initializeCompany(Optional<Company> company) {
        if(company.isPresent()) {
            Hibernate.initialize(company.get().getSector());
            Hibernate.initialize(company.get().getMainUser());
            Hibernate.initialize(company.get().getCity());
            Hibernate.initialize(company.get().getCompanyType());
        }
        return company;
    }

    private Company prepareToCreate(Company company) {
        company.setCreationDate(Instant.now());
        company.setLastUpdateDate(Instant.now());
        return company;
    }
}
