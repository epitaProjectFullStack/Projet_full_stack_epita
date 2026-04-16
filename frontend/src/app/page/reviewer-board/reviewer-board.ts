import {Component, inject, OnInit, signal} from '@angular/core';
import {Router} from '@angular/router';
import {UUIDTypes} from 'uuid';

import {GameCard} from '../../components/game-card/game-card';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-reviewer-board',
  imports: [GameCard],
  templateUrl: './reviewer-board.html',
  styleUrl: './reviewer-board.css',
})
export class ReviewerBoard implements OnInit {
  backendService = inject(BackendService);
  games = signal<Game[]>([]);

  private router = inject(Router);

  ngOnInit(): void{this.backendService.getReviewerGames().subscribe(
      reponse => {this.games.set(reponse.list)})}

  onReviewClick(id: UUIDTypes) {
    this.router.navigate(['/article/review', id]);
  }
}
