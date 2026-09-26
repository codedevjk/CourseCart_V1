export interface Category {
  id: number;
  name: string;
}

export interface Lesson {
  id: number;
  courseId: number;
  title: string;
  content: string;
  displayOrder: number;
}

export interface Course {
  id: number;
  category: Category | null;
  title: string;
  description: string;
  price: number;
  originalPrice?: number;
  instructorName?: string;
  rating?: number;
  ratingCount?: number;
  bestseller?: boolean;
  lessonCount?: number;

  status: 'DRAFT' | 'ACTIVE' | 'INACTIVE';
}

export interface CourseDetail {
  id: number;
  title: string;
  description: string;
  price: number;
  status: 'DRAFT' | 'ACTIVE' | 'INACTIVE';
  category: Category | null;

  lessons: Lesson[];
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  last: boolean;
  size: number;
  number: number;
}

