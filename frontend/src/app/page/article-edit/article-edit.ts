import {Component, inject, signal, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {ArticleEditor} from '../../components/article-editor/article-editor';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-article-edit',
  imports: [ArticleEditor],
  templateUrl: './article-edit.html',
  styleUrl: './article-edit.css',
})
export class ArticleEdit {
  game = signal<Game|undefined>(undefined);

  private activatedRoute = inject(ActivatedRoute);
  private backendService = inject(BackendService);
  private router = inject(Router);

  @ViewChild(ArticleEditor) private articleEditor!: ArticleEditor;

  constructor() {
    this.activatedRoute.params.subscribe((params) => {
      const id: string = params['id'];
      this.backendService.getAllGames().subscribe(response => {
        this.game.set(response.list.find(game => game.uuid === id));
        console.log(`Edit got contemt ${this.game()?.articleContent}`)
      })
    })
  }

  onValidate() {
    this.backendService
        .editGame(
            this.game()!.uuid, this.game()!.subjectGameName,
            this.game()!.articleName, this.articleEditor.html)
        .subscribe(() => {
          this.router.navigate(['']);
        })
  }
}
