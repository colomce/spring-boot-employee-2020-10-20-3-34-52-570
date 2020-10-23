package com.thoughtworks.springbootemployee.integration;

import com.thoughtworks.springbootemployee.models.Company;
import com.thoughtworks.springbootemployee.repository.ICompanyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyIntegrationTest {

    @Autowired
    private ICompanyRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_get_all_companies_when_get_all() throws Exception {
        Company company = new Company("OOCL", Collections.emptyList());
        repository.save(company);

        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].companyName").value("OOCL"))
                .andExpect(jsonPath("$[0].employees").isEmpty());
    }

    @Test
    void should_return_the_error_response_with_message_and_status_when_search_by_id_given_invalid_company_id() throws Exception {
        //given
        Integer companyId = 12345;

        // when then
        mockMvc.perform(get("/companies/{companyId}", companyId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Company with id:12345 not found"))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andReturn();
    }
}
