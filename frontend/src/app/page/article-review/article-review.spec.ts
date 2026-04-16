import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleReview } from './article-review';

describe('ArticleReview', () => {
  let component: ArticleReview;
  let fixture: ComponentFixture<ArticleReview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleReview]
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
