import { Component, OnInit } from '@angular/core';
import { CommerceService } from '../../../services/commerce.service';
import { CatalogService } from '../../../services/catalog.service';
import { AuthService } from '../../../services/auth.service';
import { Order } from '../../../models/commerce.model';
import { CourseDetail } from '../../../models/catalog.model';
import { forkJoin, of, Observable } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

interface EnrichedOrder {
  order: Order;
  course: CourseDetail | null;
}

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent implements OnInit {
  orders: EnrichedOrder[] = [];
  loading: boolean = true;
  error: string = '';

  constructor(
    private commerceService: CommerceService,
    private catalogService: CatalogService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.id) {
      this.error = 'User not logged in.';
      this.loading = false;
      return;
    }

    // TODO[TRAINEE]: Fetch order history for the current user and enrich with course details (US 11).
    throw new Error('TODO[TRAINEE]: Implement fetching order history');
  }

  getCardColor(index: number): string {
    return "theme-${index % 4}";
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
}
