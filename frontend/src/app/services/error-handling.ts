import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandling {
  private _errorMessages = signal<string[]>([]);
  public errorMessages = this._errorMessages.asReadonly();

  addMessage(message: string) {
    this._errorMessages.set([message, ...this._errorMessages()]);
  }
}
