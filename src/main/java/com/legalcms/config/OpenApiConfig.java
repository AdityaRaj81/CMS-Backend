package com.legalcms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Legal Case Management System API",
        version = "1.0.0",
        description = """
            REST API for Legal CMS Backend
            
            This system manages legal cases for Indian law firms handling Patna High Court 
            and Barh Civil Court cases. It supports Advocate, Admin, and Client roles.
            
            **Features:**
            - JWT-based authentication
            - Role-based access control (ADMIN, ADVOCATE, CLIENT)
            - Complete case management (CRUD operations)
            - Court system integration (CNR fetch, case status)
            - Document management (upload, view, download)
            - Hearing history tracking
            - Automated court data synchronization (daily scheduler)
            
            **Authentication:**
            1. Login via `/api/auth/login` to get JWT token
            2. Use the token in Authorization header: `Bearer <token>`
            3. All protected endpoints require valid JWT token
            
            **Frontend Integration:**
            - All dates returned in ISO-8601 format (YYYY-MM-DD)
            - Field names match frontend exactly (camelCase)
            - Standard HTTP status codes
            - Meaningful error messages
            """,
        contact = @Contact(
            name = "Legal CMS Support",
            email = "support@legalcms.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Local Development Server"),
        @Server(url = "https://api.legalcms.com", description = "Production Server")
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER,
    description = "Enter JWT token obtained from /api/auth/login endpoint"
)
public class OpenApiConfig {
}
