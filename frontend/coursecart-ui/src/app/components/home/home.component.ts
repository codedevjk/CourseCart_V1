import { Component, OnInit } from '@angular/core';
import { CatalogService } from '../../services/catalog.service';
import { Course, Category } from '../../models/catalog.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  trendingCourses: Course[] = [];
  categories: Category[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private catalogService: CatalogService) {}

  ngOnInit(): void {
    // Fetch categories
    this.catalogService.getCategories().subscribe({
      next: (cats) => {
        this.categories = cats;
      },
      error: () => {}
    });

    // Fetch top 3 courses
    this.catalogService.getCourses(undefined, undefined, 0, 3).subscribe({
      next: (response) => {
        this.trendingCourses = response.content;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load trending courses.';
        this.isLoading = false;
      }
    });
  }

  getCardColor(index: number): string {
    return `theme-${index % 4}`;
  }

  getColorClass(index: number): string {
    const colors = ['color-blue', 'color-pink', 'color-yellow', 'color-green'];
    return colors[index % colors.length];
  }

  getCategoryInitials(categoryName: string): string {
    if (!categoryName) return 'CC';
    
    // E.g., "Web Development" -> "WD", "Business" -> "BU"
    const words = categoryName.split(' ');
    if (words.length > 1) {
      return (words[0][0] + words[1][0]).toUpperCase();
    } else if (categoryName.length >= 2) {
      return categoryName.substring(0, 2).toUpperCase();
    }
    return categoryName[0].toUpperCase();
  }
}

