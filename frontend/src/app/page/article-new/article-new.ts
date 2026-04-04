import {Component, ViewChild} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {ArticleEditor} from '../../components/article-editor/article-editor';

@Component({
  selector: 'app-article-new',
  imports: [ArticleEditor, FormsModule],
  templateUrl: './article-new.html',
  styleUrl: './article-new.css',
})
export class ArticleNew {
  gameName: string = '';
  articleName: string = '';

  @ViewChild(ArticleEditor) private articleEditor!: ArticleEditor;

  protected onCreateClick() {
    console.log(`Creating article ${this.articleName} about game ${
        this.gameName} with html:\n${this.articleEditor.html}`);
  }
}
