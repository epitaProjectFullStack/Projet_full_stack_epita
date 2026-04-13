import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';
import {Observable, shareReplay} from 'rxjs';

import {environment} from '../../environments/environment';
import {Game} from '../interface/game';


@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private http = inject(HttpClient);

  private backendUrl = environment.apiUrl;
  private token: string|null = null;

  public gamesList = signal<Game[]>([]);


  private createPost<T>(
      url: string, body: any, headers?: {[id: string]: string}) {
    if (headers === undefined) {
      headers = {};
    }

    headers['content-type'] = 'application/json';
    const bodyJson = JSON.stringify(body);

    return this.http.post<T>(url, bodyJson, {headers: headers});
  }

  getAllGames() {
    if (this.gamesList().length === 0) {
      const request = this.http.get<{list: Game[]}>(this.backendUrl + 'games');
      request.subscribe({
        next: response => {
          console.log(response.list);
          this.gamesList.set(response.list);
        }
      });
    }
  }

  postRegister(login: string, password: string, email: string) {
    return this.createPost<void>(
        this.backendUrl + 'auth/register',
        {username: login, password: password, email: email});
  }

  postLogin(login: string, password: string) {
    const request = this.createPost<{token: string}>(
        this.backendUrl + 'auth/login', {login: login, password: password});

    const replay = request.pipe(shareReplay(1))
    replay.subscribe({
      next: (t) => {
        this.token = t.token;
        console.log(`Set JWT to ${this.token}`);
      }
    });

    return replay;
  }

  getToken() {
    return this.token;
  }
}
