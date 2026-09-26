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
    
    // Check if we didn't add the imports yet
    if (!content.includes('import { HttpClientTestingModule }')) {
      content = content.replace(
        "import { ComponentFixture, TestBed } from '@angular/core/testing';",
        "import { ComponentFixture, TestBed } from '@angular/core/testing';\nimport { HttpClientTestingModule } from '@angular/common/http/testing';\nimport { RouterTestingModule } from '@angular/router/testing';\nimport { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';\nimport { FormsModule } from '@angular/forms';"
      );
      
      // I also want to make sure FormsModule is in the imports array of TestBed if it's not already
      if (content.includes('imports: [HttpClientTestingModule, RouterTestingModule]')) {
        content = content.replace('imports: [HttpClientTestingModule, RouterTestingModule]', 'imports: [HttpClientTestingModule, RouterTestingModule, FormsModule]');
      }
      
      fs.writeFileSync(filePath, content);
      console.log('Patched: ' + filePath);
    }
  }
});
