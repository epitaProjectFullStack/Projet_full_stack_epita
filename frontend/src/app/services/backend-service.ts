import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';
import {Observable} from 'rxjs';

import {environment} from '../../environments/environment';
import {Game} from '../interface/game';

import {ErrorHandling} from './error-handling';

export type NextCallback = (response: any) => void;

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private http = inject(HttpClient);
  private errorHandling = inject(ErrorHandling);

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

    console.log(`POST ${url}`);
    console.log(body);
    return this.http.post<T>(url, bodyJson, {headers: headers});
  }


  private errorHandlerFactory(endpoint: string) {
    return (e: any) => {
      console.error(`Error on ${endpoint}`);
      console.error(e);
      this.errorHandling.addMessage(e.message);
    }
  }

  getAllGames() {
    if (this.gamesList().length === 0) {
      const request = this.http.get<{list: Game[]}>(this.backendUrl + 'games');
      request.subscribe({
        next: response => {
          console.log(response.list);
          this.gamesList.set(response.list);
        },
        error: this.errorHandlerFactory('Get All Games')
      });
    }
  }

  postRegister(
      login: string, password: string, email: string, callback: NextCallback) {
    this.createPost<void>(
            this.backendUrl + 'auth/register',
            {username: login, password: password, email: email})
        .subscribe(
            {next: callback, error: this.errorHandlerFactory('Post Register')});
  }

  postLogin(login: string, password: string, callback: NextCallback) {
    const request = this.createPost<{token: string}>(
        this.backendUrl + 'auth/login', {login: login, password: password});

    request.subscribe({
      next: (t) => {
        this.token = t.token;
        console.log(`Set JWT to ${this.token}`);
        callback(t);
      },
      error: this.errorHandlerFactory('Post Login')
    });

    return request;
  }
}
