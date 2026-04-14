import {Component, inject, OnInit, signal} from '@angular/core';

import {FilterBar} from '../../components/filter-bar/filter-bar';
import {GameCard} from '../../components/game-card/game-card';
import {BackendService} from '../../services/backend-service';
import { WebSocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-main-page',
  imports: [FilterBar, GameCard],
  templateUrl: './main-page.html',
  styleUrl: './main-page.css',
})
export class MainPage implements OnInit {
  protected backendApi = inject(BackendService);
  protected currentFilter = signal<string>('');
  private ws = inject(WebSocketService);

  ngOnInit(): void {
    this.backendApi.getAllGames();

    this.ws.connect((event) => {
      console.log('EVENT RECU', event);

      // refresh liste
      this.backendApi.gamesList.set([]);
      this.backendApi.getAllGames();
    });
  }

  protected onFilterChange(filter: string) {
    this.currentFilter.set(filter);
  }
}
