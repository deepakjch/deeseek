package com.deebank.accountservice.health

import com.deebank.accountservice.BaseIntSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class HealthCheckIntSpec extends BaseIntSpec {

    def "should return health status UP"() {
        when: "we call the health endpoint"
            def result = mockMvc.perform(get("/actuator/health"))

        then: "it should return OK status with UP health"
            result.andExpect(status().isOk())
                    .andExpect(jsonPath('$.status').value("UP"))
    }
}