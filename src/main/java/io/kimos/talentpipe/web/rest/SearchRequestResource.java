package io.kimos.talentpipe.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.kimos.talentpipe.domain.*;
import io.kimos.talentpipe.service.SearchRequestService;
import io.kimos.talentpipe.service.UserService;
import io.kimos.talentpipe.web.rest.dto.CreateSearchRequestDTO;
import io.kimos.talentpipe.web.rest.errors.BadRequestAlertException;
import io.kimos.talentpipe.web.rest.errors.UserNotAuthenticatedException;
import io.kimos.talentpipe.web.rest.util.HeaderUtil;
import io.kimos.talentpipe.web.rest.util.PaginationUtil;
import ma.glasnost.orika.MapperFacade;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing SearchRequest.
 */
@RestController
@RequestMapping("/api")
public class SearchRequestResource {

    private static final String ENTITY_NAME = "searchRequest";
    private final Logger log = LoggerFactory.getLogger(SearchRequestResource.class);
    private final SearchRequestService searchRequestService;
    private final MapperFacade orikaMapper;
    private final UserService userService;

    public SearchRequestResource(SearchRequestService searchRequestService, MapperFacade orikaMapper, UserService userService) {
        this.searchRequestService = searchRequestService;
        this.orikaMapper = orikaMapper;
        this.userService = userService;
    }

    /**
     * POST  /search-requests : Create a new searchRequest.
     *
     * @param searchRequest the searchRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchRequest, or with status 400 (Bad Request) if the searchRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-requests")
    @Timed
    public ResponseEntity<SearchRequest> createSearchRequest(@Valid @RequestBody SearchRequest searchRequest) throws URISyntaxException {
        log.debug("REST request to save SearchRequest : {}", searchRequest);
        SearchRequest result = searchRequestService.save(searchRequest);
        return ResponseEntity.created(new URI("/api/search-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }/**
     * POST  /search-requests : Create a new searchRequest.
     *
     * @param searchRequest the searchRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searchRequest, or with status 400 (Bad Request) if the searchRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/search-requests/me")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN')")
    @Timed
    public ResponseEntity<SearchRequest> createSearchRequest(@Valid @RequestBody CreateSearchRequestDTO searchRequest) throws URISyntaxException {
        log.debug("REST request to save SearchRequest : {}", searchRequest);
        User user = userService.getUserWithAuthorities().orElseThrow(UserNotAuthenticatedException::new);
        SearchRequest entity = orikaMapper.map(searchRequest, SearchRequest.class);
        entity.setCompany(user.getCompany());
        SearchRequest result = searchRequestService.save(entity);
        return ResponseEntity.created(new URI("/api/search-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /search-requests : Updates an existing searchRequest.
     *
     * @param searchRequest the searchRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated searchRequest,
     * or with status 400 (Bad Request) if the searchRequest is not valid,
     * or with status 500 (Internal Server Error) if the searchRequest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/search-requests")
    @Timed
    public ResponseEntity<SearchRequest> updateSearchRequest(@Valid @RequestBody SearchRequest searchRequest) throws URISyntaxException {
        log.debug("REST request to update SearchRequest : {}", searchRequest);
        if (searchRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SearchRequest result = searchRequestService.save(searchRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, searchRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /search-requests : get all the searchRequests.
     *
     * @param pageable  the pagination informatio
     * @return the ResponseEntity with status 200 (OK) and the list of searchRequests in body
     */
    @GetMapping("/search-requests")
    @Timed
    public ResponseEntity<List<SearchRequest>> getAllSearchRequests(Pageable pageable) {
        log.debug("REST request to get a page of SearchRequests");
        User user = userService.getUserWithAuthorities().orElseThrow(UserNotAuthenticatedException::new);
        Page<SearchRequest> page = user.getRoles().stream().map(Role::getAuthorities)
            .flatMap(Collection::stream).map(Authority::getAuthority)
            .collect(Collectors.toSet())
            .contains(BaseAuthorities.ROLE_ADMIN.getAuthority())
            ? searchRequestService.findAll(pageable) : searchRequestService.findAllForCompany(pageable, user);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/search-requests");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /search-requests/:id : get the "id" searchRequest.
     *
     * @param id the id of the searchRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the searchRequest, or with status 404 (Not Found)
     */
    @GetMapping("/search-requests/{id}")
    @Timed
    public ResponseEntity<SearchRequest> getSearchRequest(@PathVariable Long id) {
        log.debug("REST request to get SearchRequest : {}", id);
        Optional<SearchRequest> searchRequest = searchRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(searchRequest);
    }

    /**
     * DELETE  /search-requests/:id : delete the "id" searchRequest.
     *
     * @param id the id of the searchRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/search-requests/{id}")
    @Timed
    public ResponseEntity<Void> deleteSearchRequest(@PathVariable Long id) {
        log.debug("REST request to delete SearchRequest : {}", id);
        searchRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/search-requests?query=:query : search for the searchRequest corresponding
     * to the query.
     *
     * @param query    the query of the searchRequest search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/search-requests")
    @Timed
    public ResponseEntity<List<SearchRequest>> searchSearchRequests(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SearchRequests for query {}", query);
        Page<SearchRequest> page = searchRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/search-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
