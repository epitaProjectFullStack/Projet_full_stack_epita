import {Component, inject, ViewChild} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {ArticleEditor} from '../../components/article-editor/article-editor';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-article-new',
  imports: [ArticleEditor, FormsModule, ReactiveFormsModule],
  templateUrl: './article-new.html',
  styleUrl: './article-new.css',
})
export class ArticleNew {
  private backend = inject(BackendService);
  private router = inject(Router);

  articleForm = new FormGroup({
    articleName: new FormControl('', [Validators.required]),
    gameName: new FormControl('', [Validators.required]),
  });

  @ViewChild(ArticleEditor) private articleEditor!: ArticleEditor;

  protected onCreateClick() {
    if (this.articleForm.valid) {
      this.backend
          .createGame(
              this.articleForm.value.gameName!!,
              this.articleForm.value.articleName!!, this.articleEditor.html)
          .subscribe(() => {this.router.navigate(['/'])})
    }
  }
}
