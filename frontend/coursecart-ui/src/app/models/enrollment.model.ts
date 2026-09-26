export interface Enrollment {
  id: number;
  userId: number;
  courseId: number;
  enrolledAt: string;
}

export interface LessonProgress {
  id: number;
  enrollmentId: number;
  lessonId: number;
  isCompleted: boolean;
  completedAt: string;
}
