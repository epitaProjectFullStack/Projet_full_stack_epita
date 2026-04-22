import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {Router} from '@angular/router';
import {UUIDTypes} from 'uuid';

import {GameCard} from '../../components/game-card/game-card';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';
import {WebSocketService} from '../../services/websocket.service';

@Component({
  selector: 'app-reviewer-board',
  imports: [GameCard],
  templateUrl: './reviewer-board.html',
  styleUrl: './reviewer-board.css',
})
export class ReviewerBoard implements OnInit, OnDestroy {
  backendService = inject(BackendService);
  private router = inject(Router);
  private ws = inject(WebSocketService);

  private kafkaCallback = () => {
    this.backendService.getReviewerGames().subscribe(
        reponse => {this.games.set(reponse.list)});
  };

  games = signal<Game[]>([]);

  ngOnInit(): void {
    this.backendService.getReviewerGames().subscribe(
        reponse => {this.games.set(reponse.list)});

    this.ws.listen(this.kafkaCallback);
  }

  ngOnDestroy(): void {
    this.ws.unlisten(this.kafkaCallback);
  }

  onReviewClick(id: UUIDTypes) {
    this.router.navigate(['article', 'review', id]).then(b => console.log(b));
  }
}
