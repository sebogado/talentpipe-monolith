package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.City;
import io.kimos.talentpipe.service.CityService;
import io.kimos.talentpipe.web.rest.dto.CreateCityDTO;
import io.kimos.talentpipe.web.rest.dto.UpdateCityDTO;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.util.HeaderUtil;
import io.kimos.talentpipe.web.rest.util.PaginationUtil;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing City.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    private final CityService cityService;

    private final MapperFacade orikaMapper;

    public CityResource(CityService cityService, MapperFacade orikaMapper) {
        this.cityService = cityService;
        this.orikaMapper = orikaMapper;
    }

    /**
     * POST  /cities : Create a new city.
     *
     * @param city the city to create
     * @return the ResponseEntity with status 201 (Created) and with body the new city, or with status 400 (Bad Request) if the city has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cities")
    @Timed
    public ResponseEntity<City> createCity(@Valid @RequestBody CreateCityDTO city) throws URISyntaxException {
        log.debug("REST request to save City : {}", city);
        City result = cityService.save(orikaMapper.map(city, City.class));
        return ResponseEntity.created(new URI("/api/cities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cities : Updates an existing city.
     *
     * @param city the city to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated city,
     * or with status 400 (Bad Request) if the city is not valid,
     * or with status 500 (Internal Server Error) if the city couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cities")
    @Timed
    public ResponseEntity<City> updateCity(@Valid @RequestBody UpdateCityDTO city) throws URISyntaxException {
        log.debug("REST request to update City : {}", city);
        if (city.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        City result = cityService.save(orikaMapper.map(city, City.class));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, city.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cities : get all the cities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cities in body
     */
    @GetMapping("/cities")
    @Timed
    public ResponseEntity<List<City>> getAllCities(Pageable pageable) {
        log.debug("REST request to get a page of Cities");
        Page<City> page = cityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cities/:id : get the "id" city.
     *
     * @param id the id of the city to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the city, or with status 404 (Not Found)
     */
    @GetMapping("/cities/{id}")
    @Timed
    public ResponseEntity<City> getCity(@PathVariable Long id) {
        log.debug("REST request to get City : {}", id);
        Optional<City> city = cityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(city);
    }

    /**
     * DELETE  /cities/:id : delete the "id" city.
     *
     * @param id the id of the city to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cities/{id}")
    @Timed
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        log.debug("REST request to delete City : {}", id);
        cityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cities?query=:query : search for the city corresponding
     * to the query.
     *
     * @param query    the query of the city search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cities")
    @Timed
    public ResponseEntity<List<City>> searchCities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cities for query {}", query);
        Page<City> page = cityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
