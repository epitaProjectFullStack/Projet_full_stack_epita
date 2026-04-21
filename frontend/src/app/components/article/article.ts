import {Component, Input} from '@angular/core';

import {Game} from '../../interface/game';
import {SafeHtmlPipe} from '../../pipe/safe-html-pipe';

@Component({
  selector: 'app-article',
  imports: [SafeHtmlPipe],
  templateUrl: './article.html',
  styleUrl: './article.css',
})
export class Article {
  @Input({required: true}) game!: Game;
}
