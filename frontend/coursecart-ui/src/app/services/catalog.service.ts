import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course, CourseDetail, Category, Lesson, PageResponse } from '../models/catalog.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
  private apiUrl = `${environment.apiBaseUrl}/catalog`;

  constructor(private http: HttpClient) { }

  getCategories(): Observable<Category[]> {
    throw new Error('TODO[TRAINEE]: Fetch all categories from catalog-service (US 04).');
  }

  createCategory(category: Category): Observable<Category> {
    throw new Error('TODO[TRAINEE]: Create category via catalog-service (US 04).');
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    throw new Error('TODO[TRAINEE]: Update category via catalog-service (US 04).');
  }

  deleteCategory(id: number): Observable<void> {
    throw new Error('TODO[TRAINEE]: Delete category via catalog-service (US 04).');
  }

  getCourses(categoryId?: number, search?: string, page?: number, size?: number): Observable<PageResponse<Course>> {
    throw new Error('TODO[TRAINEE]: Fetch active courses using pagination and filters from catalog-service (US 08).');
  }

  getCourse(id: number): Observable<CourseDetail> {
    throw new Error('TODO[TRAINEE]: Fetch detailed active course by ID from catalog-service (US 09).');
  }

  getAdminCourses(): Observable<Course[]> {
    throw new Error('TODO[TRAINEE]: Fetch all courses for admin from catalog-service (US 05).');
  }

  createCourse(course: Course): Observable<Course> {
    throw new Error('TODO[TRAINEE]: Create new course via catalog-service (US 05).');
  }

  updateCourse(id: number, course: Course): Observable<Course> {
    throw new Error('TODO[TRAINEE]: Update existing course via catalog-service (US 05).');
  }

  updateCourseStatus(id: number, status: string): Observable<void> {
    throw new Error('TODO[TRAINEE]: Update course status via catalog-service (US 07).');
  }

  getCourseCount(): Observable<{ totalCourses: number }> { // Contract didn't specify exact JSON key but implied count
    return this.http.get<{ totalCourses: number }>(`${this.apiUrl}/courses/count`);
  }

  getLessons(courseId: number): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(`${this.apiUrl}/courses/${courseId}/lessons`);
  }

  createLesson(courseId: number, lesson: Lesson): Observable<Lesson> {
    return this.http.post<Lesson>(`${this.apiUrl}/courses/${courseId}/lessons`, lesson);
  }

  updateLesson(courseId: number, lessonId: number, lesson: Lesson): Observable<Lesson> {
    return this.http.put<Lesson>(`${this.apiUrl}/courses/${courseId}/lessons/${lessonId}`, lesson);
  }

  deleteLesson(courseId: number, lessonId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/courses/${courseId}/lessons/${lessonId}`);
  }
}
