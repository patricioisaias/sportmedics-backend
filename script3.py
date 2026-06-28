import os

base_dir = r"d:\Documentos\GitHub\sportmedics-backend\sportmedics-maven"
business_services = [
    "ms-access", "ms-auth", "ms-billing", "ms-employee", "ms-inventory",
    "ms-member", "ms-notification", "ms-scheduling", "ms-subscription", "ms-workout"
]

for service in business_services:
    base_package_str = service.replace("-", "_")
    class_name = service.replace("ms-", "").replace("-", "").title()
    
    config_dir = os.path.join(base_dir, service, "src", "main", "java", "cl", "sportmedics", base_package_str, "config")
    config_path = os.path.join(config_dir, "SwaggerConfig.java")
    
    if os.path.exists(config_path):
        config_content = f"""package cl.sportmedics.{base_package_str}.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {{

    @Bean
    public OpenAPI customOpenAPI() {{
        return new OpenAPI()
                .info(new Info()
                        .title("API Sportmedics {class_name}")
                        .version("1.0")
                        .description("Documentación de la API para el microservicio {service}"));
    }}
}}
"""
        with open(config_path, 'w', encoding='utf-8') as f:
            f.write(config_content)
            
print("SwaggerConfigs reescritos exitosamente sin BOM.")
