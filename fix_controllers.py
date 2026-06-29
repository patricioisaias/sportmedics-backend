import os
import re

root_dir = r"d:\Documentos\GitHub\sportmedics-backend\sportmedics-maven"

count = 0
for subdir, dirs, files in os.walk(root_dir):
    for file in files:
        if file.endswith("Controller.java"):
            filepath = os.path.join(subdir, file)
            with open(filepath, "rb") as f:
                content = f.read()
            
            # Remove BOM
            if content.startswith(b"\xef\xbb\xbf"):
                content = content[3:]
                
            try:
                text = content.decode("utf-8")
            except UnicodeDecodeError:
                text = content.decode("latin1", errors="replace")
                
            orig_text = text
            text = re.sub(r"^[ \t]*@Operation[^\n]+\n", "", text, flags=re.MULTILINE)
            text = re.sub(r"^[ \t]*@ApiResponses\([\s\S]*?\}\)\n", "", text, flags=re.MULTILINE)
            text = re.sub(r"^[ \t]*import io\.swagger\.v3\.oas\.annotations.*?\n", "", text, flags=re.MULTILINE)
            
            if text != orig_text or len(content) != len(text.encode("utf-8")):
                with open(filepath, "wb") as f:
                    f.write(text.encode("utf-8"))
                count += 1
                print(f"Fixed {filepath}")

print(f"Total files fixed: {count}")

