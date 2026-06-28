$services = "ms-access", "ms-auth", "ms-billing", "ms-employee", "ms-inventory", "ms-member", "ms-notification", "ms-scheduling", "ms-subscription", "ms-workout"

$yamlProperties = @"

springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    path: /doc/swagger-ui.html
"@

foreach ($service in $services) {
    # 1. Update application.yml
    $ymlPath = "sportmedics-maven\$service\src\main\resources\application.yml"
    if (Test-Path $ymlPath) {
        $content = Get-Content $ymlPath -Raw
        if ($content -notmatch "springdoc:") {
            Add-Content -Path $ymlPath -Value $yamlProperties -Encoding UTF8
        }
    }
    
    # 2. Create SwaggerConfig.java
    $basePackageStr = $service.Replace("-", "_")
    $className = (Get-Culture).TextInfo.ToTitleCase($service.Replace("ms-", "")).Replace("-", "")
    $packageDir = "sportmedics-maven\$service\src\main\java\cl\sportmedics\$basePackageStr\config"
    
    if (-not (Test-Path $packageDir)) {
        New-Item -ItemType Directory -Path $packageDir | Out-Null
    }
    
    $configContent = @"
package cl.sportmedics.$basePackageStr.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sportmedics $className")
                        .version("1.0")
                        .description("Documentación de la API para el microservicio $service"));
    }
}
"@
    $configPath = Join-Path $packageDir "SwaggerConfig.java"
    Set-Content -Path $configPath -Value $configContent -Encoding UTF8
}
