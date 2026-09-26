import { Component, Input } from '@angular/core';
import { Course, CourseDetail } from '../../../models/catalog.model';

@Component({
  selector: 'app-course-card',
  templateUrl: './course-card.component.html',
  styleUrls: ['./course-card.component.css']
})
export class CourseCardComponent {
  @Input() course!: Course | CourseDetail | any;
  @Input() progress?: number; // Optional, for learning dashboard
  
  getCourseImage(): string {
    return this.course.imageUrl || 'assets/course-discovery.png';
  }
}
