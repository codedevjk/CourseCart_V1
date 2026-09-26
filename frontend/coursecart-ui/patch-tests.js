const fs = require('fs');
const path = require('path');

function walkDir(dir, callback) {
  fs.readdirSync(dir).forEach(f => {
    let dirPath = path.join(dir, f);
    let isDirectory = fs.statSync(dirPath).isDirectory();
    isDirectory ? walkDir(dirPath, callback) : callback(path.join(dir, f));
  });
}

walkDir('./src/app', function(filePath) {
  if (filePath.endsWith('.spec.ts')) {
    let content = fs.readFileSync(filePath, 'utf8');
    
    // Check if we already patched it
    if (!content.includes('HttpClientTestingModule')) {
      content = content.replace(
        "import { TestBed } from '@angular/core/testing';",
        "import { TestBed } from '@angular/core/testing';\nimport { HttpClientTestingModule } from '@angular/common/http/testing';\nimport { RouterTestingModule } from '@angular/router/testing';\nimport { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';"
      );
      
      // Inject into TestBed.configureTestingModule
      if (content.includes('imports: [')) {
        content = content.replace('imports: [', 'imports: [\n        HttpClientTestingModule,\n        RouterTestingModule,\n');
      } else {
        content = content.replace('declarations: [', 'imports: [HttpClientTestingModule, RouterTestingModule],\n      schemas: [CUSTOM_ELEMENTS_SCHEMA],\n      declarations: [');
      }
      
      // Specially for app.component.spec.ts which might already have imports
      if (!content.includes('schemas: [CUSTOM_ELEMENTS_SCHEMA]')) {
         content = content.replace('declarations: [', 'schemas: [CUSTOM_ELEMENTS_SCHEMA],\n      declarations: [');
      }
      
      fs.writeFileSync(filePath, content);
      console.log('Patched: ' + filePath);
    }
  }
});
