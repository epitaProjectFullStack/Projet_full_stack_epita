import {ComponentFixture, TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';

import {GameStatus} from '../../enum/game-status';

import {GameCard} from './game-card';

describe('GameCard', () => {
  let component: GameCard;
  let fixture: ComponentFixture<GameCard>;

  beforeEach(async () => {
    await TestBed
        .configureTestingModule(
            {imports: [GameCard], providers: [provideRouter([])]})
        .compileComponents();

    fixture = TestBed.createComponent(GameCard);
    component = fixture.componentInstance;
    component.game = {
      uuid: '0000-0000-0000-0000',
      authorLogin: 'oui',
      subjectGameName: 'LoL',
      articleName: 'Magic',
      articleContent: 'Magic is magic!',
      status: GameStatus.OK
    };
    fixture.detectChanges();
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
