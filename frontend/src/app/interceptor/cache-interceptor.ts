import {HttpEventType, HttpInterceptorFn, HttpResponse} from '@angular/common/http';
import {inject} from '@angular/core';
import {of, tap} from 'rxjs';

import {CacheService} from '../services/cache-service';

export const cacheInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.method !== 'GET') {
    return next(req);
  }

  const cacheService = inject(CacheService);

  const cachedResponse = cacheService.get(req.urlWithParams);

  if (cachedResponse === null) {
    return next(req).pipe(tap(response => {
      if (response.type === HttpEventType.Response) {
        cacheService.set(req.urlWithParams, response.body);
      }
    }))
  } else {
    return of(new HttpResponse({body: cachedResponse, status: 200}));
  }
};
