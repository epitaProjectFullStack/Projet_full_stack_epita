import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UUIDTypes} from 'uuid';

import {Article} from '../../components/article/article';
import {GameStatus} from '../../enum/game-status';
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
  private router = inject(Router);

  constructor() {
    this.activatedRoute.params.subscribe((params) => {
      const id: string = params['id'];
      this.backendService.getReviewerGames().subscribe(response => {
        this.game.set(response.list.find(game => game.uuid === id));
      })
    })
  }

  onApprove() {
    if (this.game()) {
      this.backendService.changeGameStatus(this.game()!.uuid, GameStatus.OK)
          .subscribe(() => {this.router.navigate(['reviewer'])});
    }
  }

  onRefuse() {
    if (this.game()) {
      this.backendService
          .changeGameStatus(this.game()!.uuid, GameStatus.DISCARD)
          .subscribe(() => {this.router.navigate(['reviewer'])});
    }
  }
}
