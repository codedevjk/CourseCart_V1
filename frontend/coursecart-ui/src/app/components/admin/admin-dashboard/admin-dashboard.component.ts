import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { UserService } from '../../../services/user.service';
import { CatalogService } from '../../../services/catalog.service';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CommerceService } from '../../../services/commerce.service';
import { Order } from '../../../models/commerce.model';
import { CourseDetail } from '../../../models/catalog.model';
import { catchError, map, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';

interface EnrichedOrder {
  order: Order;
  course: CourseDetail | null;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  totalUsers = 0;
  activeCourses = 0;
  totalEnrollments = 0;
  totalRevenue = 0;
  recentOrders: EnrichedOrder[] = [];

  isLoading = true;
  error = '';

  constructor(
    private userService: UserService,
    private catalogService: CatalogService,
    private enrollmentService: EnrollmentService,
    private commerceService: CommerceService
  ) {}

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.isLoading = true;
    this.error = '';

    forkJoin({
      users: this.userService.getUserCount(),
      courses: this.catalogService.getCourseCount(),
      enrollments: this.enrollmentService.getEnrollmentCount(),
      revenue: this.commerceService.getRevenue()
    }).subscribe({
      next: (data: any) => {
        this.totalUsers = data.users.count || data.users.totalUsers || 0; // fallback depending on actual API response mapping
        this.activeCourses = data.courses.totalCourses || 0;
        
        // Enrollment might return Map<String, Long> mapped as an object { "totalEnrollments": X } or just { count: X }
        this.totalEnrollments = data.enrollments.totalEnrollments || data.enrollments.count || 0;
        this.totalRevenue = data.revenue.totalRevenue || 0;
        this.loadRecentOrders();
      },
      error: (err) => {

        this.error = 'Failed to load dashboard metrics. Please ensure backend services are running.';
        this.isLoading = false;
      }
    });
  }

  currentPage = 0;
  totalPages = 0;
  totalElements = 0;

  loadRecentOrders(page: number = 0): void {
    this.commerceService.getAllOrders(page, 5).pipe(
      switchMap(response => {
        this.currentPage = response.number;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;

        const orders = response.content;
        if (!orders || orders.length === 0) {
          return of([]);
        }
        
        const enrichedRequests = orders.map((order: Order) => {
          return this.catalogService.getCourse(order.courseId).pipe(
            map(course => ({ order, course })),
            catchError(() => of({ order, course: null }))
          );
        });

        return forkJoin(enrichedRequests);
      })
    ).subscribe({
      next: (enrichedOrders: any) => {
        this.recentOrders = enrichedOrders;
        this.isLoading = false;
      },
      error: (err) => {

        this.isLoading = false;
      }
    });
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.isLoading = true;
      this.loadRecentOrders(this.currentPage + 1);
    }
  }

  prevPage(): void {
    if (this.currentPage > 0) {
      this.isLoading = true;
      this.loadRecentOrders(this.currentPage - 1);
    }
  }
}







