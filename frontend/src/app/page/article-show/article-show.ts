import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {Article} from '../../components/article/article';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-article-show',
  imports: [Article],
  templateUrl: './article-show.html',
  styleUrl: './article-show.css',
})

export class ArticleShow {
  game = signal<Game|undefined>(undefined);
  madeByUser!: boolean;

  private activatedRoute = inject(ActivatedRoute);
  private backendService = inject(BackendService);
  private router = inject(Router);

  constructor() {
    this.activatedRoute.params.subscribe((params) => {
      const id = params['id'];
      this.backendService.getAllGames().subscribe(response => {
        this.game.set(response.list.find(g => g.uuid === id));

        const token = this.backendService.getToken();
        this.madeByUser =
            token !== null && token.payload.login === this.game()!!.authorLogin;
      });
    })
  }

  onDeleteClick() {
    this.backendService.deleteGame(this.game()!.uuid).subscribe(() => {
      this.router.navigate(['']);
    })
  }

  onModifyClick() {
    this.router.navigate(['article', 'edit', this.game()!.uuid]);
  }
}
