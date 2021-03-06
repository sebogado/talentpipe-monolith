package io.kimos.talentpipe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CompanyTypeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CompanyTypeSearchRepositoryMockConfiguration {

    @MockBean
    private CompanyTypeSearchRepository mockCompanyTypeSearchRepository;

}
