package com.deeseek.accounts

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@SpringBootTest
@ContextConfiguration(classes = [com.deeseek.accounts.AccountsApplication])
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class BaseIntSpec extends Specification {
    
    def setupSpec() {
        // Common setup for all integration tests
        println "Setting up integration test context"
    }
}