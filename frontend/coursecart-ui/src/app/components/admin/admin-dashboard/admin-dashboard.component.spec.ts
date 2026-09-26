import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminDashboardComponent } from './admin-dashboard.component';
import { UserService } from '../../../services/user.service';
import { CatalogService } from '../../../services/catalog.service';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CommerceService } from '../../../services/commerce.service';
import { of, throwError } from 'rxjs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('AdminDashboardComponent', () => {
  let component: AdminDashboardComponent;
  let fixture: ComponentFixture<AdminDashboardComponent>;
  let userSvc: any, catalogSvc: any, enrollSvc: any, commerceSvc: any;

  beforeEach(async () => {
    userSvc = { getUserCount: jasmine.createSpy().and.returnValue(of({ totalUsers: 10 })) };
    catalogSvc = { getCourseCount: jasmine.createSpy().and.returnValue(of({ totalCourses: 5 })) };
    enrollSvc = { getEnrollmentCount: jasmine.createSpy().and.returnValue(of({ totalEnrollments: 20 })) };
    commerceSvc = { getRevenue: jasmine.createSpy().and.returnValue(of({ totalRevenue: 500 })) };

    await TestBed.configureTestingModule({
      declarations: [ AdminDashboardComponent ],
      providers: [
        { provide: UserService, useValue: userSvc },
        { provide: CatalogService, useValue: catalogSvc },
        { provide: EnrollmentService, useValue: enrollSvc },
        { provide: CommerceService, useValue: commerceSvc }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and load metrics', () => {
    expect(component).toBeTruthy();
    expect(component.totalUsers).toBe(10);
    expect(component.activeCourses).toBe(5);
    expect(component.totalEnrollments).toBe(20);
    expect(component.totalRevenue).toBe(500);
    expect(component.isLoading).toBeFalse();
  });

  it('should handle API errors', () => {
    userSvc.getUserCount.and.returnValue(throwError(() => new Error('Error')));
    component.loadMetrics();
    expect(component.error).toBeTruthy();
    expect(component.isLoading).toBeFalse();
  });
});
