package com.example.orders.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                        .title("API de Ordenes")
                        .description("Api con Spring Boot, Swagger y OpenAPI")
                        .version("1.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .contact(new Contact()
                                .name("Franco Martin")
                                .email("kitox1212@gmail.com")))
                .paths(new Paths()
                        .addPathItem("/api/orders", new PathItem()
                                .get(new Operation()
                                        .summary("Obtener todas las ordenes")
                                        .description("Obtener todas las ordenes de la base de datos")
                                )
                                .post(new Operation()
                                        .summary("Crear una nueva orden")
                                        .description("Crear una nueva orden en la base de datos")
                                ))
                        .addPathItem("/api/orders/{id}", new PathItem()
                                .get(new Operation()
                                        .summary("Obtener una orden por su ID")
                                        .description("Obtener una orden de la base de datos por su ID")
                                )
                                .delete(new Operation()
                                        .summary("Eliminar una orden por su ID")
                                        .description("Eliminar una orden de la base de datos por su ID")
                                )
                                .put(new Operation()
                                        .summary("Actualizar una orden por su ID")
                                        .description("Actualizar una orden de la base de datos por su ID")
                                )
                        )
                );
    }
}
