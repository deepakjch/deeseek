package com.deeseek.accounts

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ContextConfiguration(classes = [com.deeseek.accounts.AccountsApplication])
@ActiveProfiles("test")
abstract class BaseIntSpec extends Specification {
    
    @Autowired
    WebApplicationContext webApplicationContext
    
    MockMvc mockMvc
    
    def setupSpec() {
        // Common setup for all integration tests
        println "Setting up integration test context"
    }
    
    def setup() {
        // Setup MockMvc for each test
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }
}