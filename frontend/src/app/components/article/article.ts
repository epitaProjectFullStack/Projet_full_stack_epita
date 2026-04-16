import {Component, Input} from '@angular/core';

import {Game} from '../../interface/game';

@Component({
  selector: 'app-article',
  imports: [],
  templateUrl: './article.html',
  styleUrl: './article.css',
})
export class Article {
  @Input({required: true}) game!: Game;
}
