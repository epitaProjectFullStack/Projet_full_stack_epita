import {HttpEventType, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {tap} from 'rxjs';

import {ErrorHandling} from '../services/error-handling';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorHandling)

  return next(req).pipe(tap((event) => {
    if (event.type === HttpEventType.Response && !event.ok) {
      console.error(event);
      errorService.addMessage(`Error code ${event.status}: ${event.body}`)
    }
  }));
};
