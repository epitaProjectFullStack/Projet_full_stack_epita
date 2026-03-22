import {Component, Input} from '@angular/core';
import {RouterLink} from '@angular/router';

import {Game} from '../../interface/game';

@Component({
  selector: 'app-game-card',
  imports: [RouterLink],
  templateUrl: './game-card.html',
  styleUrl: './game-card.css',
})
export class GameCard {
  @Input({required: true}) game!: Game;
}
