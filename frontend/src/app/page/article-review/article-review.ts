import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {Article} from '../../components/article/article';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-article-review',
  imports: [Article],
  templateUrl: './article-review.html',
  styleUrl: './article-review.css',
})
export class ArticleReview {
  game = signal<Game|undefined>(undefined);

  private activatedRoute = inject(ActivatedRoute);
  private backendService = inject(BackendService);

  constructor() {
    this.activatedRoute.params.subscribe((params) => {
      const id = params['id'];
      this.game.set(
          this.backendService.gamesList().find(game => game.uuid === id));
    })
  }

  onApprove() {}

  onRefuse() {}
}
