import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { NavbarComponent } from './components/shell/navbar/navbar.component';
import { FooterComponent } from './components/shell/footer/footer.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { CourseDiscoveryComponent } from './components/catalog/course-discovery/course-discovery.component';
import { CourseDetailComponent } from './components/catalog/course-detail/course-detail.component';
import { CourseCardComponent } from './components/catalog/course-card/course-card.component';
import { CheckoutComponent } from './components/commerce/checkout/checkout.component';
import { PurchaseSuccessComponent } from './components/commerce/purchase-success/purchase-success.component';
import { LearningDashboardComponent } from './components/learning/learning-dashboard/learning-dashboard.component';
import { CourseLearningComponent } from './components/learning/course-learning/course-learning.component';
import { LoadingSpinnerComponent } from './components/shared/loading-spinner/loading-spinner.component';
import { EmptyStateComponent } from './components/shared/empty-state/empty-state.component';
import { ErrorStateComponent } from './components/shared/error-state/error-state.component';
import { ProgressBarComponent } from './components/shared/progress-bar/progress-bar.component';
import { AdminShellComponent } from './components/admin/admin-shell/admin-shell.component';
import { AdminDashboardComponent } from './components/admin/admin-dashboard/admin-dashboard.component';
import { CategoryManagementComponent } from './components/admin/category-management/category-management.component';
import { CourseManagementComponent } from './components/admin/course-management/course-management.component';
import { LessonManagementComponent } from './components/admin/lesson-management/lesson-management.component';
import { OrderHistoryComponent } from './components/commerce/order-history/order-history.component';
import { ProfileComponent } from './components/user/profile/profile.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    CourseDiscoveryComponent,
    CourseDetailComponent,
    CourseCardComponent,
    CheckoutComponent,
    PurchaseSuccessComponent,
    LearningDashboardComponent,
    CourseLearningComponent,
    LoadingSpinnerComponent,
    EmptyStateComponent,
    ErrorStateComponent,
    ProgressBarComponent,
    AdminShellComponent,
    AdminDashboardComponent,
    CategoryManagementComponent,
    CourseManagementComponent,
    LessonManagementComponent,
    OrderHistoryComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

