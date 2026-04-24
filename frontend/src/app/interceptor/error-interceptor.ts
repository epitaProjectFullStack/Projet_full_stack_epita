import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {catchError, of} from 'rxjs';

import {ErrorHandling} from '../services/error-handling';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorHandling)

  return next(req).pipe(catchError((event: HttpErrorResponse) => {
    console.error(`Error on url ${req.url}`);
    console.error(event);

    try {
      errorService.addMessage(`${event.error.message}`);
    } catch (e) {
      errorService.addMessage(`An error occured (code: ${event.status})`);
    }

    return of();
  }));
};
