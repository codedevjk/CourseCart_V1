import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogService } from '../../../services/catalog.service';
import { CommerceService } from '../../../services/commerce.service';
import { AuthService } from '../../../services/auth.service';
import { CourseDetail } from '../../../models/catalog.model';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  courseId!: number;
  course: CourseDetail | null = null;
  
  selectedTab: string = 'Card';
  
  cardNumber: string = '';
  cardExpiry: string = '';
  cardCvc: string = '';
  
  upiId: string = '';
  
  selectedBank: string = 'Demo National Bank';
  banks: string[] = [
    'Demo National Bank',
    'State Bank of India',
    'HDFC Bank',
    'ICICI Bank',
    'Axis Bank',
    'Kotak Mahindra Bank',
    'Bank of Baroda',
    'Punjab National Bank'
  ];

  
  
  isLoading = true;
  isProcessing = false;
  errorMessage = '';

  isPastDate(expiry: string): boolean {
    if (!expiry || !expiry.match(/^(0[1-9]|1[0-2])\/\d{2}$/)) return false;
    const parts = expiry.split('/');
    const month = parseInt(parts[0], 10);
    const year = parseInt(parts[1], 10) + 2000;
    const now = new Date();
    const currentYear = now.getFullYear();
    const currentMonth = now.getMonth() + 1;
    if (year < currentYear) return true;
    if (year === currentYear && month < currentMonth) return true;
    return false;
  }

  constructor(
    private route: ActivatedRoute,
    private catalogService: CatalogService,
    private commerceService: CommerceService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('courseId');
    if (idParam) {
      this.courseId = +idParam;
      this.fetchCourse();
    } else {
      this.errorMessage = 'Invalid course ID';
      this.isLoading = false;
    }
  }

  fetchCourse(): void {
    this.catalogService.getCourse(this.courseId).subscribe({
      next: (data) => {
        this.course = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Failed to load course details for checkout.';
        this.isLoading = false;
      }
    });
  }

  setTab(tab: string) {
    this.selectedTab = tab;
    this.errorMessage = '';
  }

  onSubmit(): void {
    if (!this.course) return;
    
    this.errorMessage = '';

    if (this.selectedTab === 'Card') {
      const cardClean = this.cardNumber.replace(/\s+/g, '');
      if (!/^\d{16}$/.test(cardClean)) {
        this.errorMessage = 'Please enter a valid 16-digit card number.';
        return;
      }
      if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(this.cardExpiry)) {
        this.errorMessage = 'Please enter a valid expiry date (MM/YY).';
        return;
      }
      if (!/^\d{3}$/.test(this.cardCvc)) {
        this.errorMessage = 'Please enter a valid 3-digit CVC.';
        return;
      }
    } else if (this.selectedTab === 'UPI') {
      if (!this.upiId || !this.upiId.includes('@')) {
        this.errorMessage = 'Please enter a valid UPI ID containing "@".';
        return;
      }
    } else if (this.selectedTab === 'Net Banking') {
      if (!this.selectedBank) {
        this.errorMessage = 'Please select a bank.';
        return;
      }
    }

    

    const user = this.authService.getCurrentUser();
    if (!user) {
      this.router.navigate(['/login']);
      return;
    }

    this.isProcessing = true;
    
    let finalPaymentMethod = this.selectedTab;
    if (this.selectedTab === 'Net Banking') {
      finalPaymentMethod = 'Net Banking (' + this.selectedBank + ')';
    } else if (this.selectedTab === 'Card') {
      finalPaymentMethod = 'Credit Card';
    }

    const request = {
      userId: user.id,
      courseId: this.course.id,
      paymentMethod: finalPaymentMethod
    };

    // TODO[TRAINEE]: Call commerceService.checkout() with the request payload (US 10).
    // Handle success (navigate to /purchase-success) and errors (409 conflict, 400 bad request).
    throw new Error('TODO[TRAINEE]: Implement checkout submission logic');
  }
}



