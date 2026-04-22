import {inject, TestBed} from '@angular/core/testing';
import {BrowserModule, DomSanitizer} from '@angular/platform-browser';

import {SafeHtmlPipe} from './safe-html-pipe';

describe('SafeHtmlPipe', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({imports: [BrowserModule]});
  });

  it('create an instance',
     inject([DomSanitizer], (domSanitizer: DomSanitizer) => {
       let pipe = new SafeHtmlPipe(domSanitizer);
       expect(pipe).toBeTruthy();
     }));
});
