import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AppComponent } from './app.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { environment } from '../environments/environment';

describe('AppComponent', () => {
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [AppComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA]
    });
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should attempt to communicate with API Gateway on init', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges(); // triggers ngOnInit

    const req = httpTestingController.expectOne(`${environment.apiBaseUrl}/catalog/categories`);
    expect(req.request.method).toEqual('GET');
    req.flush([]); // Mock success

    expect(fixture.componentInstance.apiStatus).toBe('Success');
  });

  afterEach(() => {
    httpTestingController.verify();
  });
});
