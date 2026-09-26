import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CourseDiscoveryComponent } from './course-discovery.component';
import { CatalogService } from '../../../services/catalog.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('CourseDiscoveryComponent', () => {
  let component: CourseDiscoveryComponent;
  let fixture: ComponentFixture<CourseDiscoveryComponent>;
  let catalogServiceSpy: jasmine.SpyObj<CatalogService>;

  beforeEach(async () => {
    catalogServiceSpy = jasmine.createSpyObj('CatalogService', ['getCourses', 'getCategories']);

    await TestBed.configureTestingModule({
      declarations: [CourseDiscoveryComponent],
      imports: [FormsModule, RouterTestingModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        { provide: CatalogService, useValue: catalogServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CourseDiscoveryComponent);
    component = fixture.componentInstance;
  });

  it('should load categories and courses on init', () => {
    const mockCategories = [{ id: 1, name: 'Programming' }];
    const mockCourses = {
      content: [
        { id: 1, title: 'Java 101', categoryId: 1 }
      ]
    };
    catalogServiceSpy.getCategories.and.returnValue(of(mockCategories as any));
    catalogServiceSpy.getCourses.and.returnValue(of(mockCourses as any));

    fixture.detectChanges();

    expect(component.categories.length).toBe(1);
    expect(component.allCourses.length).toBe(1);
    expect(component.filteredCourses.length).toBe(1);
  });

  it('should filter courses by search query', () => {
    component.allCourses = [
      { id: 1, title: 'Java 101', categoryId: 1, description: 'Learn Java' },
      { id: 2, title: 'Python 101', categoryId: 1, description: 'Learn Python' }
    ] as any;
    
    component.searchQuery = 'Java';
    component.applyFilters();
    
    expect(component.filteredCourses.length).toBe(1);
    expect(component.filteredCourses[0].title).toBe('Java 101');
  });

  it('should filter courses by category', () => {
    component.allCourses = [
      { id: 1, title: 'Java 101', categoryId: 1, description: 'Learn Java' },
      { id: 2, title: 'Math 101', categoryId: 2, description: 'Learn Math' }
    ] as any;
    
    component.selectCategory(1);
    
    expect(component.filteredCourses.length).toBe(1);
    expect(component.filteredCourses[0].title).toBe('Java 101');
    expect(component.selectedCategoryId).toBe(1);
  });

  it('should show all courses when category is cleared', () => {
    component.allCourses = [
      { id: 1, title: 'Java 101', categoryId: 1, description: 'Learn Java' },
      { id: 2, title: 'Math 101', categoryId: 2, description: 'Learn Math' }
    ] as any;
    component.selectedCategoryId = 1;
    
    component.selectCategory(null);
    
    expect(component.filteredCourses.length).toBe(2);
    expect(component.selectedCategoryId).toBeNull();
  });
});
