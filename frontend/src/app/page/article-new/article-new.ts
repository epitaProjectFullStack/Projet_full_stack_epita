import {Component, ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

import {ArticleEditor} from '../../components/article-editor/article-editor';

@Component({
  selector: 'app-article-new',
  imports: [ArticleEditor, FormsModule, ReactiveFormsModule],
  templateUrl: './article-new.html',
  styleUrl: './article-new.css',
})
export class ArticleNew {
  articleForm = new FormGroup({
    articleName: new FormControl('', [Validators.required]),
    gameName: new FormControl('', [Validators.required]),
  });

  @ViewChild(ArticleEditor) private articleEditor!: ArticleEditor;

  protected onCreateClick() {
    if (this.articleForm.valid) {
      console.log(this.articleForm.value);
      console.log(this.articleEditor.html);
    }
  }
}
