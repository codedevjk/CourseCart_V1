import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { CourseDiscoveryComponent } from './components/catalog/course-discovery/course-discovery.component';
import { CourseDetailComponent } from './components/catalog/course-detail/course-detail.component';
import { CheckoutComponent } from './components/commerce/checkout/checkout.component';
import { PurchaseSuccessComponent } from './components/commerce/purchase-success/purchase-success.component';
import { LearningDashboardComponent } from './components/learning/learning-dashboard/learning-dashboard.component';
import { CourseLearningComponent } from './components/learning/course-learning/course-learning.component';
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';
import { AdminShellComponent } from './components/admin/admin-shell/admin-shell.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { CategoryManagementComponent } from './components/admin/category-management/category-management.component';
import { CourseManagementComponent } from './components/admin/course-management/course-management.component';
import { LessonManagementComponent } from './components/admin/lesson-management/lesson-management.component';
import { OrderHistoryComponent } from './components/commerce/order-history/order-history.component';
import { ProfileComponent } from './components/user/profile/profile.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'courses', component: CourseDiscoveryComponent },
  { path: 'courses/:courseId', component: CourseDetailComponent },
  { path: 'checkout/:courseId', component: CheckoutComponent, canActivate: [AuthGuard] },
  { path: 'purchase-success', component: PurchaseSuccessComponent, canActivate: [AuthGuard] },
  { path: 'learning', component: LearningDashboardComponent, canActivate: [AuthGuard] },
  { path: 'learning/:courseId', component: CourseLearningComponent, canActivate: [AuthGuard] },
  { path: 'orders', component: OrderHistoryComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  
  // Admin Routes
  { 
    path: 'admin', 
    component: AdminShellComponent, 
    canActivate: [AdminGuard],
    children: [
      { path: '', component: AdminDashboardComponent },
      { path: 'categories', component: CategoryManagementComponent },
      { path: 'courses', component: CourseManagementComponent },
      { path: 'courses/:courseId/lessons', component: LessonManagementComponent }
    ]
  },
  
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

