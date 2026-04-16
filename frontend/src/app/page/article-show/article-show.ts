import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

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

  private activatedRoute = inject(ActivatedRoute);
  private backendService = inject(BackendService);

  constructor() {
    this.activatedRoute.params.subscribe((params) => {
      const id = params['id'];
      this.backendService.getAllGames().subscribe(
          response => {this.game.set(response.list.find(g => g.uuid === id))});
    })
  }
}
