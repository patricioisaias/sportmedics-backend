import os
import re

root_dir = r"d:\Documentos\GitHub\sportmedics-backend\sportmedics-maven"
count = 0

for subdir, dirs, files in os.walk(root_dir):
    for file in files:
        if file.endswith("Controller.java"):
            filepath = os.path.join(subdir, file)
            with open(filepath, "r", encoding="utf-8") as f:
                content = f.read()
            
            if "import io.swagger.v3.oas.annotations" in content:
                continue
                
            entity_name = file.replace("Controller.java", "")
            
            # 1. Add imports after package
            import_str = """
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
"""
            content = re.sub(r"^(package .*?;)", r"\1\n" + import_str, content, count=1, flags=re.MULTILINE)
            
            # 2. Add @Tag above @RestController
            tag_str = f'@Tag(name = "{entity_name}", description = "Operaciones relacionadas con {entity_name}")\n@RestController'
            content = re.sub(r"@RestController", tag_str, content)
            
            # 3. Add Operation and ApiResponses above Mapping
            lines = content.split("\n")
            new_lines = []
            
            for line in lines:
                mapping_match = re.search(r"^[ \t]*@(Get|Post|Put|Delete|Patch)Mapping", line)
                if mapping_match:
                    clean_line = line.replace("\r", "")
                    indent = clean_line[:len(clean_line) - len(clean_line.lstrip())]
                    
                    op_str = f'{indent}@Operation(summary = "Realizar operación", description = "Endpoint para realizar operaciones en {entity_name}")'
                    
                    new_lines.append(op_str)
                    new_lines.append(f'{indent}@ApiResponses(value = {{ ')
                    new_lines.append(f'{indent}    @ApiResponse(responseCode = "200", description = "Operación exitosa"),')
                    new_lines.append(f'{indent}    @ApiResponse(responseCode = "400", description = "Petición inválida"),')
                    new_lines.append(f'{indent}    @ApiResponse(responseCode = "404", description = "Recurso no encontrado")')
                    new_lines.append(f'{indent}}})')
                
                new_lines.append(line.replace("\r", ""))
                
            with open(filepath, "w", encoding="utf-8") as f:
                f.write("\n".join(new_lines))
            count += 1
            print(f"Added Swagger to {filepath}")
