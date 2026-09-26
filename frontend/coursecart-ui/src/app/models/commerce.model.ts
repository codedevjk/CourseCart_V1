export interface Order {
  id: number;
  userId: number;
  courseId: number;
  amountPaid: number;
  orderDate: string;
  paymentMethod?: string;
}

export interface CheckoutRequest {
  userId: number;
  courseId: number;
  paymentMethod: string;
}

