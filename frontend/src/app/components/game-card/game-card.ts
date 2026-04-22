import {Component, Input, OnInit} from '@angular/core';

import {Game} from '../../interface/game';

@Component({
  selector: 'app-game-card',
  imports: [],
  templateUrl: './game-card.html',
  styleUrl: './game-card.css',
})
export class GameCard implements OnInit {
  @Input({required: true}) game!: Game;
  @Input({required: true}) actionName!: string;
  @Input({required: true}) action!: (game: Game) => void;

  protected splitContent!: string;

  ngOnInit(): void {
    const parser = new DOMParser();
    const doc = parser.parseFromString(this.game.articleContent, 'text/html');
    this.splitContent = doc.body.textContent || '';
  }

  onActionClick() {
    this.action(this.game);
  }
}
