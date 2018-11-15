package io.kimos.talentppe.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of WorkTypeSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class WorkTypeSearchRepositoryMockConfiguration {

    @MockBean
    private WorkTypeSearchRepository mockWorkTypeSearchRepository;

}