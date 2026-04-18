import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {ApplicationConfig, provideBrowserGlobalErrorListeners} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {authInterceptor} from './interceptor/auth-interceptor';
import {cacheInterceptor} from './interceptor/cache-interceptor';
import {errorInterceptor} from './interceptor/error-interceptor';
import {loggingInterceptor} from './interceptor/logging-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(), provideRouter(routes),
    provideHttpClient(withInterceptors([
      authInterceptor, errorInterceptor, loggingInterceptor, cacheInterceptor
    ]))
  ]
};
