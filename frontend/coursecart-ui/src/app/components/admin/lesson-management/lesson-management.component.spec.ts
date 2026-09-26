import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LessonManagementComponent } from './lesson-management.component';
import { CatalogService } from '../../../services/catalog.service';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';

describe('LessonManagementComponent', () => {
  let component: LessonManagementComponent;
  let fixture: ComponentFixture<LessonManagementComponent>;
  let catalogSvc: any;

  beforeEach(async () => {
    catalogSvc = {
      getCourse: jasmine.createSpy().and.returnValue(of({ title: 'Course' })),
      getLessons: jasmine.createSpy().and.returnValue(of([{ id: 1, title: 'Lesson 1', displayOrder: 1, content: 'Test' }])),
      createLesson: jasmine.createSpy().and.returnValue(of({})),
      updateLesson: jasmine.createSpy().and.returnValue(of({})),
      deleteLesson: jasmine.createSpy().and.returnValue(of({}))
    };

    const routeSpy = {
      paramMap: of({ get: () => '1' })
    };

    await TestBed.configureTestingModule({
      declarations: [ LessonManagementComponent ],
      imports: [ FormsModule, RouterTestingModule ],
      providers: [
        { provide: CatalogService, useValue: catalogSvc },
        { provide: ActivatedRoute, useValue: routeSpy }
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LessonManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should load course context and lessons on init', () => {
    expect(component.courseId).toBe(1);
    expect(component.lessons.length).toBe(1);
    expect(catalogSvc.getLessons).toHaveBeenCalledWith(1);
  });

  it('should create lesson', () => {
    component.openCreateModal();
    component.currentLesson.title = 'New';
    component.currentLesson.content = 'Content';
    component.saveLesson();
    expect(catalogSvc.createLesson).toHaveBeenCalled();
  });
});
