import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CourseLearningComponent } from './course-learning.component';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CatalogService } from '../../../services/catalog.service';
import { AuthService } from '../../../services/auth.service';
import { ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('CourseLearningComponent', () => {
  let component: CourseLearningComponent;
  let fixture: ComponentFixture<CourseLearningComponent>;
  let enrollmentServiceSpy: jasmine.SpyObj<EnrollmentService>;
  let catalogServiceSpy: jasmine.SpyObj<CatalogService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    enrollmentServiceSpy = jasmine.createSpyObj('EnrollmentService', ['getEnrollments', 'getLessonProgress', 'completeLesson']);
    catalogServiceSpy = jasmine.createSpyObj('CatalogService', ['getCourse']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);

    await TestBed.configureTestingModule({
      declarations: [CourseLearningComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        { provide: EnrollmentService, useValue: enrollmentServiceSpy },
        { provide: CatalogService, useValue: catalogServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CourseLearningComponent);
    component = fixture.componentInstance;
  });

  it('should set error message if enrollment not found on init', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    enrollmentServiceSpy.getEnrollments.and.returnValue(of([])); // No enrollments
    catalogServiceSpy.getCourse.and.returnValue(of({} as any));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('You are not enrolled in this course.');
  });

  it('should call completeLesson when markComplete is called and lesson is not already completed', () => {
    component.enrollmentId = 1;
    component.activeLesson = { id: 1 } as any;
    component.completedLessonIds = new Set();
    enrollmentServiceSpy.completeLesson.and.returnValue(of(undefined as void));

    component.markComplete();

    expect(enrollmentServiceSpy.completeLesson).toHaveBeenCalledWith(1, 1);
    expect(component.completedLessonIds.has(1)).toBeTrue();
  });

  it('should NOT call completeLesson when lesson is already in completedLessonIds', () => {
    component.enrollmentId = 1;
    component.activeLesson = { id: 1 } as any;
    component.completedLessonIds = new Set([1]); // Already completed

    component.markComplete();

    expect(enrollmentServiceSpy.completeLesson).not.toHaveBeenCalled();
  });

  it('should calculate progressPercentage based on completed lessons', () => {
    component.course = { lessons: [{}, {}] } as any; // 2 lessons total
    component.completedLessonIds = new Set([1]); // 1 completed

    expect(component.progressPercentage).toBe(50);
  });

  it('should return 0 progressPercentage when no lessons completed', () => {
    component.course = { lessons: [{}, {}] } as any;
    component.completedLessonIds = new Set();

    expect(component.progressPercentage).toBe(0);
  });
});
