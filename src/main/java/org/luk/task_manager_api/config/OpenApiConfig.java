package org.luk.task_manager_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
            .info(new Info()
                    .title("Task Manager API")
                    .description("Документация для проекта Task Manager API")
                    .version("0.2"));
  }
}