import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrderHistoryComponent } from './order-history.component';
import { CommerceService } from '../../../services/commerce.service';
import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('OrderHistoryComponent', () => {
  let component: OrderHistoryComponent;
  let fixture: ComponentFixture<OrderHistoryComponent>;
  let commerceServiceSpy: jasmine.SpyObj<CommerceService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    commerceServiceSpy = jasmine.createSpyObj('CommerceService', ['getOrders']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);

    await TestBed.configureTestingModule({
      declarations: [OrderHistoryComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        { provide: CommerceService, useValue: commerceServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OrderHistoryComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getOrders on init', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    commerceServiceSpy.getOrders.and.returnValue(of([{ id: 1, courseId: 2, amountPaid: 10, orderDate: new Date() } as any]));

    fixture.detectChanges();

    expect(commerceServiceSpy.getOrders).toHaveBeenCalledWith(1);
    expect(component.orders.length).toBe(1);
  });

  it('should render a list of orders from response', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    commerceServiceSpy.getOrders.and.returnValue(of([{ id: 100, courseId: 200, amountPaid: 19.99, orderDate: new Date() } as any]));

    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    
    expect(compiled.innerHTML).toContain('100');
    expect(compiled.innerHTML).toContain('200');
    expect(compiled.innerHTML).toContain('19.99');
  });

  it('should show empty state component when order list is empty', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    commerceServiceSpy.getOrders.and.returnValue(of([]));

    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    
    expect(compiled.querySelector('app-empty-state')).toBeTruthy();
  });
});
