package pl.fintech.metissociallending.metissociallendingservice.infrastructure.swagger;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(metadata())
                .useDefaultResponseMessages(false)
                .securitySchemes(Lists.newArrayList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .tags(new Tag("User","Log in, sign up, withdraw, deposit money, get user account details operations"),
                        new Tag("Borrower","Operations on borrowers auctions, loans, installments"),
                        new Tag("Lender","Lenders operations on his offers, investments and available auctions"),
                        new Tag("Clock", "Time controlling operations for testing purpose"))
                .genericModelSubstitutes(Optional.class);

    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("Metis Social Lending Service")
                .description("Service API documentation. If you want to try it, you need to use User -> signup to create user, then login with User -> signin. " +
                        "\nSign in responses with JWT Token, copy it to \"authorize\" field formatted as \"Bearer [JWT Token]\", otherwise you will get 403 response" +
                        "\n on every endpoint. Best way to test how it works is to use two cards in browser with two accounts authorized, use one as lender, second as borrower." +
                        "\n\nImportant! This API takes date in \\\"dd/MM/yyyy HH:mm\\\" format and returns timestamp\"")
                .version("1.0.0 RELEASE")
                .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")
                .contact(new Contact("DevMountain - Fintech Challenge", null, null))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new springfox.documentation.service.AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("Authorization", authorizationScopes));
    }
}
