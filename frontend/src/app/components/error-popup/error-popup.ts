import {Component, inject} from '@angular/core';

import {ErrorHandling} from '../../services/error-handling';

@Component({
  selector: 'app-error-popup',
  imports: [],
  templateUrl: './error-popup.html',
  styleUrl: './error-popup.css',
})
export class ErrorPopup {
  protected errorService: ErrorHandling = inject(ErrorHandling);
}
