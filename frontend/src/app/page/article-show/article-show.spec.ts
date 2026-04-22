import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {Observable, of} from 'rxjs';

import {GameStatus} from '../../enum/game-status';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

import {ArticleShow} from './article-show';



describe('ArticleShow', () => {
  class BackendServiceStub {
    getAllGames(): Observable<{list: Game[]}>{return of({
      list: [{
        uuid: '0000-0000-0000-0000',
        authorLogin: 'oui',
        subjectGameName: 'LoL',
        articleName: 'Magic',
        articleContent: 'Magic is magic!',
        status: GameStatus.OK
      }]
    })}

    getToken() {
      return null;
    }
  }

  let component: ArticleShow;
  let fixture: ComponentFixture<ArticleShow>;

  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [ArticleShow],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub}
          ]
        })
        .compileComponents();

    fixture = TestBed.createComponent(ArticleShow);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
