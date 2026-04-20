import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';

import {BackendService} from '../services/backend-service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const backendService = inject(BackendService);
  const token = backendService.getToken();

  if (token !== null) {
    if (token.expAccessToken.getUTCSeconds() < Date.now()) {
      // Invalid access but valid refresh token.
      backendService.doRefresh().subscribe(
          () => {return next(req.clone({
            setHeaders: {Authorization: backendService.getToken()!!.accessToken}
          }))});
    } else {
      // Valid access token.
      const cloned =
          req.clone({setHeaders: {Authorization: token.accessToken}});
      return next(cloned);
    }
  }

  // No token
  return next(req);
}
