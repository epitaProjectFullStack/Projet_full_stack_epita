import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {Observable, of} from 'rxjs';

import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

import {ArticleReview} from './article-review';

describe('ArticleReview', () => {
  class BackendServiceStub {
    getReviewerGames(): Observable<{list: Game[]}> {
      return of({
        list: [{
          uuid: '0000-0000-0000-0000',
          authorLogin: 'oui',
          subjectGameName: 'LoL',
          articleName: 'Magic',
          articleContent: 'Magic is magic!'
        }]
      })
    }
  }

  let component: ArticleReview;
  let fixture: ComponentFixture<ArticleReview>;

  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [ArticleReview],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub}
          ]
        })
        .compileComponents();

    fixture = TestBed.createComponent(ArticleReview);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
