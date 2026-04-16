import {TestBed} from '@angular/core/testing';

import {CacheService} from './cache-service';

describe('Cache', () => {
  let service: CacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CacheService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set key', () => {
    service.set('oui', 'non');
    expect(service.get('oui')).eq('non');
  })

  it('should return null after invalidate', () => {
    service.set('k', 'v');
    expect(service.get('k')).eq('v');

    service.invalidate('k');

    expect(service.get('k')).toBeNull();
  });

  it('should return null on all after clear', () => {
    const vals: {[id: string]: any} = {k1: 'v1', k2: 8, k3: true};

    Object.keys(vals).forEach(k => {service.set(k, vals[k])});
    Object.keys(vals).forEach(k => {expect(service.get(k)).eq(vals[k])});

    service.clear();

    Object.keys(vals).forEach(k => {expect(service.get(k)).toBeNull()});
  })
});
