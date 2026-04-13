import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ArticleEditor} from './article-editor';

describe('ArticleEditor', () => {
  let component: ArticleEditor;
  let fixture: ComponentFixture<ArticleEditor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({imports: [ArticleEditor]})
        .compileComponents();

    fixture = TestBed.createComponent(ArticleEditor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be bind to html', async () => {
    const text = 'Salut!';
    component.editor.setContent(text);

    await fixture.whenStable();
    fixture.detectChanges();
    expect(component.html).toBe(`<p>${text}</p>`);
  })
});
