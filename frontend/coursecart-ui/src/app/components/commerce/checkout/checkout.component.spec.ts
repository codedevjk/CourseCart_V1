import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CheckoutComponent } from './checkout.component';
import { CommerceService } from '../../../services/commerce.service';
import { CatalogService } from '../../../services/catalog.service';
import { AuthService } from '../../../services/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('CheckoutComponent', () => {
  let component: CheckoutComponent;
  let fixture: ComponentFixture<CheckoutComponent>;
  let commerceServiceSpy: jasmine.SpyObj<CommerceService>;
  let catalogServiceSpy: jasmine.SpyObj<CatalogService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    commerceServiceSpy = jasmine.createSpyObj('CommerceService', ['checkout']);
    catalogServiceSpy = jasmine.createSpyObj('CatalogService', ['getCourse']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getCurrentUser']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [CheckoutComponent],
      imports: [FormsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      providers: [
        { provide: CommerceService, useValue: commerceServiceSpy },
        { provide: CatalogService, useValue: catalogServiceSpy },
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CheckoutComponent);
    component = fixture.componentInstance;
  });

  it('should call getCourse on init', () => {
    catalogServiceSpy.getCourse.and.returnValue(of({ id: 1, title: 'Java', category: { name: 'Programming' }, price: 10 } as any));
    
    fixture.detectChanges();
    
    expect(catalogServiceSpy.getCourse).toHaveBeenCalledWith(1);
    expect(component.course).toBeDefined();
  });

  it('should call checkout and navigate to purchase-success on success', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    commerceServiceSpy.checkout.and.returnValue(of({ status: 'SUCCESS' }));
    component.courseId = 1;
    component.course = { id: 1 } as any;
    component.paymentMethod = 'CREDIT_CARD';

    component.onSubmit();

    expect(commerceServiceSpy.checkout).toHaveBeenCalledWith({ userId: 1, courseId: 1, paymentMethod: 'CREDIT_CARD' });
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/purchase-success']);
  });

  it('should set error message containing already enrolled on 409', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    commerceServiceSpy.checkout.and.returnValue(throwError(() => ({ status: 409 })));
    component.courseId = 1;
    component.course = { id: 1 } as any;
    component.paymentMethod = 'CREDIT_CARD';

    component.onSubmit();

    expect(component.errorMessage).toContain('already enrolled');
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });

  it('should set error message containing unavailable on 400', () => {
    authServiceSpy.getCurrentUser.and.returnValue({ id: 1 } as any);
    commerceServiceSpy.checkout.and.returnValue(throwError(() => ({ status: 400 })));
    component.courseId = 1;
    component.course = { id: 1 } as any;
    component.paymentMethod = 'CREDIT_CARD';

    component.onSubmit();

    expect(component.errorMessage).toContain('unavailable');
    expect(routerSpy.navigate).not.toHaveBeenCalled();
  });
});
