import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';
import {v4} from 'uuid';

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
    if (this.gamesList().length === 0) {
      this.gamesList.set([{
        uuid: v4(),
        authorLogin: 'oui',
        subjectGameName: 'LoL',
        articleName: 'Magic',
        articleContent: 'Magic is magic!'
      }]);
    }
  }
}
