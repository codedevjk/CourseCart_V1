import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-error-state',
  templateUrl: './error-state.component.html',
  styleUrls: ['./error-state.component.css']
})
export class ErrorStateComponent {
  @Input() message: string = 'An error occurred';
  @Input() submessage: string = 'Please try again later.';
}
