import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';
import {jwtDecode} from 'jwt-decode'
import {shareReplay} from 'rxjs';

import {environment} from '../../environments/environment';
import {AdminUser} from '../interface/admin-user';
import {Game} from '../interface/game';


@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private http = inject(HttpClient);

  private backendUrl = environment.apiUrl;
  private token: any|null = null;

  public gamesList = signal<Game[]>([]);
  public adminUsersList = signal<any[]>([]);
  isAdmin = signal<boolean>(false);

  checkAdmin() {
    this.http.get<{list: AdminUser[]}>(this.backendUrl + 'admin/user')
        .subscribe({
          next: (response) => {
            this.adminUsersList.set(response.list);
          }
        });
  }

  getAdminUsers() {
    const request =
        this.http.get<{list: any[]}>(this.backendUrl + 'admin/user');

    request.subscribe({
      next: response => {
        console.log(response.list);
        this.adminUsersList.set(response.list);
      }
    });
  }
  deleteGame(id: any) {
    const request = this.http.delete(this.backendUrl + 'games/' + id);

    request.subscribe({
      next: () => {
        this.gamesList.set(this.gamesList().filter(g => g.uuid !== id));
      }
    });
  }

  deleteUser(id: any) {
    const request = this.http.delete(this.backendUrl + 'admin/user/' + id);

    request.subscribe({
      next: () => {
        this.adminUsersList.set(
            this.adminUsersList().filter((u: AdminUser) => u.id !== id));
      }
    });
  }

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
    return this.http.get<{list: Game[]}>(this.backendUrl + 'games');
  }

  getReviewerGames() {
    return this.http.get<{list: Game[]}>(this.backendUrl + 'games/review');
  }

  getAdminGames() {
    return this.http.get<{list: Game[]}>(this.backendUrl + 'games/all');
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
        this.token = jwtDecode(t.token);
      }
    });

    return replay;
  }

  getToken() {
    return this.token;
  }
}
