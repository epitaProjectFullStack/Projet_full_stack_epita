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
  private router = inject(Router);

  games = signal<Game[]>([]);

  ngOnInit(): void{this.backendService.getReviewerGames().subscribe(
      reponse => {this.games.set(reponse.list)})}

  onReviewClick(id: UUIDTypes) {
    console.log(`Review click for id ${id}`);
    this.router.navigate(['article', 'review', id]).then(b => console.log(b));
  }
}
