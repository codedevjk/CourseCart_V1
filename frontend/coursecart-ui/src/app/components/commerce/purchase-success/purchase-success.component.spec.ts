import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { PurchaseSuccessComponent } from './purchase-success.component';

describe('PurchaseSuccessComponent', () => {
  let component: PurchaseSuccessComponent;
  let fixture: ComponentFixture<PurchaseSuccessComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule, FormsModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA],
      declarations: [PurchaseSuccessComponent]
    });
    fixture = TestBed.createComponent(PurchaseSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
