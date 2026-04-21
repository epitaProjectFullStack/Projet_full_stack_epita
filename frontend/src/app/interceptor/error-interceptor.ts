import {HttpEventType, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {tap} from 'rxjs';

import {ErrorHandling} from '../services/error-handling';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorService = inject(ErrorHandling)

  return next(req).pipe(tap((event) => {
    console.log(event);
    if (event.type === HttpEventType.Response && !event.ok) {
      console.error(`Error on url ${req.url}`);
      console.error(event);

      errorService.addMessage(`An error occured (code: ${event.status})`);
    }
  }));
};
