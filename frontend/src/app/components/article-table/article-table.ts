import {Component, EventEmitter, Input, Output} from '@angular/core';
import {UUIDTypes} from 'uuid';

import {Game} from '../../interface/game';

@Component({
  selector: 'app-article-table',
  imports: [],
  templateUrl: './article-table.html',
  styleUrl: './article-table.css',
})
export class ArticleTable {
  @Input({required: true}) articles!: Game[];

  @Output() delete = new EventEmitter<UUIDTypes>();
}
