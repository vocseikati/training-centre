package com.greenfoxacademy.vocseikatimasterwork.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors
            .basePackage("com.greenfoxacademy.vocseikatimasterwork"))
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .build()
        .apiInfo(apiDetails());
  }

  private ApiInfo apiDetails() {
    return new ApiInfoBuilder()
        .title("Hairdresser Training Center Backend API")
        .version("1.0.0")
        .description("API details of Training Center Backend Service")
        .build();
  }
}
