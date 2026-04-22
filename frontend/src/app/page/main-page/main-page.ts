import {Component, inject, OnDestroy, OnInit, signal} from '@angular/core';
import {Router} from '@angular/router';

import {FilterBar} from '../../components/filter-bar/filter-bar';
import {GameCard} from '../../components/game-card/game-card';
import {Game} from '../../interface/game';
import {BackendService} from '../../services/backend-service';
import {WebSocketService} from '../../services/websocket.service';

@Component({
  selector: 'app-main-page',
  imports: [FilterBar, GameCard],
  templateUrl: './main-page.html',
  styleUrl: './main-page.css',
})
export class MainPage implements OnInit, OnDestroy {
  protected backendApi = inject(BackendService);
  protected router = inject(Router);
  private ws = inject(WebSocketService);

  protected currentFilter = signal<string>('');
  protected games = signal<Game[]>([]);

  private kafkaCallback = () => {
    this.backendApi.getAllGames().subscribe(
        response => {this.games.set(response.list)});
  };

  ngOnInit(): void {
    this.backendApi.getAllGames().subscribe(
        response => {this.games.set(response.list)});

    this.ws.listen(this.kafkaCallback);
  }

  ngOnDestroy(): void {
    this.ws.unlisten(this.kafkaCallback);
  }

  protected onFilterChange(filter: string) {
    this.currentFilter.set(filter);
  }
}
