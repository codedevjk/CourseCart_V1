import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order, CheckoutRequest } from '../models/commerce.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommerceService {
  private apiUrl = `${environment.apiBaseUrl}/commerce`;

  constructor(private http: HttpClient) { }

  checkout(request: CheckoutRequest): Observable<any> {
    throw new Error('TODO[TRAINEE]: Send checkout POST request to backend (US 10).');
  }

    getAllOrders(page: number = 0, size: number = 5): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/orders/all`, { params });
  }
  getRecentOrders(limit: number = 5): Observable<Order[]> {
    const params = new HttpParams().set('limit', limit.toString());
    return this.http.get<Order[]>(`${this.apiUrl}/orders/recent`, { params });
  }

  getOrders(userId: number): Observable<Order[]> {
    throw new Error('TODO[TRAINEE]: Fetch orders by userId from backend (US 11).');
  }

  getRevenue(): Observable<{ totalRevenue: number }> {
    return this.http.get<{ totalRevenue: number }>(`${this.apiUrl}/revenue`);
  }
}






