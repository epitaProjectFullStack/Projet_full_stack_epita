import {HttpClient} from '@angular/common/http';
import {inject, Injectable, signal} from '@angular/core';
import {jwtDecode} from 'jwt-decode'
import {of, shareReplay} from 'rxjs';

import {environment} from '../../environments/environment';
import {Role, stringToRole} from '../enum/role';
import {AdminUser} from '../interface/admin-user';
import {Game} from '../interface/game';
import {Tokens} from '../interface/tokens';

import {CacheService} from './cache-service';
import {ErrorHandling} from './error-handling';


@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private http = inject(HttpClient);
  private cache = inject(CacheService);
  private errorMessager = inject(ErrorHandling);

  private backendUrl = environment.apiUrl;
  private token = signal<Tokens|null>(null);

  private setupTokens(tokens: {accessToken: string, refreshToken: string}) {
    console.log(`Setup Token: ${this.token} | ${this.token()}`);

    const payload = jwtDecode<any>(tokens.accessToken);
    console.log(payload);

    this.token.set({
      accessToken: tokens.accessToken,
      payload: {
        sub: payload['sub'],
        role: stringToRole(payload['role']),
      },
      expAccessToken: new Date(payload['exp'] * 1000),
      refreshToken: tokens.refreshToken,
    });
  }

  private resetGameCache(gameId?: string) {
    if (gameId !== undefined) {
      this.cache.invalidate(this.backendUrl + 'games/' + gameId);
      this.cache.invalidate(this.backendUrl + 'games/' + gameId + '/versions');
    }

    this.cache.invalidate(this.backendUrl + 'games');
    this.cache.invalidate(this.backendUrl + 'games/review');
    this.cache.invalidate(this.backendUrl + 'games/all');
  }

  private resetUserCache(userId?: string) {
    if (userId !== undefined) {
      this.cache.invalidate(this.backendUrl + 'user/' + userId);
      this.cache.invalidate(this.backendUrl + 'admin/user/' + userId);
    }
    this.cache.invalidate(this.backendUrl + 'user/');
    this.cache.invalidate(this.backendUrl + 'admin/user/');
  }

  checkAdmin() {
    return this.http.get<{list: AdminUser[]}>(this.backendUrl + 'admin/user');
  }

  getAdminUsers() {
    return this.http.get<{list: AdminUser[]}>(this.backendUrl + 'admin/user');
  }

  deleteGame(id: any) {
    const replay =
        this.http.delete(this.backendUrl + 'games/' + id).pipe(shareReplay(1));
    replay.subscribe(() => {
      this.resetGameCache();
    })


    return replay;
  }

  deleteUser(id: any) {
    const replay = this.http.delete(this.backendUrl + 'admin/user/' + id)
                       .pipe(shareReplay(1));

    replay.subscribe(() => {
      this.resetUserCache();
    })

    return replay;
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
        {login: login, password: password, mail: email});
  }

  postLogin(login: string, password: string) {
    console.log(`Login: ${this.token} | ${this.token()}`);
    const request =
        this.createPost<{accessToken: string, refreshToken: string}>(
            this.backendUrl + 'auth', {login: login, password: password});

    const replay = request.pipe(shareReplay(1))
    replay.subscribe((t) => {
      this.setupTokens(t);
      this.resetUserCache();
    });

    return replay;
  }

  getToken() {
    return this.token();
  }

  getRefreshToken() {
    return this.token()?.refreshToken;
  }

  getRole() {
    if (this.token() !== null) {
      return this.token()!!.payload.role;
    }

    return Role.GUEST;
  }

  removeTokens() {
    if (this.token() !== null) {
      this.createPost<void>(
              this.backendUrl + 'auth/logout',
              {refreshToken: this.token()!!.refreshToken})
          .subscribe(() => {
            this.token.set(null);
          })
    }
  }

  doRefresh() {
    const refreshToken = this.token()?.refreshToken;
    this.removeTokens();

    const replay =
        this.createPost<{accessToken: string, refreshToken: string}>(
                this.backendUrl + 'auth/refresh', {refreshToken: refreshToken})
            .pipe(shareReplay(1));
    replay.subscribe(t => this.setupTokens(t));

    return replay;
  }

  createGame(
      subjectGameName: string, articleName: string, articleContent: string) {
    if (this.token() === null) {
      this.errorMessager.addMessage(
          'You nead to be login to be able to create a game.');
      return of();
    }
    const replay = this.createPost<void>(this.backendUrl + 'games', {
                         authorId: this.token()?.payload.sub,
                         subjectGameName: subjectGameName,
                         articleName: articleName,
                         articleContent: articleContent
                       })
                       .pipe(shareReplay(1));
    replay.subscribe(() => this.resetGameCache());

    return replay;
  }
}
