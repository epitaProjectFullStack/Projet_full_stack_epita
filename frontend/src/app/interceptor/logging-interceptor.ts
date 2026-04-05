import {HttpEventType, HttpInterceptorFn} from '@angular/common/http';
import {tap} from 'rxjs';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  console.log(`Http request on url ${req.url}`);
  if (req.body) {
    console.log('With body:', req.body);
  }

  return next(req).pipe(tap((event) => {
    if (event.type === HttpEventType.Response) {
      console.log(req.url, 'returned a response with status', event.status);
    }
  }));
};
