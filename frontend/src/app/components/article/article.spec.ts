import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameStatus} from '../../enum/game-status';

import {Article} from './article';

describe('Article', () => {
  let component: Article;
  let fixture: ComponentFixture<Article>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({imports: [Article]})
        .compileComponents();

    fixture = TestBed.createComponent(Article);
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
