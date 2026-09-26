import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { Course, Category } from '../../../models/catalog.model';

@Component({
  selector: 'app-course-discovery',
  templateUrl: './course-discovery.component.html',
  styleUrls: ['./course-discovery.component.css']
})
export class CourseDiscoveryComponent implements OnInit {
  courses: Course[] = [];
  categories: Category[] = [];
  
  isLoading = true;
  errorMessage = '';

  searchQuery = '';
  selectedCategoryId: number | null = null;
  
  currentPage = 0;
  pageSize = 12; // 12 courses per page for a nice grid
  totalPages = 0;
  totalElements = 0;

  constructor(
    private catalogService: CatalogService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['category']) {
        this.selectedCategoryId = Number(params['category']);
      }
      this.fetchCategoriesAndCourses();
    });
  }

  fetchCategoriesAndCourses(): void {
    // TODO[TRAINEE]: Fetch categories from catalogService and then call loadCoursesPage() (US 08).
    throw new Error('TODO[TRAINEE]: Implement fetchCategoriesAndCourses');
  }

  loadCoursesPage(): void {
    // TODO[TRAINEE]: Call catalogService.getCourses with filters and pagination. Handle response and errors. (US 08)
    throw new Error('TODO[TRAINEE]: Implement loadCoursesPage');
  }

  applyFilters(): void {
    this.currentPage = 0; // Reset to page 0 on new filter/search
    this.loadCoursesPage();
  }

  selectCategory(categoryId: number | null): void {
    this.selectedCategoryId = categoryId;
    this.applyFilters();
  }
  
  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadCoursesPage();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadCoursesPage();
      window.scrollTo({ top: 0, behavior: 'smooth' });
    }
  }
}
