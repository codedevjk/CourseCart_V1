import { Component, OnInit } from '@angular/core';
import { EnrollmentService } from '../../../services/enrollment.service';
import { CatalogService } from '../../../services/catalog.service';
import { AuthService } from '../../../services/auth.service';
import { CourseDetail } from '../../../models/catalog.model';
import { Enrollment } from '../../../models/enrollment.model';
import { forkJoin, of, Observable } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

interface EnrolledCourseData {
  enrollment: Enrollment;
  course: CourseDetail | null;
  progress: number;
}

@Component({
  selector: 'app-learning-dashboard',
  templateUrl: './learning-dashboard.component.html',
  styleUrls: ['./learning-dashboard.component.css']
})
export class LearningDashboardComponent implements OnInit {
  enrolledCourses: EnrolledCourseData[] = [];
  
  isLoading = true;
  errorMessage = '';

  constructor(
    private enrollmentService: EnrollmentService,
    private catalogService: CatalogService,
    private authService: AuthService
  ) {}

  getCardColor(index: number): string {
    return 'theme-' + (index % 4);
  }

  getCategoryInitials(categoryName: string): string {
    if (!categoryName) return 'CC';
    const words = categoryName.split(' ');
    if (words.length > 1) {
      return (words[0][0] + words[1][0]).toUpperCase();
    } else if (categoryName.length >= 2) {
      return categoryName.substring(0, 2).toUpperCase();
    }
    return categoryName[0].toUpperCase();
  }

  ngOnInit(): void {
    const user = this.authService.getCurrentUser();
    if (!user) return; // Should be handled by AuthGuard

    this.isLoading = true;
    this.errorMessage = '';

    this.enrollmentService.getEnrollments(user.id).pipe(
      switchMap(enrollments => {
        if (enrollments.length === 0) {
          return of([]);
        }
        
        // For each enrollment, fetch course details and progress concurrently
        const courseRequests: Observable<EnrolledCourseData>[] = enrollments.map(enr => {
          return forkJoin({
            course: this.catalogService.getCourse(enr.courseId).pipe(catchError(() => of(null))),
            completedLessonIds: this.enrollmentService.getLessonProgress(enr.id).pipe(catchError(() => of([])))
          }).pipe(
            map(result => {
              let progress = 0;
              if (result.course && result.course.lessons && result.course.lessons.length > 0) {
                let validCompletedCount = 0;
                const completedSet = new Set(result.completedLessonIds);
                for (const lesson of result.course.lessons) {
                  if (completedSet.has(lesson.id)) {
                    validCompletedCount++;
                  }
                }
                progress = (validCompletedCount / result.course.lessons.length) * 100;
              }
              return {
                enrollment: enr,
                course: result.course,
                progress: progress
              };
            })
          );
        });

        return forkJoin(courseRequests);
      })
    ).subscribe({
      next: (data) => {
        this.enrolledCourses = data.filter(d => d.course !== null);
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load your learning dashboard. Please try again.';
        this.isLoading = false;
      }
    });
  }
}

