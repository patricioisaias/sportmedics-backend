
import os
import re

root_dir = r"d:\Documentos\GitHub\sportmedics-backend\sportmedics-maven"
count = 0

for subdir, dirs, files in os.walk(root_dir):
    for file in files:
        if file.endswith("Controller.java"):
            filepath = os.path.join(subdir, file)
            with open(filepath, "r", encoding="utf-8") as f:
                text = f.read()

            orig_text = text
            text = re.sub(r"^[ \t]*@Operation.*?\n", "", text, flags=re.MULTILINE)
            text = re.sub(r"^[ \t]*@Tag.*?\n", "", text, flags=re.MULTILINE)
            # Remove the whole @ApiResponses block
            text = re.sub(r"^[ \t]*@ApiResponses\([\s\S]*?\}\)\n", "", text, flags=re.MULTILINE)
            # Just in case there are stray @ApiResponses or @ApiResponse
            text = re.sub(r"^[ \t]*@ApiResponse.*?\n", "", text, flags=re.MULTILINE)
            
            # Remove all swagger imports
            text = re.sub(r"^import io\.swagger\.v3\.oas\.annotations.*?\n", "", text, flags=re.MULTILINE)

            # Let us remove empty lines that were left behind
            text = re.sub(r"\n\s*\n\s*\n", "\n\n", text)
            
            if text != orig_text:
                with open(filepath, "w", encoding="utf-8") as f:
                    f.write(text)
                count += 1
                print(f"Fixed {filepath}")

