import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {ArticleNew} from './article-new';

describe('ArticleNew', () => {
  let component: ArticleNew;
  let fixture: ComponentFixture<ArticleNew>;

  beforeEach(async () => {
    await TestBed
        .configureTestingModule(
            {imports: [ArticleNew, FormsModule, ReactiveFormsModule]})
        .compileComponents();

    fixture = TestBed.createComponent(ArticleNew);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid', () => {
    expect(component.articleForm.valid).toBeFalsy();
  });

  it('Game Name validity', () => {
    let gameName = component.articleForm.controls['gameName'];
    expect(gameName.valid).toBeFalsy();

    gameName.setValue('Mario');
    expect(gameName.valid).toBeTruthy();
  });

  it('Article Name validity', () => {
    let articleName = component.articleForm.controls['articleName'];
    expect(articleName.valid).toBeFalsy();

    articleName.setValue('How to jump?');
    expect(articleName.valid).toBeTruthy();
  });

  it('Form validity', () => {
    let gameName = component.articleForm.controls['gameName'];
    let articleName = component.articleForm.controls['articleName'];

    expect(component.articleForm.valid).toBeFalsy();

    gameName.setValue('Mario');
    expect(component.articleForm.valid).toBeFalsy();

    articleName.setValue('How to jump?');
    expect(component.articleForm.valid).toBeTruthy();

    gameName.setValue(null);
    expect(component.articleForm.valid).toBeFalsy();
  })
});
