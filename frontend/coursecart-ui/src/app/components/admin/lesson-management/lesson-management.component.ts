import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { Lesson, CourseDetail } from '../../../models/catalog.model';

@Component({
  selector: 'app-lesson-management',
  templateUrl: './lesson-management.component.html',
  styleUrls: ['./lesson-management.component.css']
})
export class LessonManagementComponent implements OnInit {

  courseId!: number;
  courseTitle = 'Course';
  lessons: Lesson[] = [];
  
  isLoading = false;
  error = '';
  
  showForm = false;
  isEditing = false;
  currentLesson: any = { id: 0, courseId: 0, title: '', content: '', videoUrl: '', displayOrder: 0 };
  formError = '';

  showConfirmModal = false;
  confirmMessage = '';
  confirmAction: (() => void) | null = null;

  constructor(
    private route: ActivatedRoute,
    private catalogService: CatalogService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('courseId');
      if (id) {
        this.courseId = +id;
        this.loadCourseInfo();
        this.loadLessons();
      }
    });
  }

  loadCourseInfo(): void {
    this.catalogService.getCourse(this.courseId).subscribe({
      next: (course) => this.courseTitle = course.title,
      error: () => this.courseTitle = `Course #${this.courseId}` // fallback
    });
  }

  loadLessons(): void {
    this.isLoading = true;
    this.error = '';
    this.catalogService.getLessons(this.courseId).subscribe({
      next: (data) => {
        this.lessons = data;
        this.isLoading = false;
      },
      error: (err) => {

        this.error = 'Failed to load lessons.';
        this.isLoading = false;
      }
    });
  }

  openCreateModal(): void {
    this.showForm = true;
    this.isEditing = false;
    const nextOrder = this.lessons.length > 0 ? Math.max(...this.lessons.map(l => l.displayOrder)) + 1 : 1;
    this.currentLesson = { id: 0, courseId: this.courseId, title: '', content: '', videoUrl: '', displayOrder: nextOrder };
    this.formError = '';
  }

  openEditModal(lesson: Lesson): void {
    this.showForm = true;
    this.isEditing = true;
    this.currentLesson = { ...lesson };
    this.formError = '';
  }

  cancelEdit(): void {
    this.showForm = false;
    this.isEditing = false;
    this.formError = '';
  }

  saveLesson(): void {
    if (!this.currentLesson.title.trim()) {
      this.formError = 'Lesson title is required';
      return;
    }

    if (this.isEditing) {
      this.catalogService.updateLesson(this.courseId, this.currentLesson.id, this.currentLesson).subscribe({
        next: () => {
          this.loadLessons();
          this.cancelEdit();
        },
        error: (err) => this.formError = err.error?.message || 'Failed to update lesson'
      });
    } else {
      this.catalogService.createLesson(this.courseId, this.currentLesson).subscribe({
        next: () => {
          this.loadLessons();
          this.cancelEdit();
        },
        error: (err) => this.formError = err.error?.message || 'Failed to create lesson'
      });
    }
  }

  deleteLesson(id: number): void {
    this.confirmMessage = 'Are you sure you want to delete this lesson?';
    this.confirmAction = () => {
      this.showConfirmModal = false;
      this.catalogService.deleteLesson(this.courseId, id).subscribe({
        next: () => {
          this.loadLessons();
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to delete lesson';
        }
      });
    };
    this.showConfirmModal = true;
  }

  executeConfirm() {
    if (this.confirmAction) {
      this.confirmAction();
      this.confirmAction = null;
    }
  }
}

