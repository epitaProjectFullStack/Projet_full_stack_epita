import {Component, Input} from '@angular/core';

import {Game} from '../../interface/game';

@Component({
  selector: 'app-game-card',
  imports: [],
  templateUrl: './game-card.html',
  styleUrl: './game-card.css',
})
export class GameCard {
  @Input({required: true}) game!: Game;
  @Input({required: true}) actionName!: string;
  @Input({required: true}) action!: (game: Game) => void;

  onActionClick() {
    this.action(this.game);
  }
}
