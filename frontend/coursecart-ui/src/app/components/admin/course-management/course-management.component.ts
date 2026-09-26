import { Component, OnInit } from '@angular/core';
import { CatalogService } from '../../../services/catalog.service';
import { Course, Category } from '../../../models/catalog.model';

@Component({
  selector: 'app-course-management',
  templateUrl: './course-management.component.html',
  styleUrls: ['./course-management.component.css']
})
export class CourseManagementComponent implements OnInit {

  courses: Course[] = [];
  categories: Category[] = [];
  
  isLoading = false;
  error = '';
  
  showForm = false;
  isEditing = false;
  currentCourse: any = { id: 0, categoryId: null, title: '', description: '', price: 0, status: 'DRAFT' };
  formError = '';
  
  showConfirmModal = false;
  confirmMessage = '';
  confirmAction: (() => void) | null = null;

  constructor(private catalogService: CatalogService) { }

  ngOnInit(): void {
    this.loadCategories();
    this.loadCourses();
  }

  loadCategories(): void {
    this.catalogService.getCategories().subscribe({
      next: (data) => this.categories = data,
      error: (err) => {}
    });
  }

  loadCourses(): void {
    // TODO[TRAINEE]: Fetch all admin courses from catalogService (US 05).
    throw new Error('TODO[TRAINEE]: Implement loadCourses');
  }

  openCreateModal(): void {
    this.showForm = true;
    this.isEditing = false;
    this.currentCourse = { id: 0, categoryId: null, title: '', description: '', price: 0, status: 'DRAFT' };
    this.formError = '';
  }

  openEditModal(course: any): void {
    this.showForm = true;
    this.isEditing = true;
    this.currentCourse = { 
      id: course.id, 
      categoryId: course.category?.id || course.categoryId, 
      title: course.title, 
      description: course.description, 
      price: course.price, 
      status: course.status 
    };
    this.formError = '';
  }

  cancelEdit(): void {
    this.showForm = false;
    this.isEditing = false;
    this.formError = '';
  }

  saveCourse(): void {
    // TODO[TRAINEE]: Validate course fields, then call createCourse or updateCourse based on isEditing (US 05).
    throw new Error('TODO[TRAINEE]: Implement saveCourse');
  }

  changeStatus(course: Course, newStatus: string): void {
    // TODO[TRAINEE]: Update course status via catalogService. Ensure course has a category before activating (US 07).
    throw new Error('TODO[TRAINEE]: Implement changeStatus');
  }

  getCategoryName(categoryId: number): string {
    const cat = this.categories.find(c => c.id === categoryId);
    return cat ? cat.name : 'Unknown';
  }

  executeConfirm() {
    if (this.confirmAction) {
      this.confirmAction();
      this.confirmAction = null;
    }
  }
}

