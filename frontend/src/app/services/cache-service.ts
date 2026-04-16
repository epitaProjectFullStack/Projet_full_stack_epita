import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CacheService {
  private cache: {[id: string]: any} = {};

  set(key: string, value: any) {
    this.cache[key] = value;
  }

  get(key: string): any|null {
    return this.cache[key] !== undefined ? this.cache[key] : null;
  }

  invalidate(key: string) {
    this.cache[key] = undefined;
  }

  clear() {
    this.cache = {};
  }
}
