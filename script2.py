import os
import re

base_dir = r"d:\Documentos\GitHub\sportmedics-backend\sportmedics-maven"
business_services = [
    "ms-access", "ms-auth", "ms-billing", "ms-employee", "ms-inventory",
    "ms-member", "ms-notification", "ms-scheduling", "ms-subscription", "ms-workout"
]

import_statements = """
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
"""

for service in business_services:
    controller_dir = os.path.join(base_dir, service, "src", "main", "java", "cl", "sportmedics", service.replace("-", "_"), "controller")
    if not os.path.exists(controller_dir):
        print(f"Skipping {controller_dir}")
        continue
        
    for filename in os.listdir(controller_dir):
        if filename.endswith("Controller.java"):
            filepath = os.path.join(controller_dir, filename)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
                
            if "@Tag(" in content:
                print(f"Already documented: {filename}")
                continue 
                
            # 1. Add imports after package declaration
            content = re.sub(r'^(package [^;]+;)', r'\1\n' + import_statements, content, count=1, flags=re.MULTILINE)
            
            # 2. Add @Tag above @RestController
            entity_name = filename.replace("Controller.java", "")
            if entity_name.startswith("Ms"):
                entity_name = entity_name[2:]
                
            tag_annotation = f'@Tag(name = "{entity_name}", description = "Operaciones relacionadas con {entity_name}")\n@RestController'
            content = content.replace('@RestController', tag_annotation)
            
            # 3. Add @Operation above Mapping methods
            mappings = [
                (r'@GetMapping(?:\([^)]*\))?', "Obtener registros"),
                (r'@PostMapping(?:\([^)]*\))?', "Crear un nuevo registro"),
                (r'@PutMapping(?:\([^)]*\))?', "Actualizar un registro existente"),
                (r'@DeleteMapping(?:\([^)]*\))?', "Eliminar un registro")
            ]
            
            for mapping_regex, desc in mappings:
                pattern = re.compile(r'(\n\s*)(' + mapping_regex + r')')
                
                def replace_mapping(match):
                    indent = match.group(1)
                    original_mapping = match.group(2)
                    
                    operation = f'{indent}@Operation(summary = "{desc}", description = "{desc} en el sistema")'
                    api_resp = f'{indent}@ApiResponses(value = {{'
                    api_resp += f'{indent}    @ApiResponse(responseCode = "200", description = "Operación exitosa"),'
                    api_resp += f'{indent}    @ApiResponse(responseCode = "400", description = "Petición inválida"),'
                    api_resp += f'{indent}    @ApiResponse(responseCode = "404", description = "Recurso no encontrado")'
                    api_resp += f'{indent}}})'
                    
                    return f'{operation}{api_resp}{indent}{original_mapping}'
                    
                content = pattern.sub(replace_mapping, content)
                
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Updated {filename}")
