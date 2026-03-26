import {TestBed} from '@angular/core/testing';

import {ErrorHandling} from './error-handling';

describe('ErrorHandling', () => {
  let service: ErrorHandling;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ErrorHandling);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it should contain 1 element', () => {
    service.addMessage('Error!');
    expect(service.errorMessages()[0]).eq('Error!');
    expect(service.errorMessages().length).eq(1);
  });

  it('it should insert at front', () => {
    service.addMessage('Error_1');
    service.addMessage('Error_2');
    expect(service.errorMessages()).toStrictEqual(['Error_2', 'Error_1']);
    expect(service.errorMessages().length).eq(2);
  });
});
