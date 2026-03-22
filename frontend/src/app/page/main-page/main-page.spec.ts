import {signal, WritableSignal} from '@angular/core';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';

import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

import {MainPage} from './main-page';

describe('MainPage', () => {
  let component: MainPage;
  let fixture: ComponentFixture<MainPage>;

  class BackendServiceStub {
    public gamesList: WritableSignal<Game[]> = signal([]);

    getAllGames(): void {
      this.gamesList.set([{
        uuid: '0000-0000-0000-0000',
        authorLogin: 'oui',
        subjectGameName: 'LoL',
        articleName: 'Magic',
        articleContent: 'Magic is magic!'
      }])
    }
  }

  beforeEach(async () => {
    await TestBed
        .configureTestingModule({
          imports: [MainPage],
          providers: [
            provideRouter([]),
            {provide: BackendService, useClass: BackendServiceStub}
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
