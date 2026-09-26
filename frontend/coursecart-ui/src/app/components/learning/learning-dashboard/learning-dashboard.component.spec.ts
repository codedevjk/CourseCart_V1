import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { LearningDashboardComponent } from './learning-dashboard.component';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CatalogService } from '../../../services/catalog.service';
import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';

describe('LearningDashboardComponent', () => {
  let component: LearningDashboardComponent;
  let fixture: ComponentFixture<LearningDashboardComponent>;
  let enrollmentServiceSpy: jasmine.SpyObj<EnrollmentService>;
  let catalogServiceSpy: jasmine.SpyObj<CatalogService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    enrollmentServiceSpy = jasmine.createSpyObj('EnrollmentService', ['getEnrollments', 'getLessonProgress']);
    catalogServiceSpy = jasmine.createSpyObj('CatalogService', ['getCourse']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);

    await TestBed.configureTestingModule({
      declarations: [LearningDashboardComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        { provide: EnrollmentService, useValue: enrollmentServiceSpy },
        { provide: CatalogService, useValue: catalogServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LearningDashboardComponent);
    component = fixture.componentInstance;
  });

  it('should call getEnrollments on init and calculate progress', fakeAsync(() => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    const mockEnrollments = [
      { id: 1, courseId: 101 }
    ];
    enrollmentServiceSpy.getEnrollments.and.returnValue(of(mockEnrollments as any));
    catalogServiceSpy.getCourse.and.returnValue(of({ id: 101, title: 'Java', lessons: [{}, {}] } as any));
    enrollmentServiceSpy.getLessonProgress.and.returnValue(of([1])); // 1 completed out of 2

    fixture.detectChanges();
    tick(); // resolve observables

    expect(enrollmentServiceSpy.getEnrollments).toHaveBeenCalledWith(1);
    expect(component.enrolledCourses.length).toBe(1);
    expect(component.enrolledCourses[0].progress).toBe(50);
  }));

  it('should handle empty enrollment list', fakeAsync(() => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    enrollmentServiceSpy.getEnrollments.and.returnValue(of([]));

    fixture.detectChanges();
    tick();

    expect(component.enrolledCourses.length).toBe(0);
  }));
});
