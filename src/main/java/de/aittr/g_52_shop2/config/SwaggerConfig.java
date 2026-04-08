package de.aittr.g_52_shop2.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Application Shop",
                description = "Application for various operations with Customers and Products",
                version = "1.0.0",
                contact = @Contact(
                        name = "Lora",
                        email = "lorageo33@gmail.com",
                        url = "http://ait-tr.de"
                )
        )
)
public class SwaggerConfig {
}
