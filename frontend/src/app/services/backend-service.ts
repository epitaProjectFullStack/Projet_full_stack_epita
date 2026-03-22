import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';

import {environment} from '../../environments/environment';
import {Game} from '../interface/game';


@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private http = inject(HttpClient);
  private backendUrl = environment.apiUrl;

  public gamesList = signal<Game[]>([])

  getAllGames() {
    console.log(this.gamesList().length);
    if (this.gamesList().length === 0) {
      this.http.get<{list: Game[]}>(this.backendUrl + 'games')
          .subscribe(response => {
            console.log(response.list);
            this.gamesList.set(response.list);
          })
    }
  }
}
