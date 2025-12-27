package com.deeseek.accounts.health

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HealthCheckTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def "should return health status UP"() {
        when:
            def result = mockMvc.perform(get("/actuator/health"))

        then:
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.status').value("UP"))
    }

    def "should return database health status"() {
        when:
            def result = mockMvc.perform(get("/actuator/health"))

        then:
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.status').value("UP"))
                    .andExpect(jsonPath('$.components.db').exists())
                    .andExpect(jsonPath('$.components.db.status').value("UP"))
    }

    def "should return actuator info endpoint"() {
        when:
            def result = mockMvc.perform(get("/actuator/info"))

        then:
            result.andExpect(status().isOk())
    }
}

