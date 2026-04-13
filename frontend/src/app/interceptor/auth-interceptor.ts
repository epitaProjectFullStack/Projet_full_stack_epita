import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';

import {BackendService} from '../services/backend-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const backendService = inject(BackendService);
  const token = backendService.getToken();

  if (token !== null) {
    const cloned = req.clone({setHeaders: {Authorization: token}});
    return next(cloned);
  } else {
    return next(req);
  }
};
