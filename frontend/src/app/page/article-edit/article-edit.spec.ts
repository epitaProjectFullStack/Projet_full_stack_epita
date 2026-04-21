import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {Observable, of} from 'rxjs';

import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

import {ArticleEdit} from './article-edit';

describe('ArticleEdit', () => {
  let component: ArticleEdit;
  let fixture: ComponentFixture<ArticleEdit>;

  class BackendServiceStub {
    getAllGames(): Observable<{list: Game[]}>{return of({
      list: [{
        uuid: '0000-0000-0000-0000',
        authorLogin: 'oui',
        subjectGameName: 'LoL',
        articleName: 'Magic',
        articleContent: 'Magic is magic!'
      }]
    })}

    getToken() {
      return null;
    }
  }

  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [ArticleEdit],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub}
          ]
        })
        .compileComponents();

    fixture = TestBed.createComponent(ArticleEdit);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
