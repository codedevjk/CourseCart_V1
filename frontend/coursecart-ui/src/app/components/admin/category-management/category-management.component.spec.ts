import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoryManagementComponent } from './category-management.component';
import { CatalogService } from '../../../services/catalog.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('CategoryManagementComponent', () => {
  let component: CategoryManagementComponent;
  let fixture: ComponentFixture<CategoryManagementComponent>;
  let catalogSvc: any;

  beforeEach(async () => {
    catalogSvc = {
      getCategories: jasmine.createSpy().and.returnValue(of([{ id: 1, name: 'Test' }])),
      createCategory: jasmine.createSpy().and.returnValue(of({ id: 2, name: 'New' })),
      updateCategory: jasmine.createSpy().and.returnValue(of({ id: 1, name: 'Updated' })),
      deleteCategory: jasmine.createSpy().and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      declarations: [ CategoryManagementComponent ],
      imports: [ FormsModule ],
      providers: [ { provide: CatalogService, useValue: catalogSvc } ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CategoryManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load categories on init', () => {
    expect(component.categories.length).toBe(1);
    expect(catalogSvc.getCategories).toHaveBeenCalled();
  });

  it('should create category', () => {
    component.openCreateModal();
    component.currentCategory.name = 'New';
    component.saveCategory();
    expect(catalogSvc.createCategory).toHaveBeenCalled();
  });

  it('should update category', () => {
    component.openEditModal({ id: 1, name: 'Test' });
    component.currentCategory.name = 'Updated';
    component.saveCategory();
    expect(catalogSvc.updateCategory).toHaveBeenCalled();
  });
});
