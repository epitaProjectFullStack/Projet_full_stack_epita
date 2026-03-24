import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';

import {environment} from '../../environments/environment';
import {Game} from '../interface/game';

import {ErrorHandling} from './error-handling';


@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private http = inject(HttpClient);
  private errorHandling = inject(ErrorHandling);

  private backendUrl = environment.apiUrl;

  public gamesList = signal<Game[]>([])

  getAllGames() {
    console.log(this.gamesList().length);
    if (this.gamesList().length === 0) {
      const request = this.http.get<{list: Game[]}>(this.backendUrl + 'games');
      request.subscribe({
        next: response => {
          console.log(response.list);
          this.gamesList.set(response.list);
        }
      })

      request.subscribe({
        error: error => {
          console.error('Error on fetch all games: ', error);
          this.errorHandling.addMessage(error.message);
        }
      })
    }
  }
}
