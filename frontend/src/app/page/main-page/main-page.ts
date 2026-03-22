import {Component, inject, OnInit, signal} from '@angular/core';

import {FilterBar} from '../../components/filter-bar/filter-bar';
import {GameCard} from '../../components/game-card/game-card';
import {BackendService} from '../../services/backend-service';

@Component({
  selector: 'app-main-page',
  imports: [FilterBar, GameCard],
  templateUrl: './main-page.html',
  styleUrl: './main-page.css',
})
export class MainPage implements OnInit {
  protected backendApi = inject(BackendService);
  protected currentFilter = signal<string>('');

  ngOnInit(): void {
    this.backendApi.getAllGames();
  }

  protected onFilterChange(filter: string) {
    this.currentFilter.set(filter);
  }
}
