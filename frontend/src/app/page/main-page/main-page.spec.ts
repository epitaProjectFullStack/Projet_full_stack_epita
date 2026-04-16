import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';
import {Observable, of} from 'rxjs';

import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

import {MainPage} from './main-page';
import { WebSocketService } from '../../services/websocket.service';

describe('MainPage', () => {
  let component: MainPage;
  let fixture: ComponentFixture<MainPage>;

  class BackendServiceStub {
    getAllGames(): Observable<{list: Game[]}> {
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

  class WebSocketServiceStub {
    connect() {
      // do nothing
    }
  }
  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [MainPage],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub},
            { provide: WebSocketService, useClass: WebSocketServiceStub }
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
