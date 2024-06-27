package com.ucc.crudservice.Swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API de Productos")
                        .description("Api con Spring Boot, Swagger y OpenAPI")
                        .version("1.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .contact(new Contact()
                                .name("Ruben Morales")
                                .url("https://www.linkedin.com/in/ruben-moraless/")
                                .email("rm96090@gmail.com")))
                .paths(new Paths()
                        .addPathItem("/api/products", new PathItem()
                                .get(new Operation()
                                        .summary("Obtener todos los productos")
                                        .description("Obtener todos los productos de la base de datos")
                                )
                                .post(new Operation()
                                        .summary("Crear un nuevo producto")
                                        .description("Crear un nuevo producto en la base de datos")
                                ))
                        .addPathItem("/api/products/{id}", new PathItem()
                                .get(new Operation()
                                        .summary("Obtener un producto por su ID")
                                        .description("Obtener un producto de la base de datos por su ID")
                                )
                                .delete(new Operation()
                                        .summary("Eliminar un producto por su ID")
                                        .description("Eliminar un producto de la base de datos por su ID")
                                )
                                .put(new Operation()
                                        .summary("Actualizar un producto por su ID")
                                        .description("Actualizar un producto de la base de datos por su ID")
                                )
                        )
                );
    }
}
