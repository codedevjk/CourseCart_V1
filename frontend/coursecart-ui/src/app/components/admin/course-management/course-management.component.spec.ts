import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CourseManagementComponent } from './course-management.component';
import { CatalogService } from '../../../services/catalog.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

describe('CourseManagementComponent', () => {
  let component: CourseManagementComponent;
  let fixture: ComponentFixture<CourseManagementComponent>;
  let catalogSvc: any;

  beforeEach(async () => {
    catalogSvc = {
      getCategories: jasmine.createSpy().and.returnValue(of([{ id: 1, name: 'Test' }])),
      getAdminCourses: jasmine.createSpy().and.returnValue(of([{ id: 1, title: 'Course', category: { id: 1 }, price: 10, status: 'DRAFT' }])),
      createCourse: jasmine.createSpy().and.returnValue(of({})),
      updateCourse: jasmine.createSpy().and.returnValue(of({})),
      updateCourseStatus: jasmine.createSpy().and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      declarations: [ CourseManagementComponent ],
      imports: [ FormsModule, RouterTestingModule ],
      providers: [ { provide: CatalogService, useValue: catalogSvc } ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load courses and categories on init', () => {
    expect(component.courses.length).toBe(1);
    expect(component.categories.length).toBe(1);
  });

  it('should change status after confirmation', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    component.changeStatus(component.courses[0], 'ACTIVE');
    expect(catalogSvc.updateCourseStatus).toHaveBeenCalledWith(1, 'ACTIVE');
  });
});
