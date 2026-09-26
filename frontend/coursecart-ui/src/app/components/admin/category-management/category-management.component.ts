import { Component, OnInit } from '@angular/core';
import { CatalogService } from '../../../services/catalog.service';
import { Category } from '../../../models/catalog.model';

@Component({
  selector: 'app-category-management',
  templateUrl: './category-management.component.html',
  styleUrls: ['./category-management.component.css']
})
export class CategoryManagementComponent implements OnInit {

  categories: Category[] = [];
  isLoading = false;
  error = '';
  
  showForm = false;
  isEditing = false;
  showConfirmModal = false;
  showAlertModal = false;
  alertMessage = '';
  confirmMessage = '';
  confirmAction: (() => void) | null = null;
  currentCategory: Category = { id: 0, name: '' };
  formError = '';

  constructor(private catalogService: CatalogService) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    // TODO[TRAINEE]: Fetch categories from catalogService and update local state (US 04).
    throw new Error('TODO[TRAINEE]: Implement loadCategories');
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.currentCategory = { id: 0, name: '' };
    this.formError = '';
    this.showForm = true;
  }

  openEditModal(category: Category): void {
    this.isEditing = true;
    this.currentCategory = { ...category };
    this.formError = '';
    this.showForm = true;
  }

  cancelEdit(): void {
    this.currentCategory = { id: 0, name: '' };
    this.isEditing = false;
    this.showForm = false;
    this.formError = '';
  }

  saveCategory(): void {
    // TODO[TRAINEE]: Validate name, then call createCategory or updateCategory based on isEditing. (US 04)
    throw new Error('TODO[TRAINEE]: Implement saveCategory');
  }

  deleteCategory(id: number): void {
    // TODO[TRAINEE]: Call deleteCategory on catalogService, handle success/errors (US 04).
    throw new Error('TODO[TRAINEE]: Implement deleteCategory');
  }

  executeConfirm() {
    if (this.confirmAction) {
      this.confirmAction();
      this.confirmAction = null;
    }
  }
}


