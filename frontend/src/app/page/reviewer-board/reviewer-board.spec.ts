import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {Observable, of} from 'rxjs';

import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

import {ReviewerBoard} from './reviewer-board';

describe('ReviewerBoard', () => {
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
  let component: ReviewerBoard;
  let fixture: ComponentFixture<ReviewerBoard>;

  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [ReviewerBoard],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub}
          ]
        })
        .compileComponents();

    fixture = TestBed.createComponent(ReviewerBoard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
