import {HttpInterceptorFn, provideHttpClient} from '@angular/common/http';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';

import {BackendService} from '../services/backend-service';
import {CacheService} from '../services/cache-service';

import {cacheInterceptor} from './cache-interceptor';

describe('cacheInterceptor', () => {
  let backendService: BackendService;
  let cacheService: CacheService;
  let httpConfig: HttpTestingController;

  const interceptor: HttpInterceptorFn = (req, next) =>
      TestBed.runInInjectionContext(() => cacheInterceptor(req, next));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CacheService, BackendService, provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    backendService = TestBed.inject(BackendService);
    cacheService = TestBed.inject(CacheService);
    httpConfig = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpConfig.verify();
    cacheService.clear();
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});
