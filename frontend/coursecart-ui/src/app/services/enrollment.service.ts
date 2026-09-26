import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enrollment } from '../models/enrollment.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private apiUrl = `${environment.apiBaseUrl}/enrollments`;

  constructor(private http: HttpClient) { }

  getEnrollments(userId: number): Observable<Enrollment[]> {
    const params = new HttpParams().set('userId', userId.toString());
    return this.http.get<Enrollment[]>(this.apiUrl, { params });
  }

  getLessonProgress(enrollmentId: number): Observable<number[]> {
    return this.http.get<number[]>(`${this.apiUrl}/${enrollmentId}/progress`);
  }

  completeLesson(enrollmentId: number, lessonId: number, completed: boolean = true): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${enrollmentId}/lessons/${lessonId}/complete`, { completed });
  }

  getEnrollmentCount(): Observable<{ totalEnrollments: number }> {
    return this.http.get<{ totalEnrollments: number }>(`${this.apiUrl}/count`);
  }
}
