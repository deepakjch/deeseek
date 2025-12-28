package com.deebank.accountservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "Account Service API",
				version = "1.0.0",
				description = "RESTful API for managing customer accounts and account operations in DeeBank",
				contact = @Contact(
						name = "DeeBank Support",
						email = "support@deebank.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0.html"
				)
		),
		servers = {
				@Server(
						url = "http://localhost:8080",
						description = "Local Development Server"
				)
		}
)
public class OpenApiConfig {
}

