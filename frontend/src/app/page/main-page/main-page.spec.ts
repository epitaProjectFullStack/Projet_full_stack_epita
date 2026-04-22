import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {Observable, of} from 'rxjs';

import {GameStatus} from '../../enum/game-status';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';
import {WebSocketService} from '../../services/websocket.service';

import {MainPage} from './main-page';

describe('MainPage', () => {
  let component: MainPage;
  let fixture: ComponentFixture<MainPage>;

  class BackendServiceStub {
    getAdminGames(): Observable<{list: Game[]}>{return of({
      list: [{
        uuid: '0000-0000-0000-0000',
        authorLogin: 'oui',
        subjectGameName: 'LoL',
        articleName: 'Magic',
        articleContent: 'Magic is magic!',
        status: GameStatus.OK
      }]
    })};

    getAllGames(): Observable<{list: Game[]}> {
      return this.getAdminGames();
    };
  }

  class WebSocketServiceStub {
    listen(callback: (event: any) => void) {
      // simulate event si besoin
      // callback({ eventType: 'GAME_CREATED' });
    }

    unlisten(callback: (event: any) => void) {}
  }
  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [MainPage],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub},
            {provide: WebSocketService, useClass: WebSocketServiceStub}
          ]
        })
        .compileComponents();

    fixture = TestBed.createComponent(MainPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
