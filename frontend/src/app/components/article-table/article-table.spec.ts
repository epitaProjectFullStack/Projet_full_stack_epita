import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleTable } from './article-table';

describe('ArticleTable', () => {
  let component: ArticleTable;
  let fixture: ComponentFixture<ArticleTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArticleTable);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
