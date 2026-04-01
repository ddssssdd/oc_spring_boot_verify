package com.example.tableviewer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("图书管理系统 API")
                        .version("1.0.0")
                        .description("图书管理系统的RESTful API文档，包含书籍、借阅、入库、归还、读者、角色等模块")
                        .contact(new Contact()
                                .name("图书馆管理系统")
                                .email("admin@library.com")));
    }
}
